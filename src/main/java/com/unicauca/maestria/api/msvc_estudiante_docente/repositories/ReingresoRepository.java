package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Reingreso;

public interface ReingresoRepository extends JpaRepository<Reingreso, Long>{

	public boolean existsByResolucion(String resolucion);
}
