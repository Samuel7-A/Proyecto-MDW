package com.example.MDW.controller;

import com.example.MDW.model.Alumno;
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

    @PostMapping("/inscribirse")
    public String inscribirse(@RequestParam Long courseId,
                              @RequestParam String registrationDate,
                              HttpSession session,
                              RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null || persona.getAlumno() == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para inscribirte en un curso.");
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        Curso curso = cursoService.findById(courseId);
        if (curso == null) {
            redirectAttrs.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/cursos";
        }

        // 🔹 Validación de duplicado antes de registrar
        if (inscripcionService.existeInscripcion(alumno.getId(), courseId)) {
            redirectAttrs.addFlashAttribute("error", "Ya estás inscrito en este curso.");
            return "redirect:/cursos";
        }

        LocalDate fecha = LocalDate.parse(registrationDate);

        

        inscripcionService.registrar(alumno, curso, fecha);

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

        Alumno alumno = persona.getAlumno();

        boolean eliminado = inscripcionService.eliminarInscripcion(alumno.getId(), courseId);

        if (eliminado) {
            redirectAttrs.addFlashAttribute("success", "Te has desinscrito del curso correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No estabas inscrito en este curso.");
        }

        return "redirect:/cursos/mis-cursos";
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
    if (persona != null && persona.getAlumno() != null) {
        Alumno alumno = persona.getAlumno();
        List<Curso> cursosInscritos = inscripcionService.obtenerCursosPorAlumno(alumno);
        model.addAttribute("cursosInscritosSidebar", cursosInscritos);
    } else {
        model.addAttribute("cursosInscritosSidebar", List.of());
    }

    return "cursos"; // template: src/main/resources/templates/cursos.html
}



    // 🔹 Mostrar los cursos en los que el alumno está inscrito
    @GetMapping("/mis-cursos")
    public String misCursos(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();

        List<Curso> cursos = inscripcionService.obtenerCursosPorAlumno(alumno);
        model.addAttribute("cursos", cursos);
        return "mis-cursos";
    }
}
