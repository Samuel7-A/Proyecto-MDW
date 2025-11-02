
package com.example.MDW.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Contacto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String distrito;
    private String dni;
    private String telefono;
    private String nivel;
    private String email;


    public Contacto(String nombre, String apellidoPaterno, String apellidoMaterno,
            String distrito, String dni, String telefono, String nivel, String email) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.distrito = distrito;
        this.dni = dni;
        this.telefono = telefono;
        this.nivel = nivel;
        this.email = email;
    }
}
