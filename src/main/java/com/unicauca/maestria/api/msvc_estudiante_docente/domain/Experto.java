package com.unicauca.maestria.api.msvc_estudiante_docente.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.unicauca.maestria.api.msvc_estudiante_docente.common.enums.EstadoPersona;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Entity @Table(name = "expertos")
public class Experto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    private String tituloexper;

    private String universidadtitexp;

    private String copiadocidentidad;

    private String universidadexp;

    private String facultadexp;

    private String grupoinvexp;

    private String lineainvexp;

    private String observacionexp;

    @Enumerated(EnumType.STRING)
    private EstadoPersona estado;

    public Experto() {
        estado = EstadoPersona.ACTIVO;
    }   



    
}
