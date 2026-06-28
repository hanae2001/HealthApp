package com.appsante.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RendezVousRequest {
    @NotNull private Integer idMedecin;
    @NotNull private String dateHeure;  // "2026-06-20T09:00"
    private String motif;
}
