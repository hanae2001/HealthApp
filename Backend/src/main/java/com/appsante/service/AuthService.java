package com.appsante.service;

import com.appsante.dto.request.LoginRequest;
import com.appsante.dto.request.RegisterRequest;
import com.appsante.dto.response.AuthResponse;
import com.appsante.entity.Medecin;
import com.appsante.entity.Patient;
import com.appsante.entity.Receptionniste;
import com.appsante.entity.Utilisateur;
import com.appsante.repository.MedecinRepository;
import com.appsante.repository.PatientRepository;
import com.appsante.repository.ReceptionnisteRepository;
import com.appsante.repository.UtilisateurRepository;
import com.appsante.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PatientRepository patientRepository;
    private final MedecinRepository medecinRepository;
    private final ReceptionnisteRepository receptionnisteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse inscrirePatient(RegisterRequest req) {
        if (utilisateurRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Un compte avec cet email existe déjà");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(req.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
        utilisateur.setRole(Utilisateur.Role.patient);
        utilisateur = utilisateurRepository.save(utilisateur);

        Patient patient = new Patient();
        patient.setUtilisateur(utilisateur);
        patient.setNom(req.getNom());
        patient.setPrenom(req.getPrenom());
        patient.setEmail(req.getEmail());
        patient.setTelephone(req.getTelephone());
        patient.setAdresse(req.getAdresse());
        patientRepository.save(patient);

        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());
        return new AuthResponse(utilisateur.getIdUtilisateur(), utilisateur.getEmail(),
                utilisateur.getRole().name(), token, req.getNom(), req.getPrenom());
    }

    @Transactional
    public AuthResponse connecter(LoginRequest req) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(req.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        if (!utilisateur.getActif()) {
            throw new RuntimeException("Ce compte est désactivé");
        }

        // Migration transparente : si le hash utilise encore le coût 10, ré-encoder avec coût 8
        String hash = utilisateur.getMotDePasse();
        if (hash.startsWith("$2a$10$") || hash.startsWith("$2b$10$")) {
            utilisateur.setMotDePasse(passwordEncoder.encode(req.getMotDePasse()));
            utilisateurRepository.save(utilisateur);
        }

        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());
        String[] nomPrenom = resolveNomPrenom(utilisateur);
        return new AuthResponse(utilisateur.getIdUtilisateur(), utilisateur.getEmail(),
                utilisateur.getRole().name(), token, nomPrenom[0], nomPrenom[1]);
    }

    private String[] resolveNomPrenom(Utilisateur u) {
        return switch (u.getRole()) {
            case medecin -> medecinRepository.findByUtilisateur_Email(u.getEmail())
                    .map(m -> new String[]{m.getNom(), m.getPrenom()})
                    .orElse(new String[]{null, null});
            case receptionniste -> receptionnisteRepository.findByUtilisateur_Email(u.getEmail())
                    .map(r -> new String[]{r.getNom(), r.getPrenom()})
                    .orElse(new String[]{null, null});
            case patient -> patientRepository.findByUtilisateurEmail(u.getEmail())
                    .map(p -> new String[]{p.getNom(), p.getPrenom()})
                    .orElse(new String[]{null, null});
            default -> new String[]{null, null};
        };
    }
}
