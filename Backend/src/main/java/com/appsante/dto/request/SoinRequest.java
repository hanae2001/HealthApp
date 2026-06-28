package com.appsante.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SoinRequest {
    private Integer idRdv;
    private String typeSoin;
    private String dateSoin;
    private BigDecimal cout;
}
