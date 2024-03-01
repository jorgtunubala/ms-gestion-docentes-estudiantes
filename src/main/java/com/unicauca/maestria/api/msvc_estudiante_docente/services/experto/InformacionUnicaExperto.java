package com.unicauca.maestria.api.msvc_estudiante_docente.services.experto;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.CamposUnicosExpertoDto;


@Service
public class InformacionUnicaExperto implements Function<ExpertoSaveDto, CamposUnicosExpertoDto>{

	@Override
	public CamposUnicosExpertoDto apply(ExpertoSaveDto expertoSaveDto) {
		
		return CamposUnicosExpertoDto.builder()
				.identificacion(expertoSaveDto.getPersona().getIdentificacion())
				.correoElectronico(expertoSaveDto.getPersona().getCorreoElectronico())
				.build();
	}

}
