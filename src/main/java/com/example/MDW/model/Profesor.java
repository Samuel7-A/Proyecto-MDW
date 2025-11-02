package com.example.MDW.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Long idProfesor;

    // 🔹 Relación con Persona (FK)
    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona") // FK a la tabla Persona
    private Persona persona;

    private String especialidad;

    // 🔹 Un profesor puede dictar muchos cursos
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> cursos;

    public Profesor() {}

    public Profesor(Persona persona, String especialidad) {
        this.persona = persona;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Long getId() { 
        return idProfesor; 
    }

    public void setId(Long idProfesor) { 
        this.idProfesor = idProfesor; 
    }

    public Persona getPersona() { 
        return persona; 
    }

    public void setPersona(Persona persona) { 
        this.persona = persona; 
    }

    public String getEspecialidad() { 
        return especialidad; 
    }

    public void setEspecialidad(String especialidad) { 
        this.especialidad = especialidad; 
    }

    public List<Curso> getCursos() { 
        return cursos; 
    }

    public void setCursos(List<Curso> cursos) { 
        this.cursos = cursos; 
    }
}