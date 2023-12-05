package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Prorroga;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.ProrrogaDto;

@Mapper(componentModel = "spring")
public interface ProrrogaMapper extends GenericMapper<ProrrogaDto,Prorroga>{

}
