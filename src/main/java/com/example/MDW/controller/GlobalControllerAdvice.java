
package com.example.MDW.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Usuario;
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
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if(usuario != null) {
            model.addAttribute("usuarioLogueado", usuario);
        }
        
    }

    @Autowired
    private InscripcionService inscripcionService;

    @ModelAttribute("cursosInscritosSidebar")
    public List<Curso> cursosInscritosSidebar(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario != null) {
            return inscripcionService.obtenerCursosPorUsuario(usuario.getIdUsuario());
        }
        return Collections.emptyList();
    }
}


