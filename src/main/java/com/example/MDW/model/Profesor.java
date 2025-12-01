package com.example.MDW.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_profesor")
    private Long idProfesor;

    //Relación con Persona (FK)
    @OneToOne
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona") // FK a la tabla Persona
    private Persona persona;

    private String especialidad;

    //un profesor puede dictar muchos cursos
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Curso> cursos;


    public Profesor(Persona persona, String especialidad) {
        this.persona = persona;
        this.especialidad = especialidad;
    }

    // Getters y Setters
    public Long getId() { return idProfesor;}

    public void setId(Long idProfesor) { this.idProfesor = idProfesor;}

}