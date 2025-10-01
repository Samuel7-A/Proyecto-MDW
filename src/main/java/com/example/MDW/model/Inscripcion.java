package com.example.MDW.model;

import java.time.LocalDate;

public class Inscripcion {
    private Long id;         // 🔹 este es el que estamos llenando en el servicio
    private Long courseId;   // id del curso
    private String userId;   // id del usuario
    private LocalDate fecha; // fecha de inscripción


    public Inscripcion() {}

    public Inscripcion(Long courseId, String userId, LocalDate fecha) {
        this.courseId = courseId;
        this.userId = userId;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getCourseId() {return courseId;}
    public void setCourseId(Long courseId) {this.courseId = courseId;}

    public String getUserId() {return userId;}
    public void setUserId(String userId) {this.userId = userId;}

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

}
