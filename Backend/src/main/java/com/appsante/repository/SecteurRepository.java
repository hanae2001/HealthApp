package com.appsante.repository;

import com.appsante.entity.Secteur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecteurRepository extends JpaRepository<Secteur, Integer> {
    List<Secteur> findByIdVilleOrderByNomSecteurAsc(Integer idVille);
    boolean existsByNomSecteurAndIdVille(String nomSecteur, Integer idVille);
}
