package com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables;


import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoMaestriaActual;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.ModalidadIngreso;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.ModalidadMaestriaActual;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.util.BooleanConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder // revisar cambios
@Embeddable
public class InformacionMaestriaActual {

	@Enumerated(EnumType.STRING)
	private EstadoMaestriaActual estadoMaestria;
	
	@Enumerated(EnumType.STRING)
	private ModalidadMaestriaActual modalidad;
	
	@Convert(converter = BooleanConverter.class)
	private Boolean esEstudianteDoctorado;
	
	private String tituloDoctorado;
	private Integer Cohorte;
	private String periodoIngreso;
	
	@Enumerated(EnumType.STRING)
	private ModalidadIngreso modalidadIngreso;
	
	private Integer semestreAcademico;

	private Integer semestreFinanciero;
	
}
