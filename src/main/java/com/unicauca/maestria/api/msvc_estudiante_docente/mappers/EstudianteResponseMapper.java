package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Estudiante;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteResponseDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
componentModel = "spring",
uses = {ReingresoMapper.class,ProrrogaMapper.class,PersonaMapper.class})
public interface EstudianteResponseMapper extends GenericMapper<EstudianteResponseDto, Estudiante>{

	@Override
	@Mappings(value = {
		@Mapping(target = "director",ignore = true),
		@Mapping(target = "codirector",ignore = true)
	})
	EstudianteResponseDto toDto(Estudiante estudiante);
	
}
