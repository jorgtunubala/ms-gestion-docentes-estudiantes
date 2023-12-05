package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity 
@Table(name = "docente_estudiante",uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id_docente","id_estudiante"})
		})
public class DocenteEstudiante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_docente")
	private Docente docente;
	
	@ManyToOne
	@JoinColumn(name = "id_estudiante")
	private Estudiante estudiante;
	
	private String tipo;	
}
