package com.appsante.service;

import com.appsante.dto.request.LoginRequest;
import com.appsante.dto.request.RegisterRequest;
import com.appsante.dto.response.AuthResponse;
import com.appsante.entity.Patient;
import com.appsante.entity.Utilisateur;
import com.appsante.repository.PatientRepository;
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
                utilisateur.getRole().name(), token);
    }

    public AuthResponse connecter(LoginRequest req) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou mot de passe incorrect"));

        if (!passwordEncoder.matches(req.getMotDePasse(), utilisateur.getMotDePasse())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }

        if (!utilisateur.getActif()) {
            throw new RuntimeException("Ce compte est désactivé");
        }

        String token = jwtUtil.generateToken(utilisateur.getEmail(), utilisateur.getRole().name());
        return new AuthResponse(utilisateur.getIdUtilisateur(), utilisateur.getEmail(),
                utilisateur.getRole().name(), token);
    }
}
