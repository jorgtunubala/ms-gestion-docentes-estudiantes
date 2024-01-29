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

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.services.docente.DocenteService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

	
	private final  DocenteService docenteService;
	
	@PostMapping
	public ResponseEntity<DocenteResponseDto> crear(@Valid @RequestBody DocenteSaveDto docente,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.crear(docente,result));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<DocenteResponseDto> Actualizar(@PathVariable Long id, @Valid @RequestBody DocenteSaveDto docente,BindingResult result){
		return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.actualizar(id, docente, result));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DocenteResponseDto> obtenerPorId(@PathVariable Long id){
		return ResponseEntity.ok(docenteService.buscarPorId(id));
	}
	
	@GetMapping
	public ResponseEntity<List<DocenteResponseDto>> listar(){
		return ResponseEntity.ok(docenteService.listar());
	}

	@GetMapping("/page")
	public ResponseEntity<Page<DocenteResponseDto>> listarPaginado(Pageable page){
		return ResponseEntity.ok(docenteService.listarPaginado(page));
	}
	
	@PostMapping("/upload")
	public ResponseEntity<?> cargarExcel(@RequestParam("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.cargarDocentes(file));
	}
	
	@GetMapping("/filtrar/{termino}")
	public ResponseEntity<List<DocenteResponseDto>> filtrarDocentes(@PathVariable String termino){
		return ResponseEntity.ok(docenteService.filtrarDocentes(termino));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> eliminarFisico(@PathVariable Long id){
		docenteService.eliminarFisico(id);
		return ResponseEntity.ok().build();
	}
	
	@PatchMapping("/eliminar-logico/{id}")
	public ResponseEntity<?> eliminarLogico(@PathVariable Long id){
		docenteService.eliminarLogico(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/listar/{estado}")
	public ResponseEntity<List<InformacionPersonalDto>> listarDocentesActivos(@PathVariable String estado){
		return ResponseEntity.ok(docenteService.listarDocentesActivos(estado));
	}

	@GetMapping("/obtener-docente/{identificador}")
	public ResponseEntity<InformacionPersonalDto> obtenerDocente(@PathVariable String identificador){
		return ResponseEntity.ok(docenteService.obtenerDocente(identificador));
	}
}
