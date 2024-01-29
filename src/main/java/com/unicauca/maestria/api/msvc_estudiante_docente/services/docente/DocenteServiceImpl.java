package com.unicauca.maestria.api.msvc_estudiante_docente.services.docente;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Docente;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.DocenteLineaInvestigacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.LineaInvestigacion;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Persona;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.EstadoCargaMasivaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.InformacionPersonalDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.common.PersonaDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.CamposUnicosDocenteDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteResponseDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.DocenteSaveDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.LineaInvestigacionDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.dtos.docente.TituloDto;
import com.unicauca.maestria.api.msvc_estudiante_docente.exceptions.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.mappers.*;
import com.unicauca.maestria.api.msvc_estudiante_docente.repositories.*;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DocenteServiceImpl implements DocenteService{

	private final PersonaRepository personaRepository;
	private final DocenteRepository docenteRepository;
	private final DocenteLineaInvestigacionRepository docenteLineaInvestigacionRepository;
	private final LineaInvestigacionRepository lineaInvestigacionRepository; 
	private final DocenteSaveMapper docenteSaveMapper;
	private final DocenteResponseMapper DocenteResponseMapper;
	private final LineaInvestigacionMapper lineaInvestigacionMapper;
	private final InformacionUnicaDocente informacionUnicaDocente;
    private final Validator validator;
	
	@Override
	@Transactional
	public DocenteResponseDto crear(DocenteSaveDto docenteSaveDto,BindingResult result) {
		if(result.hasErrors()) {
			throw new FieldErrorException(result);
		}
				
		Map<String, String> validacionCamposUnicos = validacionCampoUnicos(obtenerCamposUnicos(docenteSaveDto),null);
		if(!validacionCamposUnicos.isEmpty()) {
			throw new FieldUniqueException(validacionCamposUnicos);
		}
		
		Docente docenteBD = docenteRepository.save(docenteSaveMapper.toEntity(docenteSaveDto));
		asignarLineasInvestigacionDocente(docenteBD, docenteSaveDto.getIdsLineaInvestigacion());
		
		return crearDocenteReponseDto(docenteBD);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DocenteResponseDto> listar() {
		
		List<DocenteResponseDto> docentesResponseDto =
				docenteRepository.findAll()
				.stream()
				.map(this::crearDocenteReponseDto).toList();
		
		return docentesResponseDto;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DocenteResponseDto> listarPaginado(Pageable page) {
		return docenteRepository.findAll(page)
				.map(this::crearDocenteReponseDto);
	}

	@Override
	@Transactional(readOnly = true)
	public DocenteResponseDto buscarPorId(Long id) {
		
		return docenteRepository.findById(id)
				.map(this::crearDocenteReponseDto)
				.orElseThrow(()->new ResourceNotFoundException("Docente con id: "+id+" No encontrado"))
				;
	}
	
	@Override
	@Transactional
	public DocenteResponseDto actualizar(Long id,DocenteSaveDto docenteSaveDto,BindingResult result) {
		if(result.hasErrors()) {
			throw new FieldErrorException(result);
		}

		Docente docente = docenteSaveMapper.toEntity(docenteSaveDto);
		Docente docenteBD = docenteRepository.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Docente con id: "+id+" No encontrado"));
			
		Map<String, String> validacionCamposUnicos=validacionCampoUnicos(obtenerCamposUnicos(docenteSaveDto),obtenerCamposUnicos(docenteSaveMapper.toDto(docenteBD)));
		if(!validacionCamposUnicos.isEmpty()) {
			throw new FieldUniqueException(validacionCamposUnicos);
		}
		
		actualizarInformacionDocente(docente, docenteBD,docenteSaveDto.getIdsLineaInvestigacion());
		Docente docenteSave = docenteRepository.save(docenteBD);
		return crearDocenteReponseDto(docenteSave);
	}
	
	
	@Override
	@Transactional
	public EstadoCargaMasivaDto cargarDocentes(MultipartFile file) {
		
		
		EstadoCargaMasivaDto estadoCargaMasivaDto=null;
		try (Workbook workBook = new XSSFWorkbook(file.getInputStream())){
			Sheet sheetDocente = workBook.getSheetAt(0);
			Sheet sheetLineaInvestigacion = workBook.getSheetAt(1);
			Sheet sheetTitulo = workBook.getSheetAt(2);
			
			List<DocenteSaveDto> docentes = StreamSupport.stream(sheetDocente.spliterator(), false)
					.skip(1)
					.map(rowDocente->crearDocente(rowDocente,sheetTitulo)).toList();
					
			List<Long> idsPersonasBD = personaRepository.findAll().stream().map(Persona::getIdentificacion).toList();
			
			List<PersonaDto> docentesEstructuraIncorrecta = docentes.stream()
					.filter(this::ValidarDocente).map(docente->docente.getPersona())
					.toList();
			
			List<PersonaDto> docentesExistentes = docentes.stream()
					.filter(docente->idsPersonasBD.contains(docente.getPersona().getIdentificacion()))
					.map(docente->docente.getPersona())
					.toList();
			
			List<DocenteSaveDto> docentesAcargar = docentes
					.stream().filter(docente->
					!docentesExistentes.contains(docente.getPersona())&&!docentesEstructuraIncorrecta.contains(docente.getPersona()))
					.toList();
				
			List<Docente> docentesCargados = docenteRepository.saveAll(docenteSaveMapper.toEntityList(docentesAcargar));
			
			estadoCargaMasivaDto = EstadoCargaMasivaDto.builder()
					.registrados(docentesCargados.size())
					.existentes(docentesExistentes)
					.estructuraIncorrecta(docentesEstructuraIncorrecta).build();
			
			StreamSupport.stream(sheetLineaInvestigacion.spliterator(), false)
			.skip(1).forEach(this::cargarLineasInvestigacionDocente);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return estadoCargaMasivaDto;
	}
	
	private boolean ValidarDocente(@Valid DocenteSaveDto docenteSaveDto) {
		BindingResult bindingResult = new BeanPropertyBindingResult(docenteSaveDto, "docenteSaveDto");
		validator.validate(docenteSaveDto, bindingResult);
		return bindingResult.hasErrors();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<DocenteResponseDto> filtrarDocentes(String terminoBusqueda) {
	
		return docenteRepository
				.findByNombreOrApellidoOrCodigo(terminoBusqueda)
				.stream()
				.map(this::crearDocenteReponseDto).toList();
	}
	
	@Override
	@Transactional
	public void eliminarFisico(Long id) {
		docenteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Docente con id: "+id+" No encontrado"));
		docenteRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void eliminarLogico(Long id) {
		Docente docenteDB = docenteRepository.findById(id)
		.orElseThrow(() -> new ResourceNotFoundException("Docente con id: "+id+" No encontrado"));
		docenteDB.setEstado(EstadoPersona.INACTIVO);
		docenteRepository.save(docenteDB);
	}
	
	private CamposUnicosDocenteDto obtenerCamposUnicos(DocenteSaveDto docenteSaveDto) {
		return informacionUnicaDocente.apply(docenteSaveDto);
	}
	
	private void asignarLineasInvestigacionDocente(Docente docente,List<Long> idsLineaInvestigacion) {
		List<LineaInvestigacion> lineasInvestigacion = BuscarlineasInvestigacionDocente(idsLineaInvestigacion);
		lineasInvestigacion.forEach(li->{
			DocenteLineaInvestigacion docenteLineaInvestigacion = DocenteLineaInvestigacion.builder()
					.docente(docente)
					.lineaInvestigacion(li)
					.build();
			docenteLineaInvestigacionRepository.save(docenteLineaInvestigacion);
		});
	}
	
	private List<LineaInvestigacion> BuscarlineasInvestigacionDocente(List<Long> idsLineaInvestigacion) {
		return lineaInvestigacionRepository.findAllById(idsLineaInvestigacion);
	}
	
	private DocenteResponseDto crearDocenteReponseDto(Docente docente) {
		List<LineaInvestigacionDto> lineasInvestigacion =
				lineaInvestigacionMapper.toDtoList(docenteLineaInvestigacionRepository.findAllLineasInvByIdDocente(docente.getId()));
		DocenteResponseDto docenteResponseDto = DocenteResponseMapper.toDto(docente);
		docenteResponseDto.setLineasInvestigacion(lineasInvestigacion);
		return docenteResponseDto;
	}
	
	private DocenteSaveDto crearDocente(Row rowDocente,Sheet sheetTitulo) {
		
		String codigo= rowDocente.getCell(7)!=null?rowDocente.getCell(7).getStringCellValue():"";
		String facultad= rowDocente.getCell(8).getStringCellValue();
		String departamento= rowDocente.getCell(9).getStringCellValue();
		EscalafonDocente escalafonDocente = obtenerValorEnum(rowDocente, 10, EscalafonDocente.class);
		TipoVinculacion tipoVinculacion = obtenerValorEnum(rowDocente, 11, TipoVinculacion.class);
		
		return DocenteSaveDto.builder()
				.persona(crearPersona(rowDocente))
				.codigo(codigo)
				.facultad(facultad)
				.departamento(departamento)
				.escalafon(escalafonDocente)
				.tipoVinculacion(tipoVinculacion)
				.titulos(listaTitulos(codigo, sheetTitulo,0))
				.build();
	}
	
	private PersonaDto crearPersona(Row rowDocente) {

		return PersonaDto.builder()
				.identificacion((long)rowDocente.getCell(0).getNumericCellValue())
				.nombre(rowDocente.getCell(1)!=null? rowDocente.getCell(1).getStringCellValue():"")
				.apellido(rowDocente.getCell(2)!=null?rowDocente.getCell(2).getStringCellValue():"")
				.correoElectronico(rowDocente.getCell(3)!=null?rowDocente.getCell(3).getStringCellValue():"")
				.telefono(rowDocente.getCell(4).getStringCellValue())
				.genero(obtenerValorEnum(rowDocente, 5, Genero.class))
				.tipoIdentificacion(obtenerValorEnum(rowDocente, 6, TipoIdentificacion.class))
				.build();
	}
	
	private TituloDto crearTitulo(Row rowTitulo) {
		return TituloDto.builder()
				.abreviatura(obtenerValorEnum(rowTitulo, 1, AbreviaturaTitulo.class))
				.universidad(rowTitulo.getCell(2).getStringCellValue())
				.categoriaMinCiencia(obtenerValorEnum(rowTitulo, 3, CategoriaMinCiencia.class))
				.linkCvLac(rowTitulo.getCell(4).getStringCellValue())
				.build();
	}
	
	private List<TituloDto> listaTitulos(String codigoDocente,Sheet sheetTitulo,int indice){
		return filtrarRow(codigoDocente, sheetTitulo,indice).map(this::crearTitulo).toList();
	}
	
	private void cargarLineasInvestigacionDocente(Row row) {
	
			String codigoDocente = row.getCell(0).getStringCellValue();
			Long idLineaInvestigacion =(long)row.getCell(1).getNumericCellValue();
			DocenteLineaInvestigacion docenteLineaInvestigacion = DocenteLineaInvestigacion.builder()
					.lineaInvestigacion(lineaInvestigacionRepository.findById(idLineaInvestigacion).orElse(null))
					.docente(docenteRepository.findByCodigo(codigoDocente))
					.build();
	
			List<DocenteLineaInvestigacion> docenteLineaBD = docenteLineaInvestigacionRepository.findAll().stream().toList();
			if(docenteLineaBD.isEmpty()) {
				docenteLineaInvestigacionRepository.save(docenteLineaInvestigacion);
			}else {
				long count = docenteLineaBD.stream().filter(dl->dl.getDocente().getCodigo().equals(codigoDocente) && dl.getLineaInvestigacion().getId().equals(idLineaInvestigacion)).count();
				if(count==0) {
					docenteLineaInvestigacionRepository.save(docenteLineaInvestigacion);
				}
			}
			
			
	}
	
	private Stream<Row> filtrarRow(String codigoDocente,Sheet sheetLineaInvestigacion,int indice){
		Predicate<Row> codigosIguales = row->row.getCell(indice).getStringCellValue().equals(codigoDocente);
		return StreamSupport.stream(sheetLineaInvestigacion.spliterator(), false).skip(1).filter(codigosIguales);
	}
	
	private <T extends Enum<T>> T obtenerValorEnum(Row row,int indice,Class<T> enumClass) {
		String valorEnum = row.getCell(indice).getStringCellValue();
		return Enum.valueOf(enumClass, valorEnum);
	}
	
	private void actualizarInformacionDocente(Docente docente,Docente docenteBD, List<Long> idsLineaInvDocente) {
		 List<Long> idsLineaInvDocenteBD = docenteLineaInvestigacionRepository.findAllByDocente(docenteBD.getId());

		 List<Long> idsLineasInvDocenteAEliminar = idsLineaInvDocenteBD.stream()
				.filter(IdlineaInvDocenteDB->!idsLineaInvDocente.contains(IdlineaInvDocenteDB)).toList();
		 
		 List<Long> idsLineasInvDocenteAsignar = idsLineaInvDocente.stream()
				.filter(idLineaInvDocente->!idsLineaInvDocenteBD.contains(idLineaInvDocente)).toList();
		
		asignarLineasInvestigacionDocente(docenteBD, idsLineasInvDocenteAsignar);
		docenteLineaInvestigacionRepository.deleteAllById(idsLineasInvDocenteAEliminar);

		docenteBD.setPersona(docente.getPersona());
		docenteBD.setCodigo(docente.getCodigo());
		docenteBD.setFacultad(docente.getFacultad());
		docenteBD.setDepartamento(docente.getDepartamento());
		docenteBD.setEscalafon(docente.getEscalafon());
		docenteBD.setTipoVinculacion(docente.getTipoVinculacion());
		docenteBD.setTitulos(docente.getTitulos());
	}
	
	private Map<String, String> validacionCampoUnicos(CamposUnicosDocenteDto camposUnicos,CamposUnicosDocenteDto camposUnicosBD) {

		Map<String, Function<CamposUnicosDocenteDto, Boolean>> mapCamposUnicos = new HashMap<>();
		
		mapCamposUnicos.put("codigo", dto->(camposUnicosBD == null || !dto.getCodigo().equals(camposUnicosBD.getCodigo())) && docenteRepository.existsByCodigo(dto.getCodigo()));
		mapCamposUnicos.put("identificacion", dto->(camposUnicosBD == null || !dto.getIdentificacion().equals(camposUnicosBD.getIdentificacion())) && personaRepository.existsByIdentificacion(dto.getIdentificacion()));
		mapCamposUnicos.put("correoElectronico", dto->(camposUnicosBD == null || !dto.getCorreoElectronico().equals(camposUnicosBD.getCorreoElectronico())) && personaRepository.existsByCorreoElectronico(dto.getCorreoElectronico()));
			
		Predicate<Field> existeCampoUnico = campo-> mapCamposUnicos.containsKey(campo.getName());
		Predicate<Field> existeCampoBD = campoBD->mapCamposUnicos.get(campoBD.getName()).apply(camposUnicos);
		Predicate<Field> campoInvalido = existeCampoUnico.and(existeCampoBD);
		
		return Arrays.stream(camposUnicos.getClass().getDeclaredFields())
				.filter(campoInvalido)
				.peek(field->field.setAccessible(true))
				.collect(Collectors.toMap(Field::getName,field->{
					Object valorCampo=null;
					try {
						valorCampo=field.get(camposUnicos);
					}catch (IllegalAccessException e) {e.printStackTrace();} 
					return mensajeException(field.getName(),valorCampo);
				}
			));

		}
	
	private <T> String mensajeException(String nombreCampo,T valorCampo) {
	    return "Campo único, ya existe un docente con la información: " + nombreCampo + ": " + valorCampo;
	}

	@Override
	public List<InformacionPersonalDto> listarDocentesActivos(String estado) {
		List<Docente> docentesActivos = docenteRepository.findAllActiveDocentes(EstadoPersona.valueOf(estado));
		return docentesToInformacionPersonal(docentesActivos);
	}

	private List<InformacionPersonalDto> docentesToInformacionPersonal(List<Docente> docentes){
		List<InformacionPersonalDto> iPersonalDtos = docentes.stream()
        .map(docente -> {
			InformacionPersonalDto iPersonalDto = new InformacionPersonalDto();
			Persona persona = docente.getPersona();
			iPersonalDto.setId(docente.getId().intValue());
			iPersonalDto.setNombres(persona.getNombre());
			iPersonalDto.setApellidos(persona.getApellido());
			iPersonalDto.setCorreo(persona.getCorreoElectronico());
			iPersonalDto.setCelular(persona.getTelefono());
			iPersonalDto.setCodigoAcademico(docente.getCodigo());
			iPersonalDto.setTipoDocumento(persona.getTipoIdentificacion().obtenerAbreviatura());
			iPersonalDto.setNumeroDocumento(persona.getIdentificacion().toString());

			return iPersonalDto;
    	})
    	.collect(Collectors.toList());

        return iPersonalDtos;
    }

	@Override
	public InformacionPersonalDto obtenerDocente(String identificador) {
		List<Docente> docentesActivos = new ArrayList<>();
		try {
			Integer id = Integer.parseInt(identificador);
			docentesActivos.add(docenteRepository.findById(id.longValue()).get());	
		} catch (NumberFormatException e) {
			docentesActivos.add(docenteRepository.findByCorreo(identificador));
		}
		return docentesToInformacionPersonal(docentesActivos).get(0);
	}
 
}
