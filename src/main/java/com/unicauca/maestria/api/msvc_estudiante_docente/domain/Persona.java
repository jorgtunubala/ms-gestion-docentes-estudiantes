package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.Genero;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoIdentificacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data   @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "personas")
public class Persona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private Long identificacion;
	
	private String nombre;
	private String apellido;
	
	@Column(unique = true)
	private String correoElectronico;
	private String telefono;
	
	@Enumerated(EnumType.STRING)
	private Genero genero;
	
	@Enumerated(EnumType.STRING)
	private TipoIdentificacion tipoIdentificacion;
}
