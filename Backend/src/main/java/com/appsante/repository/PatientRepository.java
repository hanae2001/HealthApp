package com.appsante.repository;

import com.appsante.entity.Patient;
import com.appsante.entity.Utilisateur;
//Interface PARENT qui donne toutes les méthodes CRUD/ findById(Integer),delete
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    //Recherche par objet (propre)
    Optional<Patient> findByUtilisateur(Utilisateur utilisateur);
    //Recherche directe par email (pratique)
    Optional<Patient> findByUtilisateurEmail(String email);
}