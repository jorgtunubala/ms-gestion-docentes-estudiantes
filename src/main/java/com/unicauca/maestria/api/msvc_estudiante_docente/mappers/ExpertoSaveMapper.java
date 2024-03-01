package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Experto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoSaveDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        componentModel = "spring",
        uses = {PersonaMapper.class})
public interface ExpertoSaveMapper extends GenericMapper<ExpertoSaveDto, Experto>{
    
}
