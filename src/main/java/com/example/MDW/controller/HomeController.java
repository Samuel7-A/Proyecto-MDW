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
            // 🔹 Vincular el alumno si existe
            Alumno alumno = alumnoService.buscarPorPersonaId(persona.getIdPersona());
            if (alumno != null) {
                persona.setAlumno(alumno); // ahora sí, tu persona tiene su alumno cargado
            }

            Profesor profesor = profesorService.buscarPorPersonaId(persona.getIdPersona());
            if (profesor != null) {
                profesor.setPersona(persona); // fuerza vínculo bidireccional
                persona.setProfesor(profesor);
            }

            session.setAttribute("personaLogueado", persona);
            System.out.println("Persona logueada correctamente: " + persona.getNombre());

            return "redirect:/"; // refresca la página
        }

        model.addAttribute("error", "Credenciales incorrectas o persona no registrada");
        return "index";
    }

    @PostMapping("/register")
    public String register(@RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email,
            @RequestParam String password,

            Model model) {

        Persona nuevo = new Persona(nombre, apellido, email, password);

        Alumno alumno = new Alumno(nuevo);
        nuevo.setAlumno(alumno);
        personaService.registrar(nuevo);

        model.addAttribute("mensaje", "Persona registrada. Ahora puedes iniciar sesión.");
        return "index";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        session.invalidate();
        redirectAttrs.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        return "redirect:/";
    }

    @PostMapping("/convertirProfesor")
    public String convertirProfesor(HttpSession session, Model model) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona != null) {
            // Si aún no tiene un Profesor asociado
            if (persona.getProfesor() == null) {
                Profesor profesor = new Profesor(persona, "Sin especialidad");
                persona.setProfesor(profesor); // Se asocia desde Persona
                personaService.registrar(persona); // Solo se guarda Persona (cascade guarda Profesor también)

            // 🔹 Recarga la persona con su profesor desde la BD
            Persona personaActualizada = personaService.buscarPorId(persona.getIdPersona());
            session.setAttribute("personaLogueado", personaActualizada);

            redirectAttrs.addFlashAttribute("success", "¡Felicidades! Ahora eres profesor.");
        } else {
            redirectAttrs.addFlashAttribute("info", "Ya eres profesor.");
        }

        return "redirect:/";
    }
}