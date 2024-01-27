package com.unicauca.maestria.api.msvc_estudiante_docente.services.estudiante;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstadoEstudianteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteSaveDto;

public interface EstudianteService {
	
	
	EstudianteResponseDto crear(EstudianteSaveDto estudiante,BindingResult result);
	EstudianteResponseDto actualizar(Long idEstudiante,EstudianteSaveDto estudiante,BindingResult result);
	List<EstudianteResponseDto> listar();
	Page<EstudianteResponseDto> listarPaginado(Pageable page);
	EstudianteResponseDto buscarPorId(Long idEstudiante);
	EstadoEstudianteDto verEstadoEstudiante(Long idEstudiante);
	EstadoEstudianteDto actualizarEstadoEstudiante(Long idEstudiante,EstadoEstudianteDto estadoEstudianteDto,BindingResult result);
	void eliminarFisico(Long idEstudiante);
	void eliminarLogico(Long idEstudiante);
	EstadoCargaMasivaDto cargarEstudiantes(MultipartFile file);
	InformacionPersonalDto obtenerEstudiantePorCorreo(String correo);
	InformacionPersonalDto obtenerEstudiantePorId(Integer id);
}
