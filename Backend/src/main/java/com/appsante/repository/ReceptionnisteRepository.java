package com.appsante.repository;

import com.appsante.entity.Receptionniste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReceptionnisteRepository extends JpaRepository<Receptionniste, Integer> {
    List<Receptionniste> findByEtablissement_IdEtablissement(Integer idEtablissement);
    Optional<Receptionniste> findByUtilisateur_Email(String email);
}
