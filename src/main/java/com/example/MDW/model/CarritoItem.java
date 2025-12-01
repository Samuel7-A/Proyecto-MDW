package com.example.MDW.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_item")
    private Long idCarritoItem;

    @ManyToOne
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "fecha_agregado")
    private LocalDate fechaAgregado;

    public CarritoItem(Alumno alumno, Curso curso, LocalDate fechaAgregado) {
        this.alumno = alumno;
        this.curso = curso;
        this.fechaAgregado = fechaAgregado;
    }
}