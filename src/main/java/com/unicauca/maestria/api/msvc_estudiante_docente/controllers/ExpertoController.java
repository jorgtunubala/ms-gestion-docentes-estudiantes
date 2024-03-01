package com.unicauca.maestria.api.msvc_estudiante_docente.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.services.experto.ExpertoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.sound.midi.MidiDevice.Info;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RequiredArgsConstructor
@RestController
@RequestMapping("/api/expertos")
public class ExpertoController {

    private final ExpertoService expertoService;

    @PostMapping
    public ResponseEntity<ExpertoResponseDto> crear(@Valid @RequestBody ExpertoSaveDto experto, BindingResult result){
        return ResponseEntity.status(HttpStatus.CREATED).body(expertoService.Crear(experto, result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpertoResponseDto> Actualizar(@PathVariable Long id, @Valid @RequestBody ExpertoSaveDto expertoSaveDto, BindingResult result){
        return ResponseEntity.status(HttpStatus.CREATED).body(expertoService.Actualizar(id, expertoSaveDto, result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpertoResponseDto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(expertoService.BuscarPorId(id));
    }
    
    @GetMapping
    public ResponseEntity<List<ExpertoResponseDto>> listar(){
        return ResponseEntity.ok(expertoService.Listar());
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ExpertoResponseDto>> listarPaginado(Pageable page){
        return ResponseEntity.ok(expertoService.ListarPaginado(page));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> cargarExcel(@RequestParam("file") MultipartFile file){
        return ResponseEntity.status(HttpStatus.CREATED).body(expertoService.CargarExpertos(file));
    }

    @GetMapping("/filtrar/{termino}")
    public ResponseEntity<List<ExpertoResponseDto>> filtrarExpertos(@PathVariable String termino){
        return ResponseEntity.ok(expertoService.FiltrarExpertos(termino));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarFisico(@PathVariable Long id){
        expertoService.EliminarFisico(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/eliminar-logico/{id}")
    public ResponseEntity<?> eliminarLogico(@PathVariable Long id){
        expertoService.EliminarLogico(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar/{estado}")
    public ResponseEntity<List<ExpertoResponseDto>> listarExpertosActivos(@PathVariable String estado){
        return ResponseEntity.ok(expertoService.ListarExpertosActivos(estado));
    }

    @GetMapping("/obtener-experto/{identificador}")
    public ResponseEntity<InformacionPersonalDto> obtenerExperto(@PathVariable String identificador){
        return ResponseEntity.ok(expertoService.ObtenerExperto(identificador));
    }
    


    
    
}
