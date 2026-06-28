package com.appsante.repository;

import com.appsante.entity.Receptionniste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReceptionnisteRepository extends JpaRepository<Receptionniste, Integer> {
    List<Receptionniste> findByEtablissement_IdEtablissement(Integer idEtablissement);
    Optional<Receptionniste> findByUtilisateur_Email(String email);

    @Query("SELECT r FROM Receptionniste r LEFT JOIN FETCH r.etablissement LEFT JOIN FETCH r.utilisateur WHERE r.utilisateur.email = :email")
    Optional<Receptionniste> findWithDetailsByEmail(@Param("email") String email);
}
