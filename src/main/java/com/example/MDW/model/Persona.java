    package com.example.MDW.model;

    import java.time.LocalDate;

    import jakarta.persistence.*;

    @Entity
    public class Persona {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_persona") // nombre explícito de la columna
        private Long idPersona;

        private String nombre;
        private String apellido;
        private String email;
        private String password;
        private LocalDate fecha_creacion = LocalDate.now();

        // 🔹 Relación bidireccional con Alumno
        @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
        private Alumno alumno;

        // 🔹 Relación bidireccional con Profesor
        @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL)
        private Profesor profesor;

        public Persona(String nombre, String apellido, String email, String password ){
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.password = password;
        }

        public Persona() {
        }

        // Getters y setters    
        public Long getIdPersona() {
            return idPersona;
        }
        public void setIdPersona(Long idPersona) {
            this.idPersona = idPersona;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getApellido() {
            return apellido;
        }
        public void setApellido(String apellido) {
            this.apellido = apellido;
        }
        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public LocalDate getFecha_creacion() {
            return fecha_creacion;
        }
        public void setFecha_creacion(LocalDate fecha_creacion) {
            this.fecha_creacion = fecha_creacion;
        }

        public Alumno getAlumno() {
            return alumno;
        }

        public void setAlumno(Alumno alumno) {
            this.alumno = alumno;
        }

        public Profesor getProfesor() {
            return profesor;
        }

        public void setProfesor(Profesor profesor) {
            this.profesor = profesor;
        }

    }

