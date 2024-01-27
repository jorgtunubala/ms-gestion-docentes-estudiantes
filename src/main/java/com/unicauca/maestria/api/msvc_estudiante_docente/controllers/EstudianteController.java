package com.unicauca.maestria.api.msvc_estudiante_docente.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstadoEstudianteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.services.estudiante.EstudianteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

	private final  EstudianteService estudianteService;
	
	@PostMapping
	public ResponseEntity<EstudianteResponseDto> crear(@Valid @RequestBody EstudianteSaveDto estudiante,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.crear(estudiante,result));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EstudianteResponseDto> Actualizar(@PathVariable Long id, @Valid @RequestBody EstudianteSaveDto estudiante,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.actualizar(id, estudiante, result));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EstudianteResponseDto> obtenerPorId(@PathVariable Long id){
		return ResponseEntity.ok(estudianteService.buscarPorId(id));
	}
	
	@GetMapping
	public ResponseEntity<List<EstudianteResponseDto>> listar(){
		return ResponseEntity.ok(estudianteService.listar());
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<EstudianteResponseDto>> listarPaginado(Pageable page){
		return ResponseEntity.ok(estudianteService.listarPaginado(page));
	}
	
	@PostMapping("/upload")
	private ResponseEntity<?> cargarExcel(@RequestParam("file") MultipartFile file) {
		EstadoCargaMasivaDto CargaEstudianteDto  = estudianteService.cargarEstudiantes(file);
		return ResponseEntity.status(HttpStatus.CREATED).body(CargaEstudianteDto);
	}
	
	@GetMapping("/ver-estado/{id}")
	public ResponseEntity<EstadoEstudianteDto> verEstadoEstudiante(@PathVariable Long id){
		return ResponseEntity.ok(estudianteService.verEstadoEstudiante(id));
	}
	
	@PatchMapping("/actualizar-estado/{id}")
	public ResponseEntity<EstadoEstudianteDto> ActualizarEstadoEstudiante(@PathVariable Long id,@Valid @RequestBody EstadoEstudianteDto estadoEstudianteDto,BindingResult result){
		return ResponseEntity.ok(estudianteService.actualizarEstadoEstudiante(id, estadoEstudianteDto,result));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> eliminarFisico(@PathVariable Long id){
		estudianteService.eliminarFisico(id);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/eliminar-logico/{id}")
	public ResponseEntity<?> eliminarLogico(@PathVariable Long id){
		estudianteService.eliminarLogico(id);;
		return ResponseEntity.ok().build();
	}

	@GetMapping("/informacion-personal/{correo}")
	public ResponseEntity<InformacionPersonalDto> obtenerInformacionEstudiante(@PathVariable String correo){
		return ResponseEntity.ok(estudianteService.obtenerEstudiantePorCorreo(correo));
	}

	@GetMapping("/informacion-personal-estudiante/{idEstudiante}")
	public ResponseEntity<InformacionPersonalDto> obtenerEstudiantePorId(@PathVariable Integer idEstudiante){
		return ResponseEntity.ok(estudianteService.obtenerEstudiantePorId(idEstudiante));
	}
}
