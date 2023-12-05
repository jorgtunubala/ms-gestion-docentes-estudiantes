package com.unicauca.maestria.api.msvc_estudiante_docente.exceptions;

import org.springframework.validation.BindingResult;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class FieldErrorException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private BindingResult result;
	
	public FieldErrorException(BindingResult result) {
		this.result = result;
	}
	

}
