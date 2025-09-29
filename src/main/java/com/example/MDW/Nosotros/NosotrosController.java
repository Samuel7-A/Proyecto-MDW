package com.example.MDW.Nosotros;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NosotrosController {

    @GetMapping("/nosotros")
    public String mostrarNosotros(Model model) {
        model.addAttribute("titulo", "Nosotros - Academia Virtual");
        model.addAttribute("resena", "Nuestra academia nació con la visión de acercar la educación de calidad...");
        model.addAttribute("mision", "Brindar formación académica virtual accesible y flexible...");
        model.addAttribute("vision", "Ser reconocidos como la academia virtual líder en Latinoamérica...");
        return "Nosotros"; // busca nosotros.html en templates/
    }
}
