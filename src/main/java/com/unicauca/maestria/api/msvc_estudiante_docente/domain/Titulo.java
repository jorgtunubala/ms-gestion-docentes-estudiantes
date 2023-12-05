package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.AbreviaturaTitulo;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.CategoriaMinCiencia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "titulos")
public class Titulo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_docente")
	private Docente docente;
	
	@Enumerated(EnumType.STRING)
	private AbreviaturaTitulo abreviatura;
	
	@Enumerated(EnumType.STRING)
	private CategoriaMinCiencia categoriaMinCiencia;

	private String universidad;
	
	private String linkCvLac;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Titulo other = (Titulo) obj;
		return Objects.equals(id, other.id);
	}


	
	
}
