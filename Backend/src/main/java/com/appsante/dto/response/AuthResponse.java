package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private Integer id;
    private String email;
    private String role;
    private String token;
    private String nom;
    private String prenom;
}
