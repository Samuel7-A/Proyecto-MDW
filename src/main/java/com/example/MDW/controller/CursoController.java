package com.example.MDW.controller;

import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import com.example.MDW.model.Persona;
import com.example.MDW.service.CursoService;
import com.example.MDW.service.InscripcionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final InscripcionService inscripcionService;

    @Autowired
    public CursoController(CursoService cursoService, InscripcionService inscripcionService) {
        this.cursoService = cursoService;
        this.inscripcionService = inscripcionService;
    }

    // 🔹 Listar cursos
  @GetMapping
public String listarCursos(Model model, HttpSession session) {
    List<Curso> cursos = cursoService.listarCursos();
    model.addAttribute("cursos", cursos);

    // 🔹 Obtener persona logueada desde sesión
    Persona persona = (Persona) session.getAttribute("personaLogueado");
    model.addAttribute("personaLogueado", persona);

    // 🔹 Mostrar cursos inscritos en el sidebar si hay sesión activa
    if (persona != null) {
        List<Curso> cursosInscritos = inscripcionService.obtenerCursosPorPersona(persona.getIdPersona());
        model.addAttribute("cursosInscritosSidebar", cursosInscritos);
    } else {
        model.addAttribute("cursosInscritosSidebar", List.of());
    }

    return "cursos"; // template: src/main/resources/templates/cursos.html
}


    @PostMapping("/registrar")
    public String registrarCurso(
            @RequestParam("courseId") Long courseId,
            @RequestParam("registrationDate") String registrationDate,
            HttpSession session,
            RedirectAttributes redirectAttrs) {

        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para inscribirte.");
            return "redirect:/cursos";
        }

        try {
            LocalDate fecha = LocalDate.parse(registrationDate);

            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setCourseId(courseId);
            inscripcion.setUserId(persona.getIdPersona());
            inscripcion.setFecha(fecha);

            inscripcionService.registrar(inscripcion);

            redirectAttrs.addFlashAttribute("success",
                    "Te has inscrito correctamente al curso con ID: " + courseId);

        } catch (DateTimeParseException ex) {
            redirectAttrs.addFlashAttribute("error", "Fecha inválida. Usa el formato correcto.");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", "Ocurrió un error al registrar. Intenta nuevamente.");
        }

        return "redirect:/cursos";
    }

    @GetMapping("/mis-cursos")
    public String misCursos(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null) {
            return "redirect:/cursos";
        }
        List<Curso> cursos = inscripcionService.obtenerCursosPorPersona(persona.getIdPersona());
        model.addAttribute("cursos", cursos);
        return "mis-cursos";
    }
}
