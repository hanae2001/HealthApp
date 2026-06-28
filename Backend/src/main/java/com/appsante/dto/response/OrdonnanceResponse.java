package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data @AllArgsConstructor
public class OrdonnanceResponse {
    private Integer idOrdonnance;
    private String nomMedecin;
    private String prenomMedecin;
    private String dateEmission;
    private Integer validiteJours;
    private String notes;
    private List<MedicamentResponse> medicaments;
}
