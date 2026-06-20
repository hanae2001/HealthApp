package com.appsante.repository;

import com.appsante.entity.Etablissement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtablissementRepository extends JpaRepository<Etablissement, Integer> {
    List<Etablissement> findByIdSecteur(Integer idSecteur);
}
