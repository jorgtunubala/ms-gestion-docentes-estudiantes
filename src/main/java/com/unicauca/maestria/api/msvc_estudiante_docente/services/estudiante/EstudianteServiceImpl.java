package com.unicauca.maestria.api.msvc_estudiante_docente.services.estudiante;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.Caracterizacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.embeddables.InformacionMaestriaActual;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.BecaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.CamposUnicosEstudianteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstadoEstudianteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.EstudianteSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.ProrrogaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.estudiante.ReingresoDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.exceptions.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.mappers.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.repositories.*;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

	private final EstudianteRepository estudianteRepository;
	private final DocenteRepository docenteRepository;
	private final ProrrogaRepository prorrogaRepository;
	private final ReingresoRepository reingresoRepository;
	private final PersonaRepository  personaRepository; 
	private final DocenteEstudianteRepository docenteEstudianteRepository;
	private final EstudianteSaveMapper estudianteSaveMapper;
	private final EstudianteResponseMapper estudianteResponseMapper;
	private final EstadoEstudianteDtoMapper estadoEstudianteDtoMapper;
	private final ProrrogaMapper prrorrogaMapper;
	private final ReingresoMapper reingresoMapper;
	private final InformacionUnicaEstudiante informacionUnicaEstudiante;
    private final Validator validator;

	@Override
	@Transactional
	public EstudianteResponseDto crear(EstudianteSaveDto estudianteSaveDto,BindingResult result) {
	
		if(result.hasErrors()) {
			throw new FieldErrorException(result);
		}
		
		CamposUnicosEstudianteDto camposUnicosEstudiante = informacionUnicaEstudiante.apply(estudianteSaveDto);
		
		Map<String, String> validacionCamposUnicos = validacionCampoUnicos(camposUnicosEstudiante,null);
		if(!validacionCamposUnicos.isEmpty()) {
			throw new FieldUniqueException(validacionCamposUnicos);
		}
		Estudiante estudianteBD = estudianteRepository.save(estudianteSaveMapper.toEntity(estudianteSaveDto));
		crearRelacionDocenteEstudiante(estudianteBD, estudianteSaveDto.getIdDirector(), estudianteSaveDto.getIdCodirector());
		
		return crearEstudianteDto(estudianteBD);
	}
	
	@Override
	@Transactional
	public EstudianteResponseDto actualizar(Long idEstudiante, EstudianteSaveDto estudianteSaveDto, BindingResult result) {
		
		if(result.hasErrors()) {
			throw new FieldErrorException(result);
		}
		
		Estudiante estudiante = estudianteSaveMapper.toEntity(estudianteSaveDto);
		Estudiante EstudianteBD = estudianteRepository.findById(idEstudiante)
				.orElseThrow(()->new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));
		 
		CamposUnicosEstudianteDto camposUnicosEstudiante = informacionUnicaEstudiante.apply(estudianteSaveDto);
		CamposUnicosEstudianteDto camposUnicosEstudianteBD = informacionUnicaEstudiante.apply(estudianteSaveMapper.toDto(EstudianteBD));
		
		Map<String, String> validacionCamposUnicos = validacionCampoUnicos(camposUnicosEstudiante,camposUnicosEstudianteBD);
		if(!validacionCamposUnicos.isEmpty()) {
			throw new FieldUniqueException(validacionCamposUnicos);
		}
		actualizarDirectorCodirector(idEstudiante, estudianteSaveDto.getIdDirector(),estudianteSaveDto.getIdCodirector());
		actualizarInformacionEstudiante(estudiante, EstudianteBD);
		
		Estudiante estudianteActualizado = estudianteRepository.save(EstudianteBD);
		
		return crearEstudianteDto(estudianteActualizado);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EstudianteResponseDto> listar() {

		return estudianteRepository.findAll().stream()
				.map(this::crearEstudianteDto)
				.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<EstudianteResponseDto> listarPaginado(Pageable page) {

		return (estudianteRepository.findAll(page))
				.map(this::crearEstudianteDto);
	}

	@Override
	@Transactional(readOnly = true)
	public EstudianteResponseDto buscarPorId(Long idEstudiante) {
		return estudianteRepository.findById(idEstudiante).map(this::crearEstudianteDto)
				.orElseThrow(() -> new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));
	}
	
	@Override
	@Transactional(readOnly = true)
	public EstadoEstudianteDto verEstadoEstudiante(Long idEstudiante) {
		
		return estudianteRepository.findById(idEstudiante)
				.map(this::crearEstadoEstudianteDto)
				.orElseThrow(()->new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));
	}
		
	@Override
	@Transactional
	public EstadoEstudianteDto actualizarEstadoEstudiante(Long idEstudiante, EstadoEstudianteDto estadoEstudianteDto,BindingResult result) {
		if(result.hasErrors()) {
			throw new FieldErrorException(result);
		}
		Estudiante estudianteBD = 
				estudianteRepository.findById(idEstudiante).orElseThrow(()->new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));
		
		estudianteBD.getInformacionMaestria().setEstadoMaestria(estadoEstudianteDto.getEstadoMaestria());
		estudianteBD.getInformacionMaestria().setSemestreAcademico(estadoEstudianteDto.getSemestreAcademico());
		estudianteBD.getInformacionMaestria().setSemestreFinanciero(estadoEstudianteDto.getSemestreFinanciero());
		estudianteBD.setProrrogas(prrorrogaMapper.toEntityList(estadoEstudianteDto.getProrrogas()));
		estudianteBD.setReingresos(reingresoMapper.toEntityList(estadoEstudianteDto.getReingresos()));
		
		return crearEstadoEstudianteDto(estudianteRepository.save(estudianteBD)); //estadoEstudianteDtoMapper.toDto(estudianteRepository.save(estudianteBD));
	}
	
	@Override
	@Transactional
	public void eliminarFisico(Long idEstudiante) {
		estudianteRepository.findById(idEstudiante)
				.orElseThrow(()->new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));
		docenteEstudianteRepository.deleteAllById(docenteEstudianteRepository.findAllIdsByIdEstudiante(idEstudiante));
		estudianteRepository.deleteById(idEstudiante);
	}

	@Override
	@Transactional
	public void eliminarLogico(Long idEstudiante) {
		estudianteRepository.findById(idEstudiante).
			map(e->{
				e.getInformacionMaestria().setEstadoMaestria(EstadoMaestriaActual.RETIRADO);
				return estudianteRepository.save(e);
			}).orElseThrow(()->new ResourceNotFoundException("Estudiante con id: " + idEstudiante + " No encontrado"));

	}
	
	@Override
	@Transactional
	public EstadoCargaMasivaDto cargarEstudiantes(MultipartFile file) {
		EstadoCargaMasivaDto estadoCargaMasivaDto=null;

		try (Workbook workBook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheetEstudiante = workBook.getSheetAt(0);
			Sheet sheetBeca = workBook.getSheetAt(1);
			Sheet sheetInfMaestria = workBook.getSheetAt(2);
			Sheet sheetReingreso = workBook.getSheetAt(3);
			Sheet sheetProrroga = workBook.getSheetAt(4);

			List<EstudianteSaveDto> estudiantes = StreamSupport.stream(sheetEstudiante.spliterator(), false)
					.skip(1)
					.map(rowEstudiante -> 
							crearEstudiante(rowEstudiante, sheetBeca, sheetInfMaestria, sheetProrroga,sheetReingreso))
					.toList();
			
			List<Long> idsPersonasBD = personaRepository.findAll().stream().map(Persona::getIdentificacion).toList();
			
			List<PersonaDto> EstudiantesExistentes = estudiantes.stream()
					.filter(estudiante->idsPersonasBD.contains(estudiante.getPersona().getIdentificacion()))
					.map(estudiante->estudiante.getPersona())
					.toList();
			
			List<PersonaDto> estudiantesEstructuraIncorrecta = estudiantes.stream().filter(this::ValidarEstudiante)
					.map(estudiante->estudiante.getPersona())
					.toList();
            
			List<EstudianteSaveDto> estudiantesAcargar = estudiantes
					.stream().filter(estudiante->
					!EstudiantesExistentes.contains(estudiante.getPersona())&&!estudiantesEstructuraIncorrecta.contains(estudiante.getPersona()))
					.toList();
				
			List<Estudiante> EstudiantesCargados = estudianteRepository.saveAll(estudianteSaveMapper.toEntityList(estudiantesAcargar));
			estadoCargaMasivaDto = EstadoCargaMasivaDto.builder()
					.registrados(EstudiantesCargados.size())
					.existentes(EstudiantesExistentes)
					.estructuraIncorrecta(estudiantesEstructuraIncorrecta).build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return estadoCargaMasivaDto;
	}
	
	private boolean ValidarEstudiante(@Valid EstudianteSaveDto estudianteSaveDto) {
		BindingResult bindingResult = new BeanPropertyBindingResult(estudianteSaveDto, "estudianteSaveDto");
		validator.validate(estudianteSaveDto, bindingResult);
		return bindingResult.hasErrors();
	}
	
	private void actualizarInformacionEstudiante(Estudiante estudiante,Estudiante estudianteBD) {
		estudianteBD.setPersona(estudiante.getPersona());
		estudianteBD.setBeca(estudiante.getBeca());
		estudianteBD.setCodigo(estudiante.getCodigo());
		estudianteBD.setCiudadResidencia(estudiante.getCiudadResidencia());
		estudianteBD.setCorreoUniversidad(estudiante.getCorreoUniversidad());
		estudianteBD.setFechaGrado(estudiante.getFechaGrado());
		estudianteBD.setTituloPregrado(estudiante.getTituloPregrado());
		estudianteBD.setCaracterizacion(estudiante.getCaracterizacion());
		estudianteBD.setInformacionMaestria(estudiante.getInformacionMaestria());
		estudianteBD.setProrrogas(estudiante.getProrrogas());
		estudianteBD.setReingresos(estudiante.getReingresos());
		
	}
	
	@Transactional
	private void crearRelacionDocenteEstudiante(Estudiante estudianteBD,Long idDirector,Long idCodirector) {
		docenteRepository.findAllById(Arrays.asList(idDirector,idCodirector)).forEach(docente->{
			String rolDocente = docente.getId().equals(idDirector)?"Director":"Codirector";
			
			DocenteEstudiante relacion = DocenteEstudiante.builder()
					.estudiante(estudianteBD)
					.docente(docente)
					.tipo(rolDocente).build();
			
			docenteEstudianteRepository.save(relacion);
		});
	}
	
	
	private void actualizarDirectorCodirector(Long idEstudiante,Long idDirector,Long idCodirector) {
		Docente director= docenteRepository.findById(idDirector).orElseThrow(() -> new ResourceNotFoundException("Docente con id: " + idDirector + " No encontrado"));
		Docente coDirector= docenteRepository.findById(idCodirector).orElseThrow(() -> new ResourceNotFoundException("Docente con id: " + idCodirector + " No encontrado"));
		
		List<DocenteEstudiante> listaRelacionDE = docenteEstudianteRepository.findAllById(idEstudiante).stream().map(de->{
			if(de.getTipo().equals("Director")) {
				de.setDocente(director);
			}else {				
				de.setDocente(coDirector);
			}
			return de;
		}).toList();
		
		listaRelacionDE.forEach(docenteEstudianteRepository::save);
	}
	
	private Map<String, String> validacionCampoUnicos(CamposUnicosEstudianteDto camposUnicos,CamposUnicosEstudianteDto camposUnicosBD) {

		Map<String, Function<CamposUnicosEstudianteDto, Boolean>> mapCamposUnicos= new HashMap<>();
		mapCamposUnicos.put("codigo", dto-> (camposUnicosBD==null || !dto.getCodigo().equals(camposUnicosBD.getCodigo())) && estudianteRepository.existsByCodigo(dto.getCodigo()));
		mapCamposUnicos.put("identificacion", dto->(camposUnicosBD == null || !dto.getIdentificacion().equals(camposUnicosBD.getIdentificacion())) && personaRepository.existsByIdentificacion(dto.getIdentificacion()));
		mapCamposUnicos.put("correoElectronico", dto->(camposUnicosBD == null || !dto.getCorreoElectronico().equals(camposUnicosBD.getCorreoElectronico())) && personaRepository.existsByCorreoElectronico(dto.getCorreoElectronico()));
		mapCamposUnicos.put("correoUniversidad",dto->(camposUnicosBD==null || !dto.getCorreoUniversidad().equals(camposUnicosBD.getCorreoUniversidad())) && estudianteRepository.existsByCorreoUniversidad(dto.getCorreoUniversidad()));
		mapCamposUnicos.put("prorrogas",dto->{
			List<ProrrogaDto> prorrogasInvalidas = dto.getProrrogas().stream()
					.filter(prorroga-> (camposUnicosBD!=null && !camposUnicosBD.getProrrogas().contains(prorroga)))
					.filter(prorroga->prorrogaRepository.existsByResolucion(prorroga.getResolucion()) || dto.getProrrogas().stream().filter(p->p.getResolucion().equals(prorroga.getResolucion())).count()>1)
					.collect(Collectors.toList());
			dto.setProrrogas(prorrogasInvalidas);
			return !prorrogasInvalidas.isEmpty();
		});
		
		mapCamposUnicos.put("reingresos",dto->{
			List<ReingresoDto> reingresosInvalidos = dto.getReingresos().stream()
					.filter(reingreso-> (camposUnicosBD!=null && !camposUnicosBD.getReingresos().contains(reingreso)))
					.filter(reingreso->reingresoRepository.existsByResolucion(reingreso.getResolucion()) || dto.getReingresos().stream().filter(r->r.getResolucion().equals(reingreso.getResolucion())).count()>1)
					.collect(Collectors.toList());
			dto.setReingresos(reingresosInvalidos);
			return !reingresosInvalidos.isEmpty();
		});
		
		Predicate<Field> existeCampoUnico=field->mapCamposUnicos.containsKey(field.getName());
		Predicate<Field> existeCampoBD=campo->mapCamposUnicos.get(campo.getName()).apply(camposUnicos);
		Predicate<Field> campoInvalido = existeCampoUnico.and(existeCampoBD);
		
		return Arrays.stream(camposUnicos.getClass().getDeclaredFields())
				.filter(campoInvalido)
				.peek(field->field.setAccessible(true))
				.collect(Collectors.toMap(Field::getName, field->{
					Object valorCampo=null;
					try {
						valorCampo=field.getName().equals("prorrogas")?
								camposUnicos.getProrrogas().stream().map(ProrrogaDto::getResolucion).distinct().toList():
									field.getName().equals("reingresos")?
											camposUnicos.getReingresos().stream().map(ReingresoDto::getResolucion).distinct().toList():field.get(camposUnicos);
					}catch (IllegalAccessException e) {e.printStackTrace();}
					return mensajeException(field.getName(),valorCampo);
		}));
	   
	}
	
	private EstudianteSaveDto crearEstudiante(Row rowEstudiante,Sheet sheetBeca,Sheet sheetInfMaestria,Sheet SheetProrroga,Sheet sheetReingreso) {
		String codigoEstudiante = rowEstudiante.getCell(7)!=null?rowEstudiante.getCell(7).getStringCellValue():"";
		String ciudad = rowEstudiante.getCell(8).getStringCellValue();
		String correoUniversidad = rowEstudiante.getCell(9)!=null?rowEstudiante.getCell(9).getStringCellValue():"";
		LocalDate fechaGrado = rowEstudiante.getCell(10).getLocalDateTimeCellValue().toLocalDate();
		String tituloPregado = rowEstudiante.getCell(11).getStringCellValue();
		
		return EstudianteSaveDto.builder()
				.persona(crearPersona(rowEstudiante))
				.codigo(codigoEstudiante)
				.ciudadResidencia(ciudad)
				.correoUniversidad(correoUniversidad)
				.fechaGrado(fechaGrado)
				.tituloPregrado(tituloPregado)
				.caracterizacion(crearCaracterizacion(rowEstudiante))
				.beca(crearBeca(codigoEstudiante, sheetBeca))
				.informacionMaestria(crearInformacionMaestria(codigoEstudiante, sheetInfMaestria))
				.reingresos(listaReingresos(codigoEstudiante, sheetReingreso))
				.prorrogas(listaProrrogas(codigoEstudiante, SheetProrroga))
				.build();
	}

	private PersonaDto crearPersona(Row rowEstudiante) {
		return PersonaDto.builder()
				.identificacion((long)rowEstudiante.getCell(0).getNumericCellValue())
				.nombre(rowEstudiante.getCell(1)!=null? rowEstudiante.getCell(1).getStringCellValue():"")
				.apellido(rowEstudiante.getCell(2)!=null?rowEstudiante.getCell(2).getStringCellValue():"")
				.correoElectronico(rowEstudiante.getCell(3)!=null?rowEstudiante.getCell(3).getStringCellValue():"")
				.telefono(rowEstudiante.getCell(4).getStringCellValue())
				.genero(obtenerValorEnum(rowEstudiante, 5, Genero.class))
				.tipoIdentificacion(obtenerValorEnum(rowEstudiante, 6, TipoIdentificacion.class))
				.build();
	}
	
	private Caracterizacion crearCaracterizacion(Row rowEstudiante) {
		return Caracterizacion.builder()
				.tipoPoblacion(obtenerValorEnum(rowEstudiante, 12, TipoPoblacion.class))
				.etnia(obtenerValorEnum(rowEstudiante, 13, Etnia.class))
				.discapacidad(obtenerValorEnum(rowEstudiante, 14, Discapacidad.class))
				.build();
	}
	
	private BecaDto crearBeca(String codigoEstudiante, Sheet sheetBeca) {
		Optional<Row> rowOptBeca = filtrarRow(codigoEstudiante, sheetBeca).findFirst();
		//TODO es ofrecidadPorUnicauca
		return rowOptBeca.isPresent()? BecaDto.builder()
				.titulo(rowOptBeca.get().getCell(1).getStringCellValue())
				.entidadAsociada(rowOptBeca.get().getCell(2).getStringCellValue())
				.tipo(obtenerValorEnum(rowOptBeca.get(), 3, TipoBeca.class))
				.dedicacion(obtenerValorEnum(rowOptBeca.get(), 4, DedicacionBeca.class))
				.build():null;
	}
	
	private InformacionMaestriaActual crearInformacionMaestria(String codigoEstudiante,Sheet sheetInfMaestria) {
		Row rowInfMaestria = filtrarRow(codigoEstudiante, sheetInfMaestria).findFirst().get();
		return InformacionMaestriaActual.builder()
				.estadoMaestria(obtenerValorEnum(rowInfMaestria, 1, EstadoMaestriaActual.class))
				.modalidad(obtenerValorEnum(rowInfMaestria, 2, ModalidadMaestriaActual.class))
				.tituloDoctorado(rowInfMaestria.getCell(3).getStringCellValue())
				.Cohorte((int)rowInfMaestria.getCell(4).getNumericCellValue())
				.periodoIngreso(rowInfMaestria.getCell(5).getStringCellValue())
				.modalidadIngreso(obtenerValorEnum(rowInfMaestria, 6, ModalidadIngreso.class))
				.semestreAcademico((int)rowInfMaestria.getCell(7).getNumericCellValue())
				.semestreFinanciero((int)rowInfMaestria.getCell(8).getNumericCellValue())
				.build();
	}
	
	private ReingresoDto crearReingreso(Row rowReingreso) {
		//TODO verficar el estado
		return ReingresoDto.builder()
				.semestreFinanciero((int)rowReingreso.getCell(1).getNumericCellValue())
				.semestreAcademico((int)rowReingreso.getCell(2).getNumericCellValue())
				.linkDocumento(rowReingreso.getCell(3)!=null?rowReingreso.getCell(3).getStringCellValue():"")
				.fecha(rowReingreso.getCell(5)!=null?rowReingreso.getCell(5).getLocalDateTimeCellValue().toLocalDate():null)
				.resolucion(rowReingreso.getCell(6)!=null?rowReingreso.getCell(6).getStringCellValue():"")
				.build();
	}
	
	private ProrrogaDto crearProrroga(Row rowProrroga) {
		//TODO verificar el estado
		return ProrrogaDto.builder()
				.linkDocumento(rowProrroga.getCell(1)!=null?rowProrroga.getCell(1).getStringCellValue():"")
				.fecha(rowProrroga.getCell(3)!=null?rowProrroga.getCell(3).getLocalDateTimeCellValue().toLocalDate():null)
				.resolucion(rowProrroga.getCell(4)!=null?rowProrroga.getCell(4).getStringCellValue():"")
				.soporte(rowProrroga.getCell(5).getStringCellValue())
				.tipoProrroga(rowProrroga.getCell(6)!=null?rowProrroga.getCell(6).getStringCellValue():"")
				.build();
	}
	
	private EstadoEstudianteDto crearEstadoEstudianteDto(Estudiante estudiante) {
		List<DocenteEstudiante> ListaDocenteEstudiante = docenteEstudianteRepository.findAllById(estudiante.getId());
		EstadoEstudianteDto estadoEstudianteDto = estadoEstudianteDtoMapper.toDto(estudiante);
		ListaDocenteEstudiante.forEach(docenteEstudiante->{
			if(docenteEstudiante.getTipo().equals("Director")) {
				estadoEstudianteDto.setDirector(docenteEstudiante.getDocente().getPersona().getNombre());
			}else {
				estadoEstudianteDto.setCodirector(docenteEstudiante.getDocente().getPersona().getNombre());
			}
		});
		return estadoEstudianteDto;
	}
	
	private EstudianteResponseDto crearEstudianteDto(Estudiante estudiante) {
		EstudianteResponseDto estudianteResponseDto = estudianteResponseMapper.toDto(estudiante);
		
		docenteEstudianteRepository.findAllById(estudiante.getId()).forEach(docenteEstudiante -> {
			if (docenteEstudiante.getTipo().equals("Director")) {
				estudianteResponseDto.setDirector(docenteEstudiante.getDocente().getPersona().getNombre());
			}
			estudianteResponseDto.setCodirector(docenteEstudiante.getDocente().getPersona().getNombre());
		});
		
		return estudianteResponseDto;
	}
	
	private List<ReingresoDto> listaReingresos(String codigoEstudiante,Sheet sheetReingreso){
		
		return filtrarRow(codigoEstudiante, sheetReingreso).map(this::crearReingreso).toList();
	}
	
	private List<ProrrogaDto> listaProrrogas(String codigoEstudiante,Sheet sheetProrroga){
		
		return filtrarRow(codigoEstudiante, sheetProrroga).map(this::crearProrroga).toList();
	}
	
	private Stream<Row> filtrarRow(String codigoEstudiante,Sheet sheet){
		Stream<Row> streamRow = StreamSupport.stream(sheet.spliterator(), false);
		Predicate<Row> codigosIguales = row->row.getCell(0).getStringCellValue().equals(codigoEstudiante);
		return streamRow.skip(1).filter(codigosIguales);
	}
	
	private <T extends Enum<T>> T obtenerValorEnum(Row row,int indiceCelda, Class<T> enumClass){
		String valorEnum = row.getCell(indiceCelda)!=null?row.getCell(indiceCelda).getStringCellValue():"";
		T valueOf=valorEnum!=""?Enum.valueOf(enumClass, valorEnum):null;
		return valueOf;
	}
	
	private <T> String mensajeException(String nombreCampo,T valorCampo) {
	    return "Campo único, ya existe un estudiante con la información: " + nombreCampo + ": " + valorCampo;
	}

	@Override
	public InformacionPersonalDto obtenerEstudiantePorCorreo(String correo) {
		Estudiante estudiante = estudianteRepository.obtenerEstudiantePorCorreo(correo);
		return estudianteToInformacionPersonal(estudiante);
	}

	private InformacionPersonalDto estudianteToInformacionPersonal(Estudiante estudiante){
		Persona persona = estudiante.getPersona();
		return new InformacionPersonalDto(
			estudiante.getId().intValue(),
			persona.getNombre(), persona.getApellido(),
			persona.getCorreoElectronico(),persona.getTelefono(),
			estudiante.getCodigo(),persona.getTipoIdentificacion().obtenerAbreviatura(),
			persona.getIdentificacion().toString());
	}

	@Override
	public InformacionPersonalDto obtenerEstudiantePorId(Integer id) {
		Estudiante estudiante = estudianteRepository.findById(id.longValue()).get();
		return estudianteToInformacionPersonal(estudiante);
	}

}
