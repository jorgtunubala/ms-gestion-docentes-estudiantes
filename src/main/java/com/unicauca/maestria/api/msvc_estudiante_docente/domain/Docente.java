package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EscalafonDocente;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoVinculacion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data   @AllArgsConstructor
@Entity @Table(name = "docentes")
public class Docente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_persona")
	private	Persona persona;
	
	@Column(unique = true)
	private String codigo;
	
	private String facultad;
	
	private String departamento;
	
	@Enumerated(EnumType.STRING)
	private TipoVinculacion tipoVinculacion;
	
	@Enumerated(EnumType.STRING)
	private EscalafonDocente escalafon;
	
	private String observacion;
	
	@Enumerated(EnumType.STRING)
	private EstadoPersona estado;
	
	
	@OneToMany(mappedBy = "docente",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Titulo> titulos;
	
	public Docente() {
		titulos = new ArrayList<>();
		estado = EstadoPersona.ACTIVO;
	}
	public void agregarTitulo(Titulo titulo) {
		titulo.setDocente(this);
		titulos.add(titulo);
	}
		
	
	public void setTitulos(List<Titulo> titulos) {
		this.titulos.clear();
		titulos.forEach(this::agregarTitulo);
	}

}
