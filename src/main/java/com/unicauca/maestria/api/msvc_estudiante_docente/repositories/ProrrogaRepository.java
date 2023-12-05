package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Prorroga;

public interface ProrrogaRepository extends JpaRepository<Prorroga, Long>{

	public boolean existsByResolucion(String resolucion);
}
