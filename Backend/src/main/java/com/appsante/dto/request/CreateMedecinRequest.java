package com.appsante.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateMedecinRequest {

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String motDePasse;

    private String telephone;

    @NotNull
    private Integer idEtablissement;

    @NotNull
    private Integer idDomaine;

    @NotBlank
    private String numeroOrdre;

    private Integer experienceAns;

    private BigDecimal tarifConsultation;
}
