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
public class ProrrogaDto {
	private Long id;
	
	@NotBlank
	private String resolucion;
	
	@NotBlank
	private String tipoProrroga;
	
	@NotBlank
	private String linkDocumento;
	
	@NotNull
	private LocalDate fecha;
	private String soporte;
	
}
