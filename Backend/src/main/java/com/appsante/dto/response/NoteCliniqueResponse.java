package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class NoteCliniqueResponse {
    private Integer idNote;
    private String nomMedecin;
    private String prenomMedecin;
    private String nomDomaine;
    private String dateNote;
    private String diagnostic;
    private String planTraitement;
    private String observations;
}
