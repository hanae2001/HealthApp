package com.appsante.controller;

import com.appsante.dto.response.DisponibiliteResponse;
import com.appsante.dto.response.DomaineResponse;
import com.appsante.dto.response.EtablissementResponse;
import com.appsante.dto.response.PublicMedecinResponse;
import com.appsante.dto.response.SecteurResponse;
import com.appsante.dto.response.VilleResponse;
import com.appsante.entity.Etablissement;
import com.appsante.entity.Secteur;
import com.appsante.entity.Ville;
import com.appsante.repository.DisponibiliteRepository;
import com.appsante.repository.DomaineMedicalRepository;
import com.appsante.repository.EtablissementRepository;
import com.appsante.repository.MedecinRepository;
import com.appsante.repository.SecteurRepository;
import com.appsante.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {

    private final DomaineMedicalRepository domaineRepository;
    private final VilleRepository villeRepository;
    private final SecteurRepository secteurRepository;
    private final EtablissementRepository etablissementRepository;
    private final MedecinRepository medecinRepository;
    private final DisponibiliteRepository disponibiliteRepository;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/domaines")
    public ResponseEntity<List<DomaineResponse>> listerDomaines() {
        List<DomaineResponse> result = domaineRepository.findAll().stream()
                .map(d -> new DomaineResponse(d.getIdDomaine(), d.getNomDomaine(), d.getIcone()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/villes")
    public ResponseEntity<List<VilleResponse>> listerVilles() {
        List<VilleResponse> result = villeRepository.findAllByOrderByNomVilleAsc().stream()
                .map(v -> new VilleResponse(v.getIdVille(), v.getNomVille()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/secteurs")
    public ResponseEntity<List<SecteurResponse>> listerSecteurs(@RequestParam Integer idVille) {
        List<SecteurResponse> result = secteurRepository.findByIdVilleOrderByNomSecteurAsc(idVille).stream()
                .map(s -> new SecteurResponse(s.getIdSecteur(), s.getNomSecteur()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/etablissements")
    @Transactional(readOnly = true)
    public ResponseEntity<List<EtablissementResponse>> listerEtablissements(
            @RequestParam(required = false) Integer idSecteur) {
        Map<Integer, Secteur> secteurs = secteurRepository.findAll().stream()
                .collect(Collectors.toMap(Secteur::getIdSecteur, s -> s));
        Map<Integer, Ville> villes = villeRepository.findAll().stream()
                .collect(Collectors.toMap(Ville::getIdVille, v -> v));

        List<Etablissement> list = idSecteur != null
                ? etablissementRepository.findByIdSecteur(idSecteur)
                : etablissementRepository.findAll();

        List<EtablissementResponse> result = list.stream()
                .map(e -> {
                    Secteur s = secteurs.get(e.getIdSecteur());
                    Ville v   = s != null ? villes.get(s.getIdVille()) : null;
                    return new EtablissementResponse(
                            e.getIdEtablissement(), e.getNomEtablissement(),
                            e.getAdresse(), e.getTelephone(),
                            s != null ? s.getNomSecteur() : null,
                            v != null ? v.getNomVille()    : null);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/medecins")
    public ResponseEntity<List<PublicMedecinResponse>> listerMedecins(
            @RequestParam Integer idEtablissement) {
        List<PublicMedecinResponse> result = medecinRepository
                .findWithDetailsByEtablissement(idEtablissement)
                .stream()
                .filter(m -> !Boolean.FALSE.equals(m.getActif()))
                .map(m -> new PublicMedecinResponse(
                        m.getIdMedecin(), m.getNom(), m.getPrenom(),
                        m.getDomaine() != null ? m.getDomaine().getNomDomaine() : null,
                        m.getExperienceAns(), m.getNoteMoyenne(), m.getNbAvis(),
                        m.getTarifConsultation()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/disponibilites")
    public ResponseEntity<List<DisponibiliteResponse>> getDisponibilites(
            @RequestParam Integer idMedecin) {
        List<DisponibiliteResponse> result = disponibiliteRepository
                .findByIdMedecinOrderByJourSemaineAscHeureDebutAsc(idMedecin)
                .stream()
                .map(d -> new DisponibiliteResponse(
                        d.getIdDispo(), d.getJourSemaine(),
                        d.getHeureDebut().format(TIME_FMT),
                        d.getHeureFin().format(TIME_FMT)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}
