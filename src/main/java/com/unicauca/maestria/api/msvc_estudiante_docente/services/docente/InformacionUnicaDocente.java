package com.unicauca.maestria.api.msvc_estudiante_docente.services.docente;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.CamposUnicosDocenteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteSaveDto;

@Service
public class InformacionUnicaDocente implements Function<DocenteSaveDto, CamposUnicosDocenteDto>{

	@Override
	public CamposUnicosDocenteDto apply(DocenteSaveDto docenteSaveDto) {
		
		return CamposUnicosDocenteDto.builder()
				.identificacion(docenteSaveDto.getPersona().getIdentificacion())
				.codigo(docenteSaveDto.getCodigo())
				.correoElectronico(docenteSaveDto.getPersona().getCorreoElectronico())
				.build();
	}

}
