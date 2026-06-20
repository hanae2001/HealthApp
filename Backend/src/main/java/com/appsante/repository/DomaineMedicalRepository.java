package com.appsante.repository;

import com.appsante.entity.DomaineMedical;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomaineMedicalRepository extends JpaRepository<DomaineMedical, Integer> {
}
