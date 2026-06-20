package com.appsante.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateEtablissementRequest {

    @NotBlank
    private String nomEtablissement;

    @NotNull
    private Integer idSecteur;

    private Integer idType = 1; // 1=cabinet par défaut

    private String adresse;

    private String telephone;
}
