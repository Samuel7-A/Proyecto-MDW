package com.example.MDW.model;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Nosotros {

        @GetMapping("/nosotros")
        public String mostrarNosotros(Model model) {
                model.addAttribute("titulo", "Nosotros - Academia Virtual");
                model.addAttribute("resena",
                                "Nuestra academia nació con la visión de acercar la educación de calidad a estudiantes de todos \r\n");
                model.addAttribute("mision",
                                "Brindar formación académica virtual de calidad, accesible y flexible para estudiantes \r\n");
                model.addAttribute("vision", "Ser reconocidos como la ");
                return "Nosotros"; // busca nosotros.html en templates/
        }
}
