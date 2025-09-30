package com.example.MDW.model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Nosotros {

        @GetMapping("/Nosotros")
        public String mostrarNosotros(Model model) {
                // Textos
                model.addAttribute("resena",
                                "Nuestra academia nació con la visión de acercar la educación de calidad...");
                model.addAttribute("mision", "Brindar formación académica virtual accesible y flexible...");
                model.addAttribute("vision", "Ser reconocidos como la academia virtual líder en Latinoamérica...");

                // Imágenes (rutas dentro de /static/img/nosotros/)
                model.addAttribute("imgResena", "/img/nosotros/historia.jpg");
                model.addAttribute("imgMision", "/img/nosotros/mision.jpg");
                model.addAttribute("imgVision", "/img/nosotros/vision.jpg");

                return "Nosotros"; // se conecta con templates/Nosotros.html
        }
}
