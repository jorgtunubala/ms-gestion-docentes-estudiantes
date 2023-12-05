package com.unicauca.maestria.api.msvc_estudiante_docente.exceptions;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Exception {

	private String mensaje;
	private HttpStatus estado;
	private LocalDate marcaTiempo;
	private String descripcionUrl;
}
