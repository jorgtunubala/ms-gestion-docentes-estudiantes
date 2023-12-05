package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.Caracterizacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.InformacionMaestriaActual;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data   @AllArgsConstructor
@Entity @Table(name = "estudiantes")
public class Estudiante {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_persona")
	private Persona persona;
		
    @OneToOne(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private Beca beca;
	
    @Column(unique = true)
	private String codigo;
	private String ciudadResidencia;
	
	@Column(unique = true)
	private String correoUniversidad;
	private LocalDate fechaGrado;
	private String tituloPregrado;
	private String observacion;
	
	@Embedded
	private Caracterizacion caracterizacion;
	
	@Embedded
	private InformacionMaestriaActual informacionMaestria;
	
	@OneToMany(mappedBy = "estudiante",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Reingreso> reingresos;
	
	@OneToMany(mappedBy = "estudiante",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Prorroga> prorrogas;
	
	
	public Estudiante() {
		this.prorrogas = new ArrayList<>();
		this.reingresos = new ArrayList<>();
	}
	
	public void agregarProrroga(Prorroga prorroga) {
		prorroga.setEstudiante(this);
		this.prorrogas.add(prorroga);
	}
	
	public void eliminarProrroga(Prorroga prorroga) {
		prorroga.setEstudiante(null);
		this.prorrogas.remove(prorroga);
	}
	

	public void agregarReingreso(Reingreso reingreso) {
		reingreso.setEstudiante(this);
		this.reingresos.add(reingreso);
	}
	
	public void eliminarReingreso(Reingreso reingreso) {
		reingreso.setEstudiante(null);
		this.reingresos.remove(reingreso);
	}
	
	public void setProrrogas(List<Prorroga> prorrogas) {
		this.prorrogas.clear();
		prorrogas.forEach(this::agregarProrroga);
	}
	
	public void setReingresos(List<Reingreso> reingresos) {
		this.reingresos.clear();
		reingresos.forEach(this::agregarReingreso);
	}
	
	public void setBeca(Beca beca) {
		if(beca!=null) {			
			beca.setEstudiante(this);
		}
		this.beca = beca;
	}

}
