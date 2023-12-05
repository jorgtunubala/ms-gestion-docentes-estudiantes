package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.Genero;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoIdentificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor @Builder
public class PersonaDto {

	private Long id;
	
	@NotNull
	private Long identificacion;
	
	@NotBlank
	private String nombre;
	
	@NotBlank
	private String apellido;
	
	//@NotBlank
	@Email
	private String correoElectronico;
	
	//@NotBlank
	private String telefono;
	
	@NotNull
	private Genero genero;
	
	@NotNull
	private TipoIdentificacion tipoIdentificacion;
}
