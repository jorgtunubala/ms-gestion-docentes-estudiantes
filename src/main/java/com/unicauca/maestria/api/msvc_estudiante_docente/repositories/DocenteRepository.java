package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Docente;

public interface DocenteRepository extends JpaRepository<Docente, Long>{

	public boolean existsByCodigo(String codigo);
	
	@Query("SELECT d FROM Docente d join d.persona p WHERE "
			+ "p.nombre LIKE %?1% OR d.codigo LIKE %?1% OR p.apellido LIKE %?1%")
	public List<Docente> findByNombreOrApellidoOrCodigo(String termino);
	public Docente findByCodigo(String codigo);

	@Query("SELECT d FROM Docente d join d.persona p WHERE "
			+ "d.estado = ?1")
	public List<Docente> findAllActiveDocentes(EstadoPersona estado);

	@Query("""
			SELECT d FROM Docente d 
			JOIN d.persona p 
			WHERE p.correoElectronico = ?1
			""")
	public Docente findByCorreo(String correo);
}
