package com.appsante.repository;

import com.appsante.entity.Soin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SoinRepository extends JpaRepository<Soin, Integer> {
    List<Soin> findByIdDossierOrderByDateSoinDesc(Integer idDossier);
}
