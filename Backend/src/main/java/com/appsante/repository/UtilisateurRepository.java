// REPOSITORY parle à la BDD, ENTITY est la FORME des données 
package com.appsante.repository;

import com.appsante.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {

    Optional<Utilisateur> findByEmail(String email);

    boolean existsByEmail(String email);
}