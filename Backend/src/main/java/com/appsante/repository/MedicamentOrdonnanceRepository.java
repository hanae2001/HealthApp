package com.appsante.repository;

import com.appsante.entity.MedicamentOrdonnance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicamentOrdonnanceRepository extends JpaRepository<MedicamentOrdonnance, Integer> {
    List<MedicamentOrdonnance> findByIdOrdonnance(Integer idOrdonnance);
    void deleteByIdOrdonnanceIn(List<Integer> idOrdonnances);
}
