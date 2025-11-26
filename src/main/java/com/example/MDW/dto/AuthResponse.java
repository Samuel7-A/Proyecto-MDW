package com.example.MDW.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private String nombre;
    private String apellido;
    private Long idPersona;
    private boolean isProfesor;
    private boolean isAlumno;
}

