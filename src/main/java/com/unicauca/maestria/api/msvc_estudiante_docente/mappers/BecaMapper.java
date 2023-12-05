package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Beca;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.BecaDto;

@Mapper(componentModel = "spring")
public interface BecaMapper extends GenericMapper<BecaDto, Beca>{
}
