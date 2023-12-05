package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.DocenteEstudiante;

public interface DocenteEstudianteRepository extends JpaRepository<DocenteEstudiante,Long>{

	@Query("SELECT de FROM DocenteEstudiante de WHERE de.estudiante.id =?1")
	List<DocenteEstudiante> findAllById(Long idEstudiante);
	
	@Query("SELECT de.id FROM DocenteEstudiante de where de.estudiante.id=?1")
	List<Long> findAllIdsByIdEstudiante(Long idEstudiante);
	

}
