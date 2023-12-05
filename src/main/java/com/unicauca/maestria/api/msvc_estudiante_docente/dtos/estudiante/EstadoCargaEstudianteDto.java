package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante;

import java.util.List;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
@Builder @NoArgsConstructor @AllArgsConstructor
public class EstadoCargaEstudianteDto {

	private Integer estudiantesRegistrados;
	private List<PersonaDto> EstudiantesExistentes;
	private List<PersonaDto> estudiantesEstructuraIncorrecta; 
}
