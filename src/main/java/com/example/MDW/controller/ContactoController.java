
package com.example.MDW.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.MDW.model.Contacto;
import com.example.MDW.service.ContactoService;

@Controller
public class ContactoController {

    @Autowired
    ContactoService contactoserv;

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
        Contacto nuevoRegistro = new Contacto(nombre, apellidoPaterno, apellidoMaterno, distrito, dni, telefono, nivel,
                email);

        contactoserv.guardarContacto(nuevoRegistro);

        redirectAttrs.addFlashAttribute("mensaje",
                "¡Gracias por contactarnos, " + nombre + "! Te responderemos pronto.");

        return "redirect:/";

    }

}
