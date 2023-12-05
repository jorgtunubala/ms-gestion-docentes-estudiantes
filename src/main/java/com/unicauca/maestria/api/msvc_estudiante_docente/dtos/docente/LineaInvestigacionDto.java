package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class LineaInvestigacionDto {
	
	private Long id;
	
	@NotBlank
	private String titulo;
	
	@NotBlank
	private String categoria;
}
