package com.unicauca.maestria.api.msvc_estudiante_docente.common.util;

import javax.persistence.Converter;
import javax.persistence.AttributeConverter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String>{

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return attribute!=null? attribute?"si":"no":null;
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return dbData!=null? dbData.equalsIgnoreCase("si")?true:false:null;
	}

}
