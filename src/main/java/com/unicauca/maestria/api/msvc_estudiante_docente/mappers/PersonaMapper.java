package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Persona;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;

@Mapper(componentModel = "spring")
public interface PersonaMapper extends GenericMapper<PersonaDto, Persona>{

}
