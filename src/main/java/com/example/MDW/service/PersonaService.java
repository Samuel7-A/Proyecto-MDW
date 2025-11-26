package com.example.MDW.service;

import com.example.MDW.Repositorio.PersonaRepository;
import com.example.MDW.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    // Registrar con contraseña encriptada
    public Persona registrar(Persona persona) {
        // Encriptar la contraseña antes de guardar
        persona.setPassword(passwordEncoder.encode(persona.getPassword()));
        return personaRepository.save(persona);
    }

    // Login verificando con BCrypt
    public Persona login(String email, String password) {
        Persona persona = personaRepository.findByEmail(email);

        if (persona != null && passwordEncoder.matches(password, persona.getPassword())) {
            return persona;
        }

        return null;
    }

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id).orElse(null);
    }

    public Persona buscarPorEmail(String email) {
        return personaRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return personaRepository.existsByEmail(email);
    }

}
