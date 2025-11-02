
package com.example.MDW.controller;

import com.example.MDW.model.Alumno;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Persona;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.MDW.service.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if(persona != null) {
            model.addAttribute("personaLogueado", persona);
        }
        
    }

    @Autowired
    private InscripcionService inscripcionService;

    @ModelAttribute("cursosInscritosSidebar")
    public List<Curso> cursosInscritosSidebar(HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona != null && persona.getAlumno() != null) {
            Alumno alumno = persona.getAlumno();
            return inscripcionService.obtenerCursosPorAlumno(alumno);
        }
        return Collections.emptyList();
    }
}


