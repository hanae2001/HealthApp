package com.appsante.repository;

import com.appsante.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedecinRepository extends JpaRepository<Medecin, Integer> {
    List<Medecin> findByEtablissement_IdEtablissement(Integer idEtablissement);
    List<Medecin> findByDomaine_IdDomaine(Integer idDomaine);
    boolean existsByNumeroOrdre(String numeroOrdre);
}
