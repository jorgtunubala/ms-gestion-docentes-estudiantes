package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor @Builder
public class ExpertoSaveDto {

    @NotNull
    @Valid
    private PersonaDto persona;


    @NotBlank
    private String tituloexper;
    private String universidadtitexp;
    private String copiadocidentidad;
    private String universidadexp;
    private String facultadexp;
    private String grupoinvexp;
    private String lineainvexp;
    private String observacionexp;

    public ExpertoSaveDto() {
        
    }

    
}
