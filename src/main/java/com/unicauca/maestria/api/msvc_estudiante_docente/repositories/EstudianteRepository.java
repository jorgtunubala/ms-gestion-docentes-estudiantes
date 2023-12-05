package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long>{

	public boolean existsByCodigo(String codigo);
	public boolean existsByCorreoUniversidad(String correoUniversidad);
	
	@Query("SELECT e FROM Estudiante e join e.persona p WHERE "
			+ "e.correoUniversidad = ?1")
	public Estudiante obtenerEstudiantePorCorreo(String correo);
}
