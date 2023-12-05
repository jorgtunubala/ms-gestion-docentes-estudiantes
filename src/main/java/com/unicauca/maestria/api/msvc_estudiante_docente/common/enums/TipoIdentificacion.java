package com.unicauca.maestria.api.msvc_estudiante_docente.common.enums;

public enum TipoIdentificacion {

	CEDULA_CIUDADANIA,PASAPORTE,CEDULA_EXTRANJERIA,DOCUMENTO_EXTRANJERO,VISA;
	
	public String obtenerAbreviatura() {
        switch (this) {
            case CEDULA_CIUDADANIA:
                return "CC";
            case PASAPORTE:
                return "PP";
            case CEDULA_EXTRANJERIA:
                return "CE";
            case DOCUMENTO_EXTRANJERO:
                return "DE";
            case VISA:
                return "VISA";
            default:
                throw new IllegalArgumentException("Tipo de identificación no reconocido.");
        }
    }
}
