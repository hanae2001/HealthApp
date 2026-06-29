package com.appsante.repository;

import com.appsante.entity.NoteClinique;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoteCliniqueRepository extends JpaRepository<NoteClinique, Integer> {
    List<NoteClinique> findByIdDossierOrderByDateNoteDesc(Integer idDossier);
    void deleteByIdMedecin(Integer idMedecin);
}
