package com.example.MDW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NivelesController {

    @GetMapping("/niveles")
    public String mostrarNiveles(Model model) {
        // Colegio
        model.addAttribute("colegioTitulo", "Colegio");
        model.addAttribute("colegioDesc", "CCursos pensados para reforzar tus conocimientos básicos y preparar a los estudiantes para etapas superiores.");
        model.addAttribute("colegioDetalle", "En el nivel de Colegio encontrarás cursos de matemáticas, ciencias, lenguaje y más. Estos cursos están diseñados para complementar lo que aprendes en clase, fortalecer tus habilidades y fomentar tu curiosidad. Ideal para estudiantes de primaria y secundaria que buscan mejorar su rendimiento académico.");
        model.addAttribute("imgColegio", "/img/colegio.jpg");

        // Academia
        model.addAttribute("academiaTitulo", "Academia");
        model.addAttribute("academiaDesc", "Programas de preparación intensiva para exámenes, competencias y habilidades especializadas.");
        model.addAttribute("academiaDetalle", "El nivel Academia ofrece cursos de preparación para exámenes de admisión, competencias académicas y talleres especializados. Los estudiantes reciben material enfocado y guías prácticas para superar pruebas y mejorar su desempeño, con instructores calificados y recursos interactivos.");
        model.addAttribute("imgAcademia", "/img/academia.jpg");

        // Universidad
        model.addAttribute("universidadTitulo", "Universidad");
        model.addAttribute("universidadDesc", "Cursos avanzados para complementar tu formación universitaria y potenciar tu carrera profesional.");
        model.addAttribute("universidadDetalle", "En el nivel Universidad encontrarás cursos diseñados para estudiantes y profesionales que quieren profundizar en su área de estudio o adquirir nuevas habilidades. Aquí puedes encontrar desde programación, negocios y diseño hasta cursos de investigación y desarrollo profesional. Cada curso está pensado para ayudarte a destacar en tu carrera.");
        model.addAttribute("imgUniversidad", "/img/universidad.jpg");

        return "niveles"; 
    }
}
