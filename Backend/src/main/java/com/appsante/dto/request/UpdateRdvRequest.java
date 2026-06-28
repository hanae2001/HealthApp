package com.appsante.dto.request;

import lombok.Data;

@Data
public class UpdateRdvRequest {
    private String statut;    // null = pas de changement
    private String dateHeure; // "2026-07-01T10:00"
    private String notes;
}
