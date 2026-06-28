package com.appsante.repository;

import com.appsante.entity.DossierMedical;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DossierMedicalRepository extends JpaRepository<DossierMedical, Integer> {
    Optional<DossierMedical> findByIdPatient(Integer idPatient);
}
