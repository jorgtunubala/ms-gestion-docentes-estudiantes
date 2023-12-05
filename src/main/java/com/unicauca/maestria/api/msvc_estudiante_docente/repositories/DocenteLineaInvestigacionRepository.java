package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.DocenteLineaInvestigacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.LineaInvestigacion;

public interface DocenteLineaInvestigacionRepository extends JpaRepository<DocenteLineaInvestigacion, Long>{

	@Query("SELECT d.id FROM DocenteLineaInvestigacion d where d.docente.id = ?1")
	public List<Long> findAllByDocente(Long idDocente);
	
	@Query("SELECT d.lineaInvestigacion FROM DocenteLineaInvestigacion d where d.docente.id = ?1")
	List<LineaInvestigacion> findAllLineasInvByIdDocente(Long idDocente);
}
