package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CamposUnicosEstudianteDto {

	private String codigo;
	private Long identificacion;
	private String correoElectronico;
	private String correoUniversidad;
	private List<ProrrogaDto> prorrogas;
	private List<ReingresoDto> reingresos;
}
