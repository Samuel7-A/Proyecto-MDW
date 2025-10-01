
package com.example.MDW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactoController {

    @PostMapping("/contacto")
    public String contacto(@RequestParam String nombre,
                           @RequestParam String apellidoPaterno,
                           @RequestParam String apellidoMaterno,
                           @RequestParam String distrito,
                           @RequestParam String dni,
                           @RequestParam String telefono,
                           @RequestParam String nivel,
                           @RequestParam String email,
                           RedirectAttributes redirectAttrs) {

        redirectAttrs.addFlashAttribute("mensaje",
                "¡Gracias por contactarnos, " + nombre + "! Te responderemos pronto.");

        return "redirect:/";
        
    }
}
