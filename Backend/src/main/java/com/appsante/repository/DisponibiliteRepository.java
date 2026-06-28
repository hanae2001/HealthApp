package com.appsante.repository;

import com.appsante.entity.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Integer> {

    List<Disponibilite> findByIdMedecinOrderByJourSemaineAscHeureDebutAsc(Integer idMedecin);

    boolean existsByIdMedecinAndJourSemaineAndHeureDebutAndHeureFin(
            Integer idMedecin, String jourSemaine,
            java.time.LocalTime heureDebut, java.time.LocalTime heureFin);

    @Transactional
    void deleteByIdMedecin(Integer idMedecin);
}
