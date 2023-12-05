package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class EstadoCargaMasivaDto {

	private Integer registrados;
	private List<PersonaDto> existentes;
	private List<PersonaDto> estructuraIncorrecta; 
}
