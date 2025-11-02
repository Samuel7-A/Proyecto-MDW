
package com.example.MDW.service;

import com.example.MDW.model.Profesor;
import com.example.MDW.Repositorio.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepo;

    public void guardar(Profesor profesor) {
        profesorRepo.save(profesor); // aquí se genera el ID
    }

}
