package com.example.MDW.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Long idCurso;

    private String nombre;
    private String descripcion;
    private String imagen;
    private int horas; //Cambiar a duracion
    private double precio;   // nuevo campo
    private String nivel;    // nuevo campo (ej: Básico, Intermedio, Avanzado)

    // Nueva relación: muchos cursos pueden ser dictados por un profesor
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_profesor") // Nombre de la columna FK en la tabla curso
    private Profesor profesor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Curso(Long idCurso, String nombre, String descripcion,
                String imagen, int horas, double precio, String nivel,
                Profesor profesor) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.horas = horas;
        this.precio = precio;
        this.nivel = nivel;
        this.profesor = profesor;
    }

    public Long getId() { return idCurso; }
    public void setId(Long idCurso) { this.idCurso = idCurso; }


}
