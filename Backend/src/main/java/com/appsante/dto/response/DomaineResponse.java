package com.appsante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DomaineResponse {
    private Integer idDomaine;
    private String nomDomaine;
    private String icone;
}
