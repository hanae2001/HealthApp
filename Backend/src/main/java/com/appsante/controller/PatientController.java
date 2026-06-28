package com.appsante.controller;

import com.appsante.dto.request.RendezVousRequest;
import com.appsante.dto.response.*;
import com.appsante.entity.*;
import com.appsante.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')")
public class PatientController {

    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final RendezVousRepository rendezVousRepository;
    private final DossierMedicalRepository dossierMedicalRepository;
    private final NoteCliniqueRepository noteCliniqueRepository;
    private final SoinRepository soinRepository;
    private final OrdonnanceRepository ordonnanceRepository;
    private final MedicamentOrdonnanceRepository medicamentOrdonnanceRepository;

    private static final DateTimeFormatter DT_IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter DT_OUT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @PostMapping("/rdv")
    public ResponseEntity<?> prendreRdv(Authentication auth,
                                         @Valid @RequestBody RendezVousRequest req) {
        Patient patient = patientRepository.findByUtilisateurEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Patient introuvable"));

        Medecin medecin = medecinRepository.findWithDetailsById(req.getIdMedecin())
                .orElseThrow(() -> new RuntimeException("Médecin introuvable"));

        LocalDateTime dateHeure = LocalDateTime.parse(req.getDateHeure(), DT_IN);

        if (rendezVousRepository.existsByIdMedecinAndDateHeure(req.getIdMedecin(), dateHeure)) {
            return ResponseEntity.status(409).body("Ce créneau est déjà pris");
        }

        RendezVous rdv = new RendezVous();
        rdv.setIdPatient(patient.getIdPatient());
        rdv.setIdMedecin(req.getIdMedecin());
        rdv.setDateHeure(dateHeure);
        rdv.setMotif(req.getMotif());
        rdv.setStatut("planifie");
        rdv = rendezVousRepository.save(rdv);

        return ResponseEntity.ok(toResponse(rdv, patient, medecin));
    }

    @GetMapping("/rdv")
    public ResponseEntity<List<RendezVousResponse>> mesRdv(Authentication auth) {
        Patient patient = patientRepository.findByUtilisateurEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Patient introuvable"));

        List<RendezVous> rdvs = rendezVousRepository
                .findByIdPatientOrderByDateHeureDesc(patient.getIdPatient());

        List<Integer> medecinIds = rdvs.stream().map(RendezVous::getIdMedecin).distinct().collect(Collectors.toList());
        Map<Integer, Medecin> medecins = medecinRepository.findWithDetailsByIds(medecinIds)
                .stream().collect(Collectors.toMap(Medecin::getIdMedecin, m -> m));

        List<RendezVousResponse> list = rdvs.stream()
                .map(rdv -> toResponse(rdv, patient, medecins.get(rdv.getIdMedecin())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/dossier")
    public ResponseEntity<DossierResponse> getDossier(Authentication auth) {
        Patient patient = patientRepository.findByUtilisateurEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Patient introuvable"));

        DossierMedical dossier = dossierMedicalRepository.findByIdPatient(patient.getIdPatient())
                .orElseThrow(() -> new RuntimeException("Dossier introuvable"));

        List<Integer> medecinIds = noteCliniqueRepository
                .findByIdDossierOrderByDateNoteDesc(dossier.getIdDossier())
                .stream().map(NoteClinique::getIdMedecin).distinct().collect(Collectors.toList());
        medecinIds.addAll(soinRepository.findByIdDossierOrderByDateSoinDesc(dossier.getIdDossier())
                .stream().map(Soin::getIdMedecin).distinct().collect(Collectors.toList()));
        medecinIds.addAll(ordonnanceRepository.findByIdDossierOrderByDateEmissionDesc(dossier.getIdDossier())
                .stream().map(Ordonnance::getIdMedecin).distinct().collect(Collectors.toList()));
        Map<Integer, Medecin> medMap = medecinRepository.findWithDetailsByIds(
                medecinIds.stream().distinct().collect(Collectors.toList()))
                .stream().collect(Collectors.toMap(Medecin::getIdMedecin, m -> m));

        DateTimeFormatter dtFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DateTimeFormatter dFmt  = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<NoteCliniqueResponse> notes = noteCliniqueRepository
                .findByIdDossierOrderByDateNoteDesc(dossier.getIdDossier()).stream()
                .map(n -> { Medecin m = medMap.get(n.getIdMedecin());
                    return new NoteCliniqueResponse(n.getIdNote(),
                            m != null ? m.getNom() : null, m != null ? m.getPrenom() : null,
                            m != null && m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                            n.getDateNote() != null ? n.getDateNote().format(dtFmt) : null,
                            n.getDiagnostic(), n.getPlanTraitement(), n.getObservations());
                }).collect(Collectors.toList());

        List<SoinResponse> soins = soinRepository
                .findByIdDossierOrderByDateSoinDesc(dossier.getIdDossier()).stream()
                .map(s -> { Medecin m = medMap.get(s.getIdMedecin());
                    return new SoinResponse(s.getIdSoin(),
                            m != null ? m.getNom() : null, m != null ? m.getPrenom() : null,
                            s.getTypeSoin(), s.getDateSoin() != null ? s.getDateSoin().format(dFmt) : null,
                            s.getCout() != null ? s.getCout().toPlainString() : "0", s.getStatut());
                }).collect(Collectors.toList());

        List<OrdonnanceResponse> ordonnances = ordonnanceRepository
                .findByIdDossierOrderByDateEmissionDesc(dossier.getIdDossier()).stream()
                .map(o -> { Medecin m = medMap.get(o.getIdMedecin());
                    List<MedicamentResponse> meds = medicamentOrdonnanceRepository
                            .findByIdOrdonnance(o.getIdOrdonnance()).stream()
                            .map(med -> new MedicamentResponse(med.getId(), med.getNomMedicament(),
                                    med.getDosage(), med.getFrequence(), med.getDuree(), med.getInstructions()))
                            .collect(Collectors.toList());
                    return new OrdonnanceResponse(o.getIdOrdonnance(),
                            m != null ? m.getNom() : null, m != null ? m.getPrenom() : null,
                            o.getDateEmission() != null ? o.getDateEmission().format(dFmt) : null,
                            o.getValiditeJours(), o.getNotes(), meds);
                }).collect(Collectors.toList());

        return ResponseEntity.ok(new DossierResponse(
                dossier.getIdDossier(),
                dossier.getDateCreation() != null ? dossier.getDateCreation().format(dFmt) : null,
                dossier.getAntecedentsMedicaux(), dossier.getAntecedentsDentaires(),
                dossier.getNotesGenerales(), notes, soins, ordonnances));
    }

    private RendezVousResponse toResponse(RendezVous rdv, Patient p, Medecin m) {
        return new RendezVousResponse(
                rdv.getIdRdv(),
                p != null ? p.getNom() : null,
                p != null ? p.getPrenom() : null,
                m != null ? m.getNom() : null,
                m != null ? m.getPrenom() : null,
                m != null && m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                rdv.getDateHeure() != null ? rdv.getDateHeure().format(DT_OUT) : null,
                rdv.getDuree(),
                rdv.getMotif(),
                rdv.getStatut(),
                rdv.getNotes());
    }
}
