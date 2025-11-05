
package com.example.MDW.service;

import com.example.MDW.model.Profesor;
import com.example.MDW.model.Persona;
import com.example.MDW.Repositorio.ProfesorRepository;
import com.example.MDW.Repositorio.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private PersonaRepository personaRepository;

    public void guardar(Profesor profesor) {
        profesorRepository.save(profesor); // aquí se genera el ID
    }

    public Profesor buscarPorPersonaId(Long idPersona) {
        Persona persona = personaRepository.findById(idPersona).orElse(null);
        return persona != null ? profesorRepository.findByPersona(persona) : null;
    }

}
