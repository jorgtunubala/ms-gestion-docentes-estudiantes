package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReingresoDto {
	private Long id;
	
	@NotBlank
	private String resolucion;
	
	@NotNull
	private LocalDate fecha;
	
	@NotBlank	
	private String linkDocumento;
	
	private Integer semestreFinanciero;
	private Integer semestreAcademico;
}
