package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoMaestriaActual;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstadoEstudianteDto {
	private Long id;
	private String codigo;
	private String nombre;
	private String apellido;
	private String director;
	private String codirector;
	private Integer semestreAcademico;
	private Integer semestreFinanciero;
	
	@NotNull
	private EstadoMaestriaActual estadoMaestria;
	
	@Valid
	private List<ProrrogaDto> prorrogas;
	
	@Valid
	private List<ReingresoDto> reingresos;
}
