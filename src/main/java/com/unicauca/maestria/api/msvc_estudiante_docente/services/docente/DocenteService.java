package com.unicauca.maestria.api.msvc_estudiante_docente.services.docente;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteSaveDto;

public interface DocenteService {

	DocenteResponseDto crear(DocenteSaveDto docente,BindingResult result);
	List<DocenteResponseDto> listar();
	Page<DocenteResponseDto> listarPaginado(Pageable page);
	DocenteResponseDto buscarPorId(Long id);
	DocenteResponseDto actualizar(Long id,DocenteSaveDto docenteSaveDto,BindingResult result);
	List<DocenteResponseDto> filtrarDocentes(String terminoBusqueda);
	EstadoCargaMasivaDto cargarDocentes(MultipartFile file);
	void eliminarFisico(Long id);
	void eliminarLogico(Long id);
	List<InformacionPersonalDto> listarDocentesActivos(String estado);
	InformacionPersonalDto obtenerDocente(String identificador);
	
}
