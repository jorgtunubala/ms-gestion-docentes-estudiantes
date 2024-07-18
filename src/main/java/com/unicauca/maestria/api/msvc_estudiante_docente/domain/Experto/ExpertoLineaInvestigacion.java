package com.unicauca.maestria.api.msvc_estudiante_docente.domain.Experto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.lineaInvestigacion.LineaInvestigacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(name="expertos_linea_investigacion",uniqueConstraints = {
		@UniqueConstraint(columnNames = {"id_experto","id_linea_investigacion"})
})
public class ExpertoLineaInvestigacion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "id_experto")
	private Experto experto;
	
	@ManyToOne
	@JoinColumn(name= "id_linea_investigacion")
	private LineaInvestigacion lineaInvestigacion;
}
