package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Experto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoResponseDto;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        componentModel = "spring",
        uses = {PersonaMapper.class})
public interface ExpertoResponseMapper extends GenericMapper<ExpertoResponseDto, Experto>{
        
        @Override
        ExpertoResponseDto toDto(Experto entity);
    
}
