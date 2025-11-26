package com.example.MDW.controller;

import com.example.MDW.model.Persona;
import com.example.MDW.model.Profesor;
import com.example.MDW.service.PersonaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private PersonaService personaService;

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona != null) {
            model.addAttribute("personaLogueado", persona);
        }
        return "index";
    }

    @GetMapping("/index")
    public String pagprincipal(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona != null) {
            model.addAttribute("personaLogueado", persona);
        }
        return "index";
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