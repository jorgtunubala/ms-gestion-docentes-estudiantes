package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Estudiante;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteSaveDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
		componentModel = "spring",
		uses = {ReingresoMapper.class,ProrrogaMapper.class,PersonaMapper.class,BecaMapper.class})
public interface EstudianteSaveMapper extends GenericMapper<EstudianteSaveDto,Estudiante>{
}
