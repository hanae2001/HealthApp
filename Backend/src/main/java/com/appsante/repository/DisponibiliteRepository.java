package com.appsante.repository;

import com.appsante.entity.Disponibilite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface DisponibiliteRepository extends JpaRepository<Disponibilite, Integer> {

    @Transactional
    void deleteByIdMedecin(Integer idMedecin);
}
