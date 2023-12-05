package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Reingreso;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.ReingresoDto;

@Mapper(componentModel = "spring")
public interface ReingresoMapper extends GenericMapper<ReingresoDto, Reingreso>{

}
