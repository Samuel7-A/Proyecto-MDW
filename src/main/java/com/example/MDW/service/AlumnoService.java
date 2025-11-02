
package com.example.MDW.service;

import com.example.MDW.model.Alumno;
import com.example.MDW.Repositorio.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    public Alumno buscarPorPersonaId(Long idPersona) {
        return alumnoRepository.findByPersonaIdPersona(idPersona);
    }

    public void guardar(Alumno alumno) {
        alumnoRepository.save(alumno); // aquí se genera el ID
    }

}

