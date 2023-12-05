package com.unicauca.maestria.api.msvc_estudiante_docente.mappers;

import java.util.List;
import org.springframework.data.domain.Page;


public interface GenericMapper<D,E> {
	
	E toEntity(D dto);
	D toDto(E entity);
	List<E> toEntityList(List<D> dtos);
	List<D> toDtoList(List<E> entities);
	
	default Page<D> toDtoPage(Page<E> entityPage) {
		return entityPage.map(this::toDto);
	}
}
