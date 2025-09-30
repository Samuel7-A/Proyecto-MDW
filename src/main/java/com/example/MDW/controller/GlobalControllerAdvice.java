
package com.example.MDW.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.MDW.model.Usuario;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if(usuario != null) {
            model.addAttribute("usuarioLogueado", usuario);
        }
        
    }
}


