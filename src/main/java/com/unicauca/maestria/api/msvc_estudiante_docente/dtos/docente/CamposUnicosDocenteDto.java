package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CamposUnicosDocenteDto {

	private String codigo;
	private Long identificacion;
	private String correoElectronico;
}
