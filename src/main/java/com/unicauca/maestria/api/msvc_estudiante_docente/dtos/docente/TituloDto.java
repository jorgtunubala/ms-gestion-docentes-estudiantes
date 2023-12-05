package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.AbreviaturaTitulo;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.CategoriaMinCiencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class TituloDto {

	private Long id;
	
	@NotNull
	private AbreviaturaTitulo abreviatura;
	
	@NotBlank
	private String universidad;
	
	@NotNull
	private CategoriaMinCiencia categoriaMinCiencia;
	
	@NotBlank
	private String linkCvLac;
}
