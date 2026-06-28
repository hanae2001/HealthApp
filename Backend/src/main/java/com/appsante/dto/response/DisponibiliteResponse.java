package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibiliteResponse {
    private Integer idDispo;
    private String jourSemaine;
    private String heureDebut;
    private String heureFin;
}
