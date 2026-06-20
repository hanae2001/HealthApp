package com.appsante.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReceptionnisteRequest {

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
}
