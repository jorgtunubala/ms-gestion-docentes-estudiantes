package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Estudiante;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstadoEstudianteDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
componentModel = "spring",
uses = {ProrrogaMapper.class,ReingresoMapper.class})
public interface EstadoEstudianteDtoMapper extends GenericMapper<EstadoEstudianteDto, Estudiante>{

	@Override
	@Mappings({
		@Mapping(source = "persona.nombre",target = "nombre"),
		@Mapping(source="persona.apellido",target="apellido"),
		@Mapping(source = "informacionMaestria.estadoMaestria",target = "estadoMaestria"),
		@Mapping(source="informacionMaestria.semestreAcademico",target="semestreAcademico"),
		@Mapping(source="informacionMaestria.semestreFinanciero",target="semestreFinanciero"),
		@Mapping(target="director",ignore=true),
		@Mapping(target="codirector",ignore=true)
	})
	EstadoEstudianteDto toDto(Estudiante entity);

	
}
