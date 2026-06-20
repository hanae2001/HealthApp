package com.appsante.service;

import com.appsante.dto.request.CreateEtablissementRequest;
import com.appsante.dto.request.CreateMedecinRequest;
import com.appsante.dto.request.CreateReceptionnisteRequest;
import com.appsante.dto.response.*;
import com.appsante.entity.*;
import com.appsante.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final MedecinRepository medecinRepository;
    private final ReceptionnisteRepository receptionnisteRepository;
    private final EtablissementRepository etablissementRepository;
    private final DomaineMedicalRepository domaineRepository;
    private final VilleRepository villeRepository;
    private final SecteurRepository secteurRepository;
    private final DisponibiliteRepository disponibiliteRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MedecinResponse creerMedecin(CreateMedecinRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }
        if (medecinRepository.existsByNumeroOrdre(req.getNumeroOrdre())) {
            throw new RuntimeException("Ce numéro d'ordre est déjà utilisé");
        }

        Etablissement etablissement = etablissementRepository.findById(req.getIdEtablissement())
                .orElseThrow(() -> new RuntimeException("Établissement introuvable"));

        DomaineMedical domaine = domaineRepository.findById(req.getIdDomaine())
                .orElseThrow(() -> new RuntimeException("Domaine médical introuvable"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(req.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
        utilisateur.setRole(Utilisateur.Role.medecin);
        utilisateur = utilisateurRepository.save(utilisateur);

        Medecin medecin = new Medecin();
        medecin.setUtilisateur(utilisateur);
        medecin.setEtablissement(etablissement);
        medecin.setDomaine(domaine);
        medecin.setNom(req.getNom());
        medecin.setPrenom(req.getPrenom());
        medecin.setEmail(req.getEmail());
        medecin.setTelephone(req.getTelephone());
        medecin.setNumeroOrdre(req.getNumeroOrdre());
        medecin.setExperienceAns(req.getExperienceAns());
        medecin.setTarifConsultation(req.getTarifConsultation());
        medecin = medecinRepository.save(medecin);

        Secteur s = secteurRepository.findById(etablissement.getIdSecteur()).orElse(null);
        Ville v   = s != null ? villeRepository.findById(s.getIdVille()).orElse(null) : null;
        Map<Integer, Secteur> sm = s != null ? Map.of(s.getIdSecteur(), s) : Map.of();
        Map<Integer, Ville>   vm = v != null ? Map.of(v.getIdVille(), v)   : Map.of();
        return toMedecinResponse(medecin, sm, vm);
    }

    @Transactional
    public ReceptionnisteResponse creerReceptionniste(CreateReceptionnisteRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        Etablissement etablissement = etablissementRepository.findById(req.getIdEtablissement())
                .orElseThrow(() -> new RuntimeException("Établissement introuvable"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(req.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
        utilisateur.setRole(Utilisateur.Role.receptionniste);
        utilisateur = utilisateurRepository.save(utilisateur);

        Receptionniste receptionniste = new Receptionniste();
        receptionniste.setUtilisateur(utilisateur);
        receptionniste.setEtablissement(etablissement);
        receptionniste.setNom(req.getNom());
        receptionniste.setPrenom(req.getPrenom());
        receptionniste.setTelephone(req.getTelephone());
        receptionniste = receptionnisteRepository.save(receptionniste);

        Secteur s = secteurRepository.findById(etablissement.getIdSecteur()).orElse(null);
        Ville v   = s != null ? villeRepository.findById(s.getIdVille()).orElse(null) : null;
        Map<Integer, Secteur> sm = s != null ? Map.of(s.getIdSecteur(), s) : Map.of();
        Map<Integer, Ville>   vm = v != null ? Map.of(v.getIdVille(), v)   : Map.of();
        return toReceptionnisteResponse(receptionniste, sm, vm);
    }

    @Transactional(readOnly = true)
    public List<MedecinResponse> listerMedecins() {
        Map<Integer, Secteur> secteurs = secteurRepository.findAll().stream()
                .collect(Collectors.toMap(Secteur::getIdSecteur, s -> s));
        Map<Integer, Ville> villes = villeRepository.findAll().stream()
                .collect(Collectors.toMap(Ville::getIdVille, v -> v));
        return medecinRepository.findAll().stream()
                .map(m -> toMedecinResponse(m, secteurs, villes))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReceptionnisteResponse> listerReceptionnistes() {
        Map<Integer, Secteur> secteurs = secteurRepository.findAll().stream()
                .collect(Collectors.toMap(Secteur::getIdSecteur, s -> s));
        Map<Integer, Ville> villes = villeRepository.findAll().stream()
                .collect(Collectors.toMap(Ville::getIdVille, v -> v));
        return receptionnisteRepository.findAll().stream()
                .map(r -> toReceptionnisteResponse(r, secteurs, villes))
                .collect(Collectors.toList());
    }

    public List<VilleResponse> listerVilles() {
        return villeRepository.findAllByOrderByNomVilleAsc().stream()
                .map(v -> new VilleResponse(v.getIdVille(), v.getNomVille()))
                .collect(Collectors.toList());
    }

    public List<SecteurResponse> listerSecteurs(Integer idVille) {
        return secteurRepository.findByIdVilleOrderByNomSecteurAsc(idVille).stream()
                .map(s -> new SecteurResponse(s.getIdSecteur(), s.getNomSecteur()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EtablissementResponse> listerEtablissements() {
        Map<Integer, Secteur> secteurs = secteurRepository.findAll().stream()
                .collect(Collectors.toMap(Secteur::getIdSecteur, s -> s));
        Map<Integer, Ville> villes = villeRepository.findAll().stream()
                .collect(Collectors.toMap(Ville::getIdVille, v -> v));
        return etablissementRepository.findAll().stream()
                .map(e -> toEtablissementResponse(e, secteurs, villes))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EtablissementResponse> listerEtablissementsParSecteur(Integer idSecteur) {
        Secteur secteur = secteurRepository.findById(idSecteur)
                .orElseThrow(() -> new RuntimeException("Secteur introuvable"));
        Ville ville = villeRepository.findById(secteur.getIdVille())
                .orElseThrow(() -> new RuntimeException("Ville introuvable"));
        Map<Integer, Secteur> secteurs = Map.of(secteur.getIdSecteur(), secteur);
        Map<Integer, Ville> villes = Map.of(ville.getIdVille(), ville);
        return etablissementRepository.findByIdSecteur(idSecteur).stream()
                .map(e -> toEtablissementResponse(e, secteurs, villes))
                .collect(Collectors.toList());
    }

    private EtablissementResponse toEtablissementResponse(Etablissement e,
            Map<Integer, Secteur> secteurs, Map<Integer, Ville> villes) {
        Secteur s = secteurs.get(e.getIdSecteur());
        Ville v   = s != null ? villes.get(s.getIdVille()) : null;
        return new EtablissementResponse(
                e.getIdEtablissement(), e.getNomEtablissement(),
                e.getAdresse(), e.getTelephone(),
                s != null ? s.getNomSecteur() : null,
                v != null ? v.getNomVille()    : null);
    }

    @Transactional
    public void supprimerMedecin(Integer idMedecin) {
        Medecin medecin = medecinRepository.findById(idMedecin)
                .orElseThrow(() -> new RuntimeException("Médecin introuvable"));
        Utilisateur utilisateur = medecin.getUtilisateur();
        disponibiliteRepository.deleteByIdMedecin(idMedecin);
        medecinRepository.delete(medecin);
        utilisateurRepository.delete(utilisateur);
    }

    @Transactional
    public void supprimerReceptionniste(Integer idReceptionniste) {
        Receptionniste r = receptionnisteRepository.findById(idReceptionniste)
                .orElseThrow(() -> new RuntimeException("Réceptionniste introuvable"));
        Utilisateur utilisateur = r.getUtilisateur();
        receptionnisteRepository.delete(r);
        utilisateurRepository.delete(utilisateur);
    }

    @Transactional
    public EtablissementResponse creerEtablissement(CreateEtablissementRequest req) {
        Secteur secteur = secteurRepository.findById(req.getIdSecteur())
                .orElseThrow(() -> new RuntimeException("Secteur introuvable"));
        Ville ville = villeRepository.findById(secteur.getIdVille())
                .orElseThrow(() -> new RuntimeException("Ville introuvable"));

        Etablissement e = new Etablissement();
        e.setNomEtablissement(req.getNomEtablissement());
        e.setIdSecteur(req.getIdSecteur());
        e.setIdType(req.getIdType() != null ? req.getIdType() : 1);
        e.setAdresse(req.getAdresse());
        e.setTelephone(req.getTelephone());
        e.setActif(true);
        e = etablissementRepository.save(e);

        return new EtablissementResponse(
                e.getIdEtablissement(), e.getNomEtablissement(),
                e.getAdresse(), e.getTelephone(),
                secteur.getNomSecteur(), ville.getNomVille());
    }

    public List<DomaineResponse> listerDomaines() {
        return domaineRepository.findAll().stream()
                .map(d -> new DomaineResponse(d.getIdDomaine(), d.getNomDomaine(), d.getIcone()))
                .collect(Collectors.toList());
    }

    private MedecinResponse toMedecinResponse(Medecin m, Map<Integer, Secteur> secteurs, Map<Integer, Ville> villes) {
        Etablissement etab = m.getEtablissement();
        Secteur s = (etab != null) ? secteurs.get(etab.getIdSecteur()) : null;
        Ville v   = (s    != null) ? villes.get(s.getIdVille())        : null;
        return new MedecinResponse(
                m.getIdMedecin(),
                m.getNom(),
                m.getPrenom(),
                m.getEmail(),
                m.getTelephone(),
                m.getNumeroOrdre(),
                etab != null ? etab.getNomEtablissement() : null,
                m.getDomaine().getNomDomaine(),
                s != null ? s.getNomSecteur() : null,
                v != null ? v.getNomVille()   : null,
                m.getTarifConsultation(),
                m.getActif());
    }

    private ReceptionnisteResponse toReceptionnisteResponse(Receptionniste r, Map<Integer, Secteur> secteurs, Map<Integer, Ville> villes) {
        Etablissement etab = r.getEtablissement();
        Secteur s = (etab != null) ? secteurs.get(etab.getIdSecteur()) : null;
        Ville v   = (s    != null) ? villes.get(s.getIdVille())        : null;
        return new ReceptionnisteResponse(
                r.getIdReceptionniste(),
                r.getNom(),
                r.getPrenom(),
                r.getUtilisateur().getEmail(),
                r.getTelephone(),
                etab != null ? etab.getNomEtablissement() : null,
                s != null ? s.getNomSecteur() : null,
                v != null ? v.getNomVille()   : null);
    }
}
