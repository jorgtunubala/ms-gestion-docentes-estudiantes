package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString   @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "lineas_investigacion")
public class LineaInvestigacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String titulo;
	
	private String categoria;


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineaInvestigacion other = (LineaInvestigacion) obj;
		return Objects.equals(id, other.id);
	}
	
	
}
