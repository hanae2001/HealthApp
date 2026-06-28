package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class DossierResponse {
    private Integer idDossier;
    private String dateCreation;
    private String antecedentsMedicaux;
    private String antecedentsDentaires;
    private String notesGenerales;
    private List<NoteCliniqueResponse> notes;
    private List<SoinResponse> soins;
    private List<OrdonnanceResponse> ordonnances;
}
