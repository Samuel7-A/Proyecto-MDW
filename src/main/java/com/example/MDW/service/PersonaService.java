package com.example.MDW.service;

import com.example.MDW.Repositorio.PersonaRepository;
import com.example.MDW.model.Persona;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public void registrar(Persona persona) {
        personaRepository.save(persona); // aquí se genera el ID
    }

    public Persona login(String email, String password) {
        return personaRepository.findFirstByEmailAndPassword(email, password)
                .orElseThrow(() -> new RuntimeException("Usuario o contraseña incorrectos"));
    }

}
