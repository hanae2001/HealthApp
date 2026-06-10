package com.appsante.service;

import com.appsante.dto.request.RegisterRequest;
import com.appsante.dto.response.RegisterResponse;
import com.appsante.entity.Patient;
import com.appsante.entity.Utilisateur;
import com.appsante.repository.PatientRepository;
import com.appsante.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
//"Cette classe est un service (logique métier)"   
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegisterResponse inscrirePatient(RegisterRequest req) {

        // ① Vérifier que l'email n'est pas déjà utilisé
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        // ② Créer l'utilisateur avec le mot de passe encodé en BCrypt
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(req.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
        utilisateur.setRole(Utilisateur.Role.patient);
        utilisateur = utilisateurRepository.save(utilisateur);
        // → INSERT INTO utilisateur (email, mot_de_passe, role, actif)

        // ③ Créer le profil patient lié à cet utilisateur
        Patient patient = new Patient();
        patient.setUtilisateur(utilisateur);
        patient.setNom(req.getNom());
        patient.setPrenom(req.getPrenom());
        patient.setEmail(req.getEmail());
        patient.setTelephone(req.getTelephone());
        patient.setAdresse(req.getAdresse());
        patientRepository.save(patient);
        // → INSERT INTO patient (id_utilisateur, nom, prenom, email, telephone, adresse)
        // → Le trigger MySQL after_patient_insert crée automatiquement le dossier_medical

        return new RegisterResponse(
            utilisateur.getIdUtilisateur(),
            utilisateur.getEmail(),
            utilisateur.getRole().name()
        );
    }
}
