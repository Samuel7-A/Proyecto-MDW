package com.example.MDW.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.Persona;
import com.example.MDW.model.Profesor;
import com.example.MDW.service.PersonaService;
import com.example.MDW.service.AlumnoService;
import com.example.MDW.service.ProfesorService;
import com.example.MDW.service.InscripcionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping

public class ProfesorController {
    @Autowired
    private ProfesorService profesorService;

    @Autowired
    private PersonaService personaService;

    @GetMapping("/gestion-cursos")
    public String gestionCursos(Model model) {
        return "gestion-cursos";
    }

    @PostMapping("/convertirProfesor")
    public String convertirProfesor(HttpSession session, RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para realizar esta acción.");
            return "redirect:/";
        }

        if (persona.getProfesor() == null) {
            Profesor profesor = new Profesor(persona, "Sin especialidad");
            persona.setProfesor(profesor);
            // ✅ Cambiar rol a ADMIN
            persona.setRol("ROLE_ADMIN");
            personaService.registrar(persona);

            // Actualizar sesión
            session.setAttribute("personaLogueado", persona);

            redirectAttrs.addFlashAttribute("success", "¡Felicidades! Ahora eres profesor.");
        } else {
            redirectAttrs.addFlashAttribute("info", "Ya eres profesor.");
        }
        return "redirect:/";
    }
}
