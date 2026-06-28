package com.appsante.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class OrdonnanceRequest {
    private Integer idRdv;
    private String notes;
    private Integer validiteJours;
    private List<MedicamentItem> medicaments;

    @Data
    public static class MedicamentItem {
        private String nomMedicament;
        private String dosage;
        private String frequence;
        private String duree;
        private String instructions;
    }
}
