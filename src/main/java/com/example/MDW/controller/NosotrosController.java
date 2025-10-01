package com.example.MDW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NosotrosController {

        @GetMapping("/Nosotros")
        public String mostrarNosotros(Model model) {
                // Textos
                model.addAttribute("resena",
                                "Nuestra academia nació con la visión de acercar la educación de calidad...");
                model.addAttribute("mision", "Brindar formación académica virtual accesible y flexible...");
                model.addAttribute("vision", "Ser reconocidos como la academia virtual líder en Latinoamérica...");

                // Imágenes (rutas dentro de /static/img/nosotros/)
                model.addAttribute("imgResena", "/img/historia.jpg");
                model.addAttribute("imgMision", "/img/mision.jpg");
                model.addAttribute("imgVision", "/img/vision.jpg");
                model.addAttribute("imgCarrusel1", "/img/conferencia.jpg");
                model.addAttribute("imgCarrusel2", "/img/feria_digital.jpg");
                model.addAttribute("imgCarrusel3", "/img/taller_practico.jpg");

                return "Nosotros"; // se conecta con templates/Nosotros.html
        }
}
