package com.unicauca.maestria.api.msvc_estudiante_docente.services.experto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.Genero;
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.TipoIdentificacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Experto;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Persona;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.CamposUnicosExpertoDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.Experto.ExpertoSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.exceptions.FieldErrorException;
import com.unicauca.maestria.api.msvc_estudiante_docente.exceptions.FieldUniqueException;
import com.unicauca.maestria.api.msvc_estudiante_docente.exceptions.ResourceNotFoundException;
import com.unicauca.maestria.api.msvc_estudiante_docente.mappers.ExpertoResponseMapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.mappers.ExpertoSaveMapper;
import com.unicauca.maestria.api.msvc_estudiante_docente.repositories.ExpertoRepository;
import com.unicauca.maestria.api.msvc_estudiante_docente.repositories.PersonaRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExpertoServiceImpl implements ExpertoService {

    private final PersonaRepository personaRepository;
    private final ExpertoRepository expertoRepository;
    private final ExpertoSaveMapper expertoSaveMapper;
    private final ExpertoResponseMapper expertoResponseMapper;
    private final InformacionUnicaExperto informacionUnicaExperto;
    private final Validator validator;

    @Override
    @Transactional
    public ExpertoResponseDto Crear(ExpertoSaveDto experto, BindingResult result) {
        if(result.hasErrors()){
            throw new FieldErrorException(result);
        }

        Map<String, String> validacionCamposUnicos = validacionCampoUnicos(obtenerCamposUnicos(experto),null);
        if(!validacionCamposUnicos.isEmpty()){
            throw new FieldUniqueException(validacionCamposUnicos);
        }
    
        Experto expertoBD = expertoRepository.save(expertoSaveMapper.toEntity(experto));
    
        // expertoBD.setPersona(personaRepository.findById(experto.getIdPersona()).orElseThrow(() -> new NotFoundException("Persona no encontrada")));
        // return expertoResponseMapper.toDto(expertoRepository.save(entity));
        return crearExpertoResposeDto(expertoBD);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpertoResponseDto> Listar() {
        List<ExpertoResponseDto> expertoResponseDto = expertoRepository.findAll()
                .stream()
                .map(this::crearExpertoResposeDto).toList();
        return expertoResponseDto;
    }

    @Override
    @Transactional(readOnly = true)

    public Page<ExpertoResponseDto> ListarPaginado(Pageable page) {
        return expertoRepository.findAll(page).map(this::crearExpertoResposeDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpertoResponseDto BuscarPorId(Long id) {
        return expertoRepository.findById(id)
                .map(this::crearExpertoResposeDto)
                .orElseThrow(() -> new ResourceNotFoundException("Experto con " + id + "no encontrado"));
    }

    @Override
    @Transactional
    public ExpertoResponseDto Actualizar(Long id, ExpertoSaveDto expertoSaveDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new FieldErrorException(result);
        }
        Experto experto = expertoSaveMapper.toEntity(expertoSaveDto);
        Experto expertoBD = expertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experto no encontrado"));

        Map<String, String> validacionCamposUnicos = validacionCampoUnicos(obtenerCamposUnicos(expertoSaveDto),
                obtenerCamposUnicos(expertoSaveMapper.toDto(expertoBD)));
        if (!validacionCamposUnicos.isEmpty()) {
            throw new FieldUniqueException(validacionCamposUnicos);
        }

        actualizarinformacionExperto(experto, expertoBD);
        Experto expertoSave = expertoRepository.save(expertoBD);
        return crearExpertoResposeDto(expertoBD);
    }

    @Override
    @Transactional
    public EstadoCargaMasivaDto CargarExpertos(MultipartFile file) {
        
        EstadoCargaMasivaDto estadoCargaMasivaDto = null;

        try (Workbook workBook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheetExpertos = workBook.getSheetAt(0);

            List<ExpertoSaveDto> expertos = StreamSupport.stream(sheetExpertos.spliterator(), false)
                    .skip(1)
                    .map(rowExperto -> crearExperto(rowExperto)).toList();

            List<Long> idsPersonasBD = personaRepository.findAll().stream().map(Persona::getIdentificacion).toList();

            List<PersonaDto> expertosEstructuraIncorrecta = expertos.stream()
                    .filter(this::ValidarExperto).map(experto -> experto.getPersona()).toList();

            List<PersonaDto> expertosExistente = expertos.stream()
                    .filter(experto -> idsPersonasBD.contains(experto.getPersona().getIdentificacion()))
                    .map(experto -> experto.getPersona()).toList();

            List<ExpertoSaveDto> expertosAcargar = expertos.stream()
                    .filter(experto -> !expertosExistente.contains(experto.getPersona())
                            && !expertosEstructuraIncorrecta.contains(experto.getPersona()))
                    .toList();

            List<Experto> expertosGuardados = expertoRepository.saveAll(expertoSaveMapper.toEntityList(expertosAcargar));

            estadoCargaMasivaDto = EstadoCargaMasivaDto.builder()
                    .registrados(expertosGuardados.size())
                    .existentes(expertosExistente)
                    .estructuraIncorrecta(expertosEstructuraIncorrecta)
                    .build();
            //
        } catch (IOException e) {
            e.printStackTrace();
            // throw new RuntimeException("Error al leer el archivo");
        }
        return estadoCargaMasivaDto;
    }

    private Boolean ValidarExperto(@Valid ExpertoSaveDto expertoSaveDto) {
        BindingResult bindingResult = new BeanPropertyBindingResult(expertoSaveDto, "expertoSaveDto");
        validator.validate(expertoSaveDto, bindingResult);
        return bindingResult.hasErrors();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpertoResponseDto> FiltrarExpertos(String terminoBusqueda) {
        return expertoRepository.findByNombreOrApellidoOrIdentificacion(terminoBusqueda)
                .stream()
                .map(this::crearExpertoResposeDto).toList();
    }

    @Override
    @Transactional
    public void EliminarFisico(Long id) {
        expertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experto no encontrado"));
        expertoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void EliminarLogico(Long id) {
        Experto experto = expertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experto no encontrado"));
        experto.setEstado(EstadoPersona.INACTIVO);
        expertoRepository.save(experto);

    }

    private CamposUnicosExpertoDto obtenerCamposUnicos(ExpertoSaveDto expertoSaveDto) {
        return informacionUnicaExperto.apply(expertoSaveDto);
    }

    private ExpertoSaveDto crearExperto(Row rowExperto) {
        String tituloexper = rowExperto.getCell(5).getStringCellValue();
        String universidadtitexp = rowExperto.getCell(6).getStringCellValue();
        String copiadocidentidad = rowExperto.getCell(7).getStringCellValue();
        String universidadexp = rowExperto.getCell(8).getStringCellValue();
        String facultadexp = rowExperto.getCell(9).getStringCellValue();
        String grupoinvexp = rowExperto.getCell(10).getStringCellValue();
        String lineainvexp = rowExperto.getCell(11).getStringCellValue();
        String observacionexp = rowExperto.getCell(12).getStringCellValue();

        return ExpertoSaveDto.builder()
                .persona(crearPersona(rowExperto))
                .tituloexper(tituloexper)
                .universidadtitexp(universidadtitexp)
                .copiadocidentidad(copiadocidentidad)
                .universidadexp(universidadexp)
                .facultadexp(facultadexp)
                .grupoinvexp(grupoinvexp)
                .lineainvexp(lineainvexp)
                .observacionexp(observacionexp)
                .build();

    }

    private PersonaDto crearPersona(Row rowExperto) {

        return PersonaDto.builder()
                .identificacion((long) rowExperto.getCell(0).getNumericCellValue())
                .nombre(rowExperto.getCell(1) != null ? rowExperto.getCell(1).getStringCellValue() : "")
                .apellido(rowExperto.getCell(2) != null ? rowExperto.getCell(2).getStringCellValue() : "")
                .correoElectronico(rowExperto.getCell(3) != null ? rowExperto.getCell(3).getStringCellValue() : "")
                .telefono(rowExperto.getCell(4).getStringCellValue())
                .genero(obtenerValorEnum(rowExperto, 5, Genero.class))
                .tipoIdentificacion(obtenerValorEnum(rowExperto, 6, TipoIdentificacion.class))
                .build();
    }

    private <T extends Enum<T>> T obtenerValorEnum(Row row, int index, Class<T> enumType) {

        return Enum.valueOf(enumType, row.getCell(index).getStringCellValue());
    }

    private ExpertoResponseDto crearExpertoResposeDto(Experto experto) {
        
        ExpertoResponseDto expertoResponseDto = expertoResponseMapper.toDto(experto);
        return expertoResponseDto;
        // return ExpertoResponseDto.builder()
        // .id(experto.getId())
        // .persona(mapToDto(experto.getPersona()))
        // .tituloexper(experto.getTituloexper())
        // .universidadtitexp(experto.getUniversidadtitexp())
        // .copiadocidentidad(experto.getCopiadocidentidad())
        // .universidadexp(experto.getUniversidadexp())
        // .facultadexp(experto.getFacultadexp())
        // .grupoinvexp(experto.getGrupoinvexp())
        // .lineainvexp(experto.getLineainvexp())
        // .observacionexp(experto.getObservacionexp())
        // .estado(experto.getEstado())
        // .build();
    }

    private Map<String, String> validacionCampoUnicos(CamposUnicosExpertoDto camposUnicosExpertoDto,
            CamposUnicosExpertoDto camposUnicosBD) {
        return Map.of(
                "identificacion",
                camposUnicosExpertoDto.getIdentificacion().equals(camposUnicosBD.getIdentificacion()) ? null
                        : "Identificacion ya existe",
                "correoElectronico",
                camposUnicosExpertoDto.getCorreoElectronico().equals(camposUnicosBD.getCorreoElectronico()) ? null
                        : "Correo Electronico ya existe");
    }

    private void actualizarinformacionExperto(Experto experto, Experto expertoBD) {
        expertoBD.setPersona(experto.getPersona());
        expertoBD.setTituloexper(experto.getTituloexper());
        expertoBD.setUniversidadtitexp(experto.getUniversidadtitexp());
        expertoBD.setCopiadocidentidad(experto.getCopiadocidentidad());
        expertoBD.setUniversidadexp(experto.getUniversidadexp());
        expertoBD.setFacultadexp(experto.getFacultadexp());
        expertoBD.setGrupoinvexp(experto.getGrupoinvexp());
        expertoBD.setLineainvexp(experto.getLineainvexp());
        expertoBD.setObservacionexp(experto.getObservacionexp());
    }

    @Override
    public List<ExpertoResponseDto> ListarExpertosActivos(String estado) 
    {
        
        return expertoRepository.findAllActiveExperto(EstadoPersona.valueOf(estado))
                .stream()
                .map(
                        experto -> ExpertoResponseDto.builder()
                                .id(experto.getId())
                                .persona(mapToDto(experto.getPersona()))
                                .tituloexper(experto.getTituloexper())
                                .universidadtitexp(experto.getUniversidadtitexp())
                                .copiadocidentidad(experto.getCopiadocidentidad())
                                .universidadexp(experto.getUniversidadexp())
                                .facultadexp(experto.getFacultadexp())
                                .grupoinvexp(experto.getGrupoinvexp())
                                .lineainvexp(experto.getLineainvexp())
                                .observacionexp(experto.getObservacionexp())
                                .build())
                .collect(Collectors.toList());
    }

    private PersonaDto mapToDto(Persona persona) {
        return PersonaDto.builder()
                .id(persona.getId())
                .identificacion(persona.getIdentificacion())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .correoElectronico(persona.getCorreoElectronico())
                .telefono(persona.getTelefono())
                .genero(persona.getGenero())
                .tipoIdentificacion(persona.getTipoIdentificacion())
                .build();
    }

    @Override
    public InformacionPersonalDto ObtenerExperto(String identificador) {

        // el identificador puede ser correo o identificacion
        Experto experto = null;
        if (identificador.contains("@")) {
            experto = expertoRepository.findByCorreo(identificador);
        } else {
            experto = expertoRepository.findByIdentificacion(Long.parseLong(identificador));
        }
        return InformacionPersonalDto.builder()
                .id(experto.getId().intValue())
                .numeroDocumento(experto.getPersona().getIdentificacion().toString())
                .nombres(experto.getPersona().getNombre())
                .apellidos(experto.getPersona().getApellido())
                .correo(experto.getPersona().getCorreoElectronico())
                .celular(experto.getPersona().getTelefono())
                .tipoDocumento(experto.getPersona().getTipoIdentificacion().obtenerAbreviatura())
                .build();

    }

   

}
