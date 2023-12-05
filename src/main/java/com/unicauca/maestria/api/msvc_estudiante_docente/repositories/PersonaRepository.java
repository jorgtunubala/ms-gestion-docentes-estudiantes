package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long>{

	public boolean existsByIdentificacion(Long identificacion);
	public boolean existsByCorreoElectronico(String correoElectronico);
	
}
