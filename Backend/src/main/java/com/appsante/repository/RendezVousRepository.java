package com.appsante.repository;

import com.appsante.entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {

    List<RendezVous> findByIdPatientOrderByDateHeureDesc(Integer idPatient);

    List<RendezVous> findByIdMedecinOrderByDateHeureAsc(Integer idMedecin);

    List<RendezVous> findByIdMedecinInOrderByDateHeureAsc(List<Integer> idMedecins);

    boolean existsByIdMedecinAndDateHeure(Integer idMedecin, LocalDateTime dateHeure);

    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.idMedecin = :idMedecin AND r.statut = 'planifie' AND r.dateHeure >= :from AND r.dateHeure < :to")
    long countPlanifiesByMedecinAndPeriod(@Param("idMedecin") Integer idMedecin,
                                          @Param("from") LocalDateTime from,
                                          @Param("to") LocalDateTime to);

    void deleteByIdMedecin(Integer idMedecin);
}
