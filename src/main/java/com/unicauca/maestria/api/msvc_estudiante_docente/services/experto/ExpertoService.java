package com.unicauca.maestria.api.msvc_estudiante_docente.services.experto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;

public interface ExpertoService {

    ExpertoResponseDto Crear(ExpertoSaveDto experto, BindingResult result);
    List<ExpertoResponseDto> Listar();
    Page<ExpertoResponseDto> ListarPaginado(Pageable page);
    ExpertoResponseDto BuscarPorId(Long id);
    ExpertoResponseDto Actualizar(Long id, ExpertoSaveDto expertoSaveDto, BindingResult result);
    List<ExpertoResponseDto> FiltrarExpertos(String terminoBusqueda);
    EstadoCargaMasivaDto CargarExpertos(MultipartFile file);
    void EliminarFisico(Long id);
    void EliminarLogico(Long id);
    List<ExpertoResponseDto> ListarExpertosActivos(String estado);
    InformacionPersonalDto ObtenerExperto(String identificador);

    
}
