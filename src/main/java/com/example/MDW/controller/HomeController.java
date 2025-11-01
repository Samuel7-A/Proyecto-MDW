package com.example.MDW.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.MDW.model.Persona;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import com.example.MDW.service.PersonaService;
import com.example.MDW.service.CursoService;
import com.example.MDW.service.InscripcionService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    private final PersonaService personaService;
    private final CursoService cursoService;
    private final InscripcionService inscripcionService;

    public HomeController(PersonaService personaService, CursoService cursoService, InscripcionService inscripcionService) {
        this.personaService = personaService;
        this.cursoService = cursoService;
        this.inscripcionService = inscripcionService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/index")
    public String pagprincipal() {
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        Persona persona = personaService.login(email, password);
        if (persona != null) {
            session.setAttribute("personaLogueado", persona);
            System.out.println("Persona logueada correctamente:");
     
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

    @PostMapping("/inscribirse")
    public String inscribirse(@RequestParam Long courseId,
                              @RequestParam String registrationDate,
                              HttpSession session,
                              RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para inscribirte en un curso.");
            return "redirect:/cursos";
        }

        Curso curso = cursoService.findById(courseId);
        if (curso == null) {
            redirectAttrs.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/cursos";
        }

        //  Validación de inscripción duplicada
        if (inscripcionService.existeInscripcion(persona.getIdPersona(), courseId)) {
            redirectAttrs.addFlashAttribute("error", "Ya estás inscrito en este curso.");
            return "redirect:/cursos";
        }

        LocalDate fecha = LocalDate.parse(registrationDate);

        inscripcionService.registrar(courseId, persona.getIdPersona(), fecha);

        redirectAttrs.addFlashAttribute("success", "Inscripción realizada con éxito.");
        return "redirect:/cursos";
    }

    @PostMapping("/desinscribirse")
    public String desinscribirse(@RequestParam Long courseId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para gestionar tus cursos.");
            return "redirect:/cursos";
        }

        boolean eliminado = inscripcionService.eliminarInscripcion(persona.getIdPersona(), courseId);

        if (eliminado) {
            redirectAttrs.addFlashAttribute("success", "Te has desinscrito del curso correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No estabas inscrito en este curso.");
        }

        return "redirect:/cursos/mis-cursos";
    }


}
