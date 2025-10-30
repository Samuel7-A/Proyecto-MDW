package com.example.MDW.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;

@Entity
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "persona_id")
    private Persona persona;

    public Alumno() {
    }

    public Alumno(Persona persona) {
        this.persona = persona;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Persona getPersona() {
        return persona;
    }
    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    
}