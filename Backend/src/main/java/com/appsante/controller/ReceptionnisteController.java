package com.appsante.controller;

import com.appsante.dto.request.UpdateRdvRequest;
import com.appsante.dto.response.MedecinResponse;
import com.appsante.dto.response.RendezVousResponse;
import com.appsante.dto.response.ReceptionnisteResponse;
import com.appsante.entity.*;
import com.appsante.repository.*;
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
@RequestMapping("/api/receptionniste")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECEPTIONNISTE')")
public class ReceptionnisteController {

    private final ReceptionnisteRepository receptionnisteRepository;
    private final MedecinRepository medecinRepository;
    private final SecteurRepository secteurRepository;
    private final VilleRepository villeRepository;
    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;

    private static final DateTimeFormatter DT_OUT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DT_IN  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @GetMapping("/profil")
    public ResponseEntity<ReceptionnisteResponse> getProfil(Authentication auth) {
        Receptionniste r = receptionnisteRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        return ResponseEntity.ok(buildResponse(r));
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<MedecinResponse>> getMedecins(Authentication auth) {
        Receptionniste r = receptionnisteRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));

        Etablissement etab = r.getEtablissement();
        if (etab == null) return ResponseEntity.ok(List.of());

        Secteur s = secteurRepository.findById(etab.getIdSecteur()).orElse(null);
        Ville v   = s != null ? villeRepository.findById(s.getIdVille()).orElse(null) : null;
        String nomSecteur = s != null ? s.getNomSecteur() : null;
        String nomVille   = v != null ? v.getNomVille()   : null;

        List<MedecinResponse> medecins = medecinRepository.findWithDetailsByEtablissement(etab.getIdEtablissement()).stream()
                .map(m -> new MedecinResponse(
                        m.getIdMedecin(), m.getNom(), m.getPrenom(), m.getEmail(), m.getTelephone(),
                        m.getNumeroOrdre(), etab.getNomEtablissement(),
                        m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                        nomSecteur, nomVille, m.getTarifConsultation(), m.getActif()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(medecins);
    }

    @GetMapping("/rdv")
    public ResponseEntity<List<RendezVousResponse>> getRdvs(Authentication auth) {
        Receptionniste r = receptionnisteRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        Etablissement etab = r.getEtablissement();
        if (etab == null) return ResponseEntity.ok(List.of());

        List<Integer> medecinIds = medecinRepository
                .findWithDetailsByEtablissement(etab.getIdEtablissement())
                .stream().map(Medecin::getIdMedecin).collect(Collectors.toList());
        if (medecinIds.isEmpty()) return ResponseEntity.ok(List.of());

        List<RendezVous> rdvs = rendezVousRepository.findByIdMedecinInOrderByDateHeureAsc(medecinIds);
        Map<Integer, Medecin> medecinsMap = medecinRepository.findWithDetailsByIds(medecinIds)
                .stream().collect(Collectors.toMap(Medecin::getIdMedecin, m -> m));

        List<Integer> patientIds = rdvs.stream().map(RendezVous::getIdPatient).distinct().collect(Collectors.toList());
        Map<Integer, Patient> patientsMap = patientRepository.findAllById(patientIds)
                .stream().collect(Collectors.toMap(Patient::getIdPatient, p -> p));

        return ResponseEntity.ok(rdvs.stream().map(rdv -> toResponse(rdv,
                patientsMap.get(rdv.getIdPatient()),
                medecinsMap.get(rdv.getIdMedecin()))).collect(Collectors.toList()));
    }

    @PatchMapping("/rdv/{id}")
    public ResponseEntity<?> updateRdv(Authentication auth,
                                        @PathVariable Integer id,
                                        @RequestBody UpdateRdvRequest req) {
        Receptionniste rec = receptionnisteRepository.findWithDetailsByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Profil introuvable"));
        Etablissement etab = rec.getEtablissement();

        RendezVous rdv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RDV introuvable"));

        List<Integer> medecinIds = medecinRepository
                .findWithDetailsByEtablissement(etab.getIdEtablissement())
                .stream().map(Medecin::getIdMedecin).collect(Collectors.toList());
        if (!medecinIds.contains(rdv.getIdMedecin())) {
            return ResponseEntity.status(403).body("Accès refusé");
        }

        if (req.getStatut() != null)    rdv.setStatut(req.getStatut());
        if (req.getNotes() != null)     rdv.setNotes(req.getNotes());
        if (req.getDateHeure() != null) rdv.setDateHeure(LocalDateTime.parse(req.getDateHeure(), DT_IN));

        rendezVousRepository.save(rdv);

        Patient p = patientRepository.findById(rdv.getIdPatient()).orElse(null);
        Medecin m = medecinRepository.findWithDetailsById(rdv.getIdMedecin()).orElse(null);
        return ResponseEntity.ok(toResponse(rdv, p, m));
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

    private ReceptionnisteResponse buildResponse(Receptionniste r) {
        Etablissement etab = r.getEtablissement();
        Secteur s = etab != null ? secteurRepository.findById(etab.getIdSecteur()).orElse(null) : null;
        Ville v   = s != null ? villeRepository.findById(s.getIdVille()).orElse(null) : null;
        return new ReceptionnisteResponse(
                r.getIdReceptionniste(), r.getNom(), r.getPrenom(),
                r.getUtilisateur() != null ? r.getUtilisateur().getEmail() : null,
                r.getTelephone(),
                etab != null ? etab.getNomEtablissement() : null,
                s != null ? s.getNomSecteur() : null,
                v != null ? v.getNomVille() : null);
    }
}
