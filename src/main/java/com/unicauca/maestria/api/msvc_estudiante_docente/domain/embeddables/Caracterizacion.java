package com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.Discapacidad;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.Etnia;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoPoblacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor @AllArgsConstructor @Builder //pediente de cambios
@Embeddable
public class Caracterizacion {
	
	@Enumerated(EnumType.STRING)
	private TipoPoblacion tipoPoblacion;
	 	
	@Enumerated(EnumType.STRING)
	private Etnia etnia;
	
	@Enumerated(EnumType.STRING)
	private Discapacidad discapacidad;
	 
}
