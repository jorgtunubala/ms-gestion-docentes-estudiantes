package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "prorrogas")
public class Prorroga {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_estudiante")
	private Estudiante estudiante;
	
	@Column(unique = true)
	private String resolucion;
	
	private String linkDocumento;
	
	private String estado;
	
	private LocalDate fecha;
	
	private String soporte;
	
	private String tipoProrroga;
}
