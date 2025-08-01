package com.app.dashboard.dashboard.dto;

import lombok.Data;

@Data
public class CompletarRegistroDTO {
    
    private String email;
    private String nuevaClave;
    private String correoBancario;
    private String llaveActual;
}
