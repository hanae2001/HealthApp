package com.appsante.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisponibiliteRequest {
    @NotBlank
    private String jourSemaine;
    @NotNull
    private String heureDebut;
    @NotNull
    private String heureFin;
}
