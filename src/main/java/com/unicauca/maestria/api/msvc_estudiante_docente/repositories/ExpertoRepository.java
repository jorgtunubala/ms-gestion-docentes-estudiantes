package com.unicauca.maestria.api.msvc_estudiante_docente.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;
import com.unicauca.maestria.api.msvc_estudiante_docente.domain.Experto;


public interface ExpertoRepository extends JpaRepository<Experto, Long>{

    // @Query("SELECT e FROM Experto e join e.persona p WHERE p.identificacion = ?1")
    // public boolean existsByIdentificacion(long identificacion);

    public Experto findByPersona(long idPersona);
    
    @Query("SELECT e FROM Experto e join e.persona p WHERE p.identificacion = ?1")
    public Experto findByIdentificacion(long identificacion);

    @Query("SELECT e FROM Experto e join e.persona p WHERE "
            + "p.nombre LIKE %?1% OR p.identificacion LIKE %?1% OR p.apellido LIKE %?1%")
    public List<Experto> findByNombreOrApellidoOrIdentificacion(String termino);

    public boolean existsByPersona(long idPersona);

    @Query("SELECT e FROM Experto e join e.persona p WHERE "
            + "e.estado = ?1")
    public List<Experto> findAllActiveExperto(EstadoPersona estado);

    @Query("""
            SELECT e FROM Experto e 
            JOIN e.persona p 
            WHERE p.correoElectronico = ?1
            """)
    public Experto findByCorreo(String correo);
    
}
