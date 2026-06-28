package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RendezVousResponse {
    private Integer idRdv;
    private String nomPatient;
    private String prenomPatient;
    private String nomMedecin;
    private String prenomMedecin;
    private String nomDomaine;
    private String dateHeure;
    private Integer duree;
    private String motif;
    private String statut;
    private String notes;
}
