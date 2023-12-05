package com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EscalafonDocente;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoVinculacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor  @Builder
public class DocenteSaveDto {

	@NotNull
	@Valid
	private PersonaDto persona;
	
	@NotBlank
	private String codigo;	
	private String facultad;
	private String departamento;
	private EscalafonDocente escalafon;
	private String observacion;
	private List<Long> idsLineaInvestigacion;
	
	@NotNull
	private TipoVinculacion tipoVinculacion;
	
	
	@NotNull
	@Valid
	private List<TituloDto> titulos;
	
	public DocenteSaveDto() {
		idsLineaInvestigacion = new ArrayList<>();
		titulos = new ArrayList<>();
	}
	
	
}
