package com.appsante.dto.request;

import lombok.Data;

@Data
public class NoteCliniqueRequest {
    private Integer idRdv;
    private String diagnostic;
    private String planTraitement;
    private String observations;
}
