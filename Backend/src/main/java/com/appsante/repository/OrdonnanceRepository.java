package com.appsante.repository;

import com.appsante.entity.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Integer> {
    List<Ordonnance> findByIdDossierOrderByDateEmissionDesc(Integer idDossier);
    List<Ordonnance> findByIdMedecin(Integer idMedecin);
    void deleteByIdMedecin(Integer idMedecin);
}
