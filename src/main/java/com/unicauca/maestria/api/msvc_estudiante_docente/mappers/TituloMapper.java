package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.Mapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Titulo;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.TituloDto;

@Mapper(componentModel = "spring")
public interface TituloMapper extends GenericMapper<TituloDto,Titulo>{

}
