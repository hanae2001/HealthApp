package com.appsante.dto.request;

import lombok.Data;

@Data
public class RdvRequest {
    private String domaine;
    private String ville;
    private String secteur;
    private String cabinetNom;
    private String medecinNom;
    private String medecinSpec;
    private String date;
    private String heure;
    private String motif;
    private String patientEmail;
    private String patientNom;
}
