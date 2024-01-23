package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacionPersonalDto {
    private Integer id;
    private String nombres;
    private String apellidos;
    private String correo;
    private String celular;
    private String codigoAcademico;
    private String tipoDocumento;
    private String numeroDocumento;
}
