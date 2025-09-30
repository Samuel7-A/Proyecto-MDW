package com.example.MDW.controller;

import com.example.MDW.model.Curso;
import com.example.MDW.model.Registro;
import com.example.MDW.service.CursoService;
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

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public String listarCursos(Model model) {
        List<Curso> cursos = cursoService.listarCursos();
        model.addAttribute("cursos", cursos);
        return "cursos"; // template: src/main/resources/templates/cursos.html
    }

    
    /**
     * Recibe el formulario desde el modal.
     * Espera: courseId (hidden), userId, registrationDate (YYYY-MM-DD desde <input type="date">)
     */
    @PostMapping("/registrar")
    public String registrarCurso(
            @RequestParam("courseId") Long courseId,
            @RequestParam("userId") String userId,
            @RequestParam("registrationDate") String registrationDate,
            RedirectAttributes redirectAttrs) {

        try {
            LocalDate fecha = LocalDate.parse(registrationDate);
            Registro registro = cursoService.registrarCurso(courseId, userId, fecha);
            redirectAttrs.addFlashAttribute("success", "Registro exitoso. Id: " + registro.getId());
        } catch (DateTimeParseException ex) {
            redirectAttrs.addFlashAttribute("error", "Fecha inválida. Usa el formato correcto.");
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("error", "Ocurrió un error al registrar. Intenta nuevamente.");
        }

        return "redirect:/cursos";
    }
}
