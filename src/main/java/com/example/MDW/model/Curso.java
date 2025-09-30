package com.example.MDW.model;

public class Curso {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private int horas; // nuevo campo

    // Constructor vacío (requerido por Spring y JPA si usas persistencia después)
    public Curso() {
    }

    // Constructor con parámetros (incluye horas)
    public Curso(Long id, String nombre, String descripcion, String imagen, int horas) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.horas = horas;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }
}
