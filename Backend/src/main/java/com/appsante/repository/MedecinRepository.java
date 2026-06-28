package com.appsante.repository;

import com.appsante.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
    List<Medecin> findByEtablissement_IdEtablissement(Integer idEtablissement);
    List<Medecin> findByDomaine_IdDomaine(Integer idDomaine);
    boolean existsByNumeroOrdre(String numeroOrdre);
    Optional<Medecin> findByUtilisateur_Email(String email);

    @Query("SELECT m FROM Medecin m LEFT JOIN FETCH m.domaine LEFT JOIN FETCH m.etablissement WHERE m.utilisateur.email = :email")
    Optional<Medecin> findWithDetailsByEmail(@Param("email") String email);

    @Query("SELECT m FROM Medecin m LEFT JOIN FETCH m.domaine LEFT JOIN FETCH m.etablissement WHERE m.idMedecin = :id")
    Optional<Medecin> findWithDetailsById(@Param("id") Integer id);

    @Query("SELECT m FROM Medecin m LEFT JOIN FETCH m.domaine LEFT JOIN FETCH m.etablissement WHERE m.etablissement.idEtablissement = :idEtab")
    List<Medecin> findWithDetailsByEtablissement(@Param("idEtab") Integer idEtab);

    @Query("SELECT m FROM Medecin m LEFT JOIN FETCH m.domaine LEFT JOIN FETCH m.etablissement WHERE m.idMedecin IN :ids")
    List<Medecin> findWithDetailsByIds(@Param("ids") List<Integer> ids);
}
