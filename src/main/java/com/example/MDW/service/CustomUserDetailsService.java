package com.example.MDW.service;

import com.example.MDW.Repositorio.PersonaRepository;
import com.example.MDW.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonaRepository personaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        String rol = persona.getProfesor() != null ? "ROLE_ADMIN" : "ROLE_USER";

        // Retorna un UserDetails de Spring Security
        return User.builder()
                .username(persona.getEmail())
                .password(persona.getPassword())
                .authorities(rol) // Puedes agregar roles aquí
                .build();
    }

    // Metodo adicional para obtener la Persona completa
    public Persona getPersonaByEmail(String email) {
        return personaRepository.findByEmail(email);
    }
}

