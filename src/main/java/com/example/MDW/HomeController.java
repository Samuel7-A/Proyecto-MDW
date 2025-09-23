
package com.example.MDW;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "index.html"; // Spring buscará templates/index.html
    }

    @GetMapping("/cursos")
    public String cursos() {
        return "cursos";
    }

    @GetMapping("/niveles")                 
    public String niveles() {
        return "niveles";
    }

    @GetMapping("/Nosotros")
    public String Nosotros() {
        return "Nosotros";
    }

    @GetMapping("/index")
    public String pagprincipal() {
        return "index";
    }

}





