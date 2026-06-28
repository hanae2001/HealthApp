package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class MedicamentResponse {
    private Integer id;
    private String nomMedicament;
    private String dosage;
    private String frequence;
    private String duree;
    private String instructions;
}
