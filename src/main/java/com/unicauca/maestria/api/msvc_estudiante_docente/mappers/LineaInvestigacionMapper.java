package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.LineaInvestigacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.LineaInvestigacionDto;

@Mapper(componentModel = "spring")
public interface LineaInvestigacionMapper extends GenericMapper<LineaInvestigacionDto, LineaInvestigacion>{
}
