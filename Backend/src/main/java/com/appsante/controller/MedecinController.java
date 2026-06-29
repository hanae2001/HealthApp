package com.appsante.controller;

import com.appsante.dto.request.*;
import com.appsante.dto.response.*;
import com.appsante.entity.*;
import com.appsante.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medecin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MEDECIN')")
public class MedecinController {

    private final MedecinRepository medecinRepository;
    private final SecteurRepository secteurRepository;
    private final VilleRepository villeRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final NoteCliniqueRepository noteCliniqueRepository;
    private final SoinRepository soinRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final MedicamentOrdonnanceRepository medicamentOrdonnanceRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DT_OUT   = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @GetMapping("/profil")
    public ResponseEntity<MedecinResponse> getProfil(Authentication auth) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        Etablissement etab = m.getEtablissement();
        Secteur s = etab != null ? secteurRepository.findById(etab.getIdSecteur()).orElse(null) : null;
        Ville v   = s != null ? villeRepository.findById(s.getIdVille()).orElse(null) : null;
        return ResponseEntity.ok(new MedecinResponse(
                m.getIdMedecin(), m.getNom(), m.getPrenom(), m.getEmail(), m.getTelephone(),
                m.getNumeroOrdre(),
                etab != null ? etab.getNomEtablissement() : null,
                m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                s != null ? s.getNomSecteur() : null,
                v != null ? v.getNomVille() : null,
                m.getTarifConsultation(), m.getActif()));
    }

    @GetMapping("/disponibilites")
    public ResponseEntity<List<DisponibiliteResponse>> getDisponibilites(Authentication auth) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        List<DisponibiliteResponse> list = disponibiliteRepository
                .findByIdMedecinOrderByJourSemaineAscHeureDebutAsc(m.getIdMedecin())
                .stream()
                .map(d -> new DisponibiliteResponse(
                        d.getIdDispo(), d.getJourSemaine(),
                        d.getHeureDebut().format(TIME_FMT),
                        d.getHeureFin().format(TIME_FMT)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/disponibilites")
    public ResponseEntity<DisponibiliteResponse> addDisponibilite(
            Authentication auth, @Valid @RequestBody DisponibiliteRequest req) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        LocalTime debut = LocalTime.parse(req.getHeureDebut(), TIME_FMT);
        LocalTime fin   = LocalTime.parse(req.getHeureFin(),   TIME_FMT);

        if (!fin.isAfter(debut))
            return ResponseEntity.badRequest().build();

        if (disponibiliteRepository.existsByIdMedecinAndJourSemaineAndHeureDebutAndHeureFin(
                m.getIdMedecin(), req.getJourSemaine(), debut, fin)) {
            return ResponseEntity.status(409).build();
        }

        Disponibilite d = new Disponibilite();
        d.setIdMedecin(m.getIdMedecin());
        d.setJourSemaine(req.getJourSemaine());
        d.setHeureDebut(debut);
        d.setHeureFin(fin);
        d = disponibiliteRepository.save(d);

        return ResponseEntity.ok(new DisponibiliteResponse(
                d.getIdDispo(), d.getJourSemaine(),
                d.getHeureDebut().format(TIME_FMT),
                d.getHeureFin().format(TIME_FMT)));
    }

    @GetMapping("/rdv")
    public ResponseEntity<List<RendezVousResponse>> getRdvs(Authentication auth) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        List<RendezVous> rdvs = rendezVousRepository.findByIdMedecinOrderByDateHeureAsc(m.getIdMedecin());

        List<Integer> patientIds = rdvs.stream().map(RendezVous::getIdPatient).distinct().collect(Collectors.toList());
        Map<Integer, Patient> patientsMap = patientRepository.findAllById(patientIds)
                .stream().collect(Collectors.toMap(Patient::getIdPatient, p -> p));

        List<RendezVousResponse> list = rdvs.stream().map(rdv -> {
            Patient p = patientsMap.get(rdv.getIdPatient());
            return new RendezVousResponse(
                    rdv.getIdRdv(),
                    p != null ? p.getNom() : null,
                    p != null ? p.getPrenom() : null,
                    m.getNom(), m.getPrenom(),
                    m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                    rdv.getDateHeure() != null ? rdv.getDateHeure().format(DT_OUT) : null,
                    rdv.getDuree(), rdv.getMotif(), rdv.getStatut(), rdv.getNotes());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    // ── Dossiers patients ────────────────────────────────────────

    @GetMapping("/patients")
    public ResponseEntity<List<Map<String, Object>>> getPatients(Authentication auth) {
        medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        List<Map<String, Object>> result = patientRepository.findAll().stream()
                .map(p -> Map.<String, Object>of(
                        "idPatient", p.getIdPatient(),
                        "nom", p.getNom() != null ? p.getNom() : "",
                        "prenom", p.getPrenom() != null ? p.getPrenom() : "",
                        "telephone", p.getTelephone() != null ? p.getTelephone() : "",
                        "groupeSanguin", p.getGroupeSanguin() != null ? p.getGroupeSanguin() : ""
                )).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/patient/{idPatient}/dossier")
    public ResponseEntity<DossierResponse> getPatientDossier(Authentication auth,
                                                               @PathVariable Integer idPatient) {
        Medecin medecin = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        DossierMedical dossier = dossierMedicalRepository.findByIdPatient(idPatient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter dFmt  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<Integer> medecinIds = noteCliniqueRepository
                .findByIdDossierOrderByDateNoteDesc(dossier.getIdDossier())
                .stream().map(NoteClinique::getIdMedecin).distinct().collect(Collectors.toList());
        medecinIds.addAll(soinRepository.findByIdDossierOrderByDateSoinDesc(dossier.getIdDossier())
                .stream().map(Soin::getIdMedecin).collect(Collectors.toList()));
        medecinIds.addAll(ordonnanceRepository.findByIdDossierOrderByDateEmissionDesc(dossier.getIdDossier())
                .stream().map(Ordonnance::getIdMedecin).collect(Collectors.toList()));
        Map<Integer, Medecin> medMap = medecinRepository.findWithDetailsByIds(
                medecinIds.stream().distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Medecin::getIdMedecin, mm -> mm));
        medMap.put(medecin.getIdMedecin(), medecin);

        List<NoteCliniqueResponse> notes = noteCliniqueRepository
                .findByIdDossierOrderByDateNoteDesc(dossier.getIdDossier()).stream()
                .map(n -> { Medecin mm = medMap.get(n.getIdMedecin());
                    return new NoteCliniqueResponse(n.getIdNote(),
                            mm != null ? mm.getNom() : null, mm != null ? mm.getPrenom() : null,
                            mm != null && mm.getDomaine() != null ? mm.getDomaine().getNomDomaine() : null,
                            n.getDateNote() != null ? n.getDateNote().format(dtFmt) : null,
                            n.getDiagnostic(), n.getPlanTraitement(), n.getObservations());
                }).collect(Collectors.toList());

        List<SoinResponse> soins = soinRepository
                .findByIdDossierOrderByDateSoinDesc(dossier.getIdDossier()).stream()
                .map(s -> { Medecin mm = medMap.get(s.getIdMedecin());
                    return new SoinResponse(s.getIdSoin(),
                            mm != null ? mm.getNom() : null, mm != null ? mm.getPrenom() : null,
                            s.getTypeSoin(), s.getDateSoin() != null ? s.getDateSoin().format(dFmt) : null,
                            s.getCout() != null ? s.getCout().toPlainString() : "0", s.getStatut());
                }).collect(Collectors.toList());

        List<OrdonnanceResponse> ordonnances = ordonnanceRepository
                .findByIdDossierOrderByDateEmissionDesc(dossier.getIdDossier()).stream()
                .map(o -> { Medecin mm = medMap.get(o.getIdMedecin());
                    List<MedicamentResponse> meds = medicamentOrdonnanceRepository
                            .findByIdOrdonnance(o.getIdOrdonnance()).stream()
                            .map(med -> new MedicamentResponse(med.getId(), med.getNomMedicament(),
                                    med.getDosage(), med.getFrequence(), med.getDuree(), med.getInstructions()))
                            .collect(Collectors.toList());
                    return new OrdonnanceResponse(o.getIdOrdonnance(),
                            mm != null ? mm.getNom() : null, mm != null ? mm.getPrenom() : null,
                            o.getDateEmission() != null ? o.getDateEmission().format(dFmt) : null,
                            o.getValiditeJours(), o.getNotes(), meds);
                }).collect(Collectors.toList());

        return ResponseEntity.ok(new DossierResponse(
                dossier.getIdDossier(),
                dossier.getDateCreation() != null ? dossier.getDateCreation().format(dFmt) : null,
                dossier.getAntecedentsMedicaux(), dossier.getAntecedentsDentaires(),
                dossier.getNotesGenerales(), notes, soins, ordonnances));
    }

    @PostMapping("/patient/{idPatient}/notes")
    public ResponseEntity<NoteCliniqueResponse> addNote(Authentication auth,
                                                         @PathVariable Integer idPatient,
                                                         @RequestBody NoteCliniqueRequest req) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        DossierMedical dossier = dossierMedicalRepository.findByIdPatient(idPatient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        NoteClinique note = new NoteClinique();
        note.setIdDossier(dossier.getIdDossier());
        note.setIdMedecin(m.getIdMedecin());
        note.setIdRdv(req.getIdRdv());
        note.setDiagnostic(req.getDiagnostic());
        note.setPlanTraitement(req.getPlanTraitement());
        note.setObservations(req.getObservations());
        note = noteCliniqueRepository.save(note);

        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return ResponseEntity.ok(new NoteCliniqueResponse(note.getIdNote(),
                m.getNom(), m.getPrenom(),
                m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                note.getDateNote() != null ? note.getDateNote().format(dtFmt) : null,
                note.getDiagnostic(), note.getPlanTraitement(), note.getObservations()));
    }

    @PostMapping("/patient/{idPatient}/soins")
    public ResponseEntity<SoinResponse> addSoin(Authentication auth,
                                                  @PathVariable Integer idPatient,
                                                  @RequestBody SoinRequest req) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        DossierMedical dossier = dossierMedicalRepository.findByIdPatient(idPatient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        Soin soin = new Soin();
        soin.setIdDossier(dossier.getIdDossier());
        soin.setIdMedecin(m.getIdMedecin());
        soin.setIdRdv(req.getIdRdv());
        soin.setTypeSoin(req.getTypeSoin());
        soin.setDateSoin(req.getDateSoin() != null
                ? LocalDate.parse(req.getDateSoin(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : LocalDate.now());
        soin.setCout(req.getCout());
        soin = soinRepository.save(soin);

        DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return ResponseEntity.ok(new SoinResponse(soin.getIdSoin(),
                m.getNom(), m.getPrenom(), soin.getTypeSoin(),
                soin.getDateSoin().format(dFmt),
                soin.getCout() != null ? soin.getCout().toPlainString() : "0", soin.getStatut()));
    }

    @PostMapping("/patient/{idPatient}/ordonnances")
    public ResponseEntity<OrdonnanceResponse> addOrdonnance(Authentication auth,
                                                              @PathVariable Integer idPatient,
                                                              @RequestBody OrdonnanceRequest req) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        DossierMedical dossier = dossierMedicalRepository.findByIdPatient(idPatient)
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        Ordonnance ord = new Ordonnance();
        ord.setIdDossier(dossier.getIdDossier());
        ord.setIdMedecin(m.getIdMedecin());
        ord.setIdRdv(req.getIdRdv());
        ord.setDateEmission(LocalDate.now());
        ord.setValiditeJours(req.getValiditeJours() != null ? req.getValiditeJours() : 30);
        ord.setNotes(req.getNotes());
        ord = ordonnanceRepository.save(ord);

        List<MedicamentOrdonnance> meds = new java.util.ArrayList<>();
        if (req.getMedicaments() != null) {
            for (OrdonnanceRequest.MedicamentItem item : req.getMedicaments()) {
                MedicamentOrdonnance med = new MedicamentOrdonnance();
                med.setIdOrdonnance(ord.getIdOrdonnance());
                med.setNomMedicament(item.getNomMedicament());
                med.setDosage(item.getDosage());
                med.setFrequence(item.getFrequence());
                med.setDuree(item.getDuree());
                med.setInstructions(item.getInstructions());
                meds.add(medicamentOrdonnanceRepository.save(med));
            }
        }

        DateTimeFormatter dFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<MedicamentResponse> medResponses = meds.stream()
                .map(med -> new MedicamentResponse(med.getId(), med.getNomMedicament(),
                        med.getDosage(), med.getFrequence(), med.getDuree(), med.getInstructions()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new OrdonnanceResponse(ord.getIdOrdonnance(),
                m.getNom(), m.getPrenom(), ord.getDateEmission().format(dFmt),
                ord.getValiditeJours(), ord.getNotes(), medResponses));
    }

    @DeleteMapping("/disponibilites/{id}")
    public ResponseEntity<Void> deleteDisponibilite(Authentication auth, @PathVariable Integer id) {
        Medecin m = medecinRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        Disponibilite d = disponibiliteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilité introuvable"));
        if (!d.getIdMedecin().equals(m.getIdMedecin()))
            return ResponseEntity.status(403).build();
        disponibiliteRepository.delete(d);
        return ResponseEntity.noContent().build();
    }
}
