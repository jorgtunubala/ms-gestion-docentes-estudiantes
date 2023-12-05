package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Docente;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteSaveDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
		componentModel = "spring",
		uses = {TituloMapper.class,PersonaMapper.class})
public interface DocenteSaveMapper extends GenericMapper<DocenteSaveDto, Docente>{
	
}
