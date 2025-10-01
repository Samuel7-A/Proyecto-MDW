package com.example.MDW.model;

public class Curso {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private int horas;
    private double precio;   // nuevo campo
    private String nivel;    // nuevo campo (ej: Básico, Intermedio, Avanzado)

    public Curso() {
    }

    public Curso(Long id, String nombre, String descripcion, String imagen, int horas, double precio, String nivel) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.horas = horas;
        this.precio = precio;
        this.nivel = nivel;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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
}
