package com.example.MDW.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
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

    // 🔹 Nueva relación: muchos cursos pueden ser dictados por un profesor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_profesor") // 👉 Nombre de la columna FK en la tabla curso
    private Profesor profesor;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Curso() {
    }

    public Curso(Long idCurso, String nombre, String descripcion, String imagen, int horas, double precio, String nivel) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.horas = horas;
        this.precio = precio;
        this.nivel = nivel;
    }

    // Getters y setters
    public Long getId() { return idCurso; }
    public void setId(Long idCurso) { this.idCurso = idCurso; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public int getHoras() { return horas; }
    public void setHoras(int horas) { this.horas = horas; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }


}
