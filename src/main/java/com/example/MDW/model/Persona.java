package com.example.MDW.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Persona {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona") // nombre explícito de la columna
    private Long idPersona;

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private LocalDate fecha_creacion = LocalDate.now();
    private String rol;

    // 🔹 Relación bidireccional con Alumno
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Alumno alumno;

    // 🔹 Relación bidireccional con Profesor
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
    private Profesor profesor;

    public Persona(String nombre, String apellido, String email, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }
}

