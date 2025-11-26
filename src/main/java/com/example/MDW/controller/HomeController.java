package com.example.MDW.controller;

import com.example.MDW.model.Persona;
import com.example.MDW.model.Profesor;
import com.example.MDW.service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional; // Importación necesaria para el helper

@Controller
public class HomeController {

    @Autowired
    private PersonaService personaService;

    // 🔄 METODO AUXILIAR para obtener la Persona completa desde Spring Security
    private Optional<Persona> getAuthenticatedPersona() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 1. Verificar si hay autenticación válida
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        // 2. Extraer el username/email
        String email;
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        // 3. Buscar la Persona completa en la base de datos (con relaciones)
        return Optional.ofNullable(personaService.buscarPorEmail(email));
    }

    // --- Endpoints de Navegación (Corregidos) ---

    @GetMapping("/")
    public String index(Model model) {
        // Cargar la persona autenticada y añadirla al modelo
        getAuthenticatedPersona().ifPresent(persona -> {
            model.addAttribute("personaLogueado", persona);
            // Si la vista necesita los cursos inscritos del alumno, se deberían cargar aquí también,
            // pero para el index, con la persona basta para el navbar.
        });

        return "index";
    }

    @GetMapping("/index")
    public String pagprincipal(Model model) {
        // El mismo proceso que en '/'
        getAuthenticatedPersona().ifPresent(persona -> {
            model.addAttribute("personaLogueado", persona);
        });
        return "index";
    }

    // --- Endpoint de Lógica de Negocio (Mantenido y Funcional) ---

    @PostMapping("/convertirProfesor")
    public String convertirProfesor(RedirectAttributes redirectAttrs) {

        // Usar el helper para obtener la persona logueada
        Optional<Persona> personaOpt = getAuthenticatedPersona();

        if (personaOpt.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para realizar esta acción.");
            return "redirect:/";
        }

        Persona persona = personaOpt.get();

        if (persona.getProfesor() == null) {

            // Lógica para crear y asociar al profesor
            Profesor profesor = new Profesor(persona, "Sin especialidad");
            persona.setProfesor(profesor);
            personaService.registrar(persona); // Guarda cambios

            redirectAttrs.addFlashAttribute("success", "¡Felicidades! Ahora eres profesor.");
        } else {
            redirectAttrs.addFlashAttribute("info", "Ya eres profesor.");
        }

        // Nota: La redirección a "/" recargará la persona en el modelo gracias a los métodos index() que corregimos.
        return "redirect:/";
    }
}