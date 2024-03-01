package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CamposUnicosExpertoDto {
    
    private Long identificacion;
    private String correoElectronico;
}

