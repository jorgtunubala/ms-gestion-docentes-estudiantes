package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente;

import java.util.ArrayList;
import java.util.List;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EscalafonDocente;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoVinculacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor @Builder
public class DocenteResponseDto {
	
	private Long id;
	private EstadoPersona estado;
	private PersonaDto persona;
	private String codigo;	
	private String facultad;
	private String departamento;
	private EscalafonDocente escalafon;
	private String observacion;
	private List<LineaInvestigacionDto> lineasInvestigacion;
	private TipoVinculacion tipoVinculacion;
	private List<TituloDto> titulos;
	
	public DocenteResponseDto() {
		lineasInvestigacion = new ArrayList<>();
		titulos = new ArrayList<>();
	}
}
