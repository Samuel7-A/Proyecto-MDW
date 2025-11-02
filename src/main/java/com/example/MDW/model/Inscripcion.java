package com.example.MDW.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incripcion")
    private Long idInscripcion;         // 🔹 este es el que estamos llenando en el servicio

    @Column(name = "fecha_inscripcion")
    private LocalDate fecha; // fecha de inscripción

    public enum EstadoInscripcion { PENDIENTE, APROBADA, CANCELADA }
    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado = EstadoInscripcion.PENDIENTE;

    // 🔹 Muchas inscripciones pertenecen a un curso
    @ManyToOne
    @JoinColumn(name = "id_curso")
    private Curso curso;

    // 🔹 Muchas inscripciones pertenecen a un alumno
    @ManyToOne
    @JoinColumn(name = "id_alumno")
    private Alumno alumno;

    public Inscripcion(Curso curso, Alumno alumno, LocalDate fecha, EstadoInscripcion estado) {
        this.curso = curso;
        this.alumno = alumno;
        this.fecha = fecha;
        this.estado = estado;
    }

}
