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
}