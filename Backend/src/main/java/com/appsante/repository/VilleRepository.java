package com.appsante.repository;

import com.appsante.entity.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VilleRepository extends JpaRepository<Ville, Integer> {
    List<Ville> findAllByOrderByNomVilleAsc();
    boolean existsByNomVille(String nomVille);
}
