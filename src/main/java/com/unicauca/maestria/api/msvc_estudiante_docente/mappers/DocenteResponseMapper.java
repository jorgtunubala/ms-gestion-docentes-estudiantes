package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Docente;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteResponseDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
		componentModel = "spring",
		uses = {TituloMapper.class,PersonaMapper.class})
public interface DocenteResponseMapper extends GenericMapper<DocenteResponseDto, Docente> {

	@Override
	@Mapping(target = "lineasInvestigacion",ignore = true)
	DocenteResponseDto toDto(Docente entity);	
}
