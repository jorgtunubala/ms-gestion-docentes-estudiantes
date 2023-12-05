package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante;


import java.time.LocalDate;
import java.util.List;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.Caracterizacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.InformacionMaestriaActual;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class EstudianteResponseDto {
	private Long id;
	private String director;
	private String codirector;
	private PersonaDto persona;
	private BecaDto beca;
	private String codigo;
	private String ciudadResidencia;
	private String correoUniversidad;
	private LocalDate fechaGrado;
	private String tituloPregrado;
	private Caracterizacion caracterizacion;
	private InformacionMaestriaActual informacionMaestria;
	private List<ProrrogaDto> prorrogas;
	private List<ReingresoDto> reingresos;

}
