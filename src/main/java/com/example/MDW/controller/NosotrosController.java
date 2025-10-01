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
                                "Nuestra academia nació con la visión de acercar la educación de calidad a estudiantes de todos \r\n"
                                                + //
                                                "los niveles. Iniciamos como un pequeño grupo de docentes apasionados por la enseñanza, desarrollando \r\n"
                                                + //
                                                "cursos virtuales para escolares de primaria y secundaria. Con el tiempo, expandimos nuestra oferta a programas \r\n"
                                                + //
                                                "preuniversitarios y universitarios, consolidándonos como una plataforma innovadora que combina tecnología y pedagogía \r\n"
                                                + //
                                                "moderna. Hoy, formamos una comunidad educativa que aprende sin fronteras y a su propio ritmo.");
                model.addAttribute("mision",
                                "Brindar formación académica virtual de calidad, accesible y flexible para estudiantes \r\n"
                                                + //
                                                "de primaria, secundaria, academia y universidad, utilizando recursos tecnológicos modernos y \r\n"
                                                + //
                                                "estrategias didácticas innovadoras que potencien el aprendizaje autónomo.");
                model.addAttribute("vision",
                                "Ser reconocidos como la academia virtual líder en Latinoamérica, formando estudiantes competentes, \r\n"
                                                + //
                                                "críticos y creativos, capaces de desenvolverse con éxito en entornos académicos y profesionales, \r\n"
                                                + //
                                                "contribuyendo al desarrollo de la sociedad.");

                // Imágenes (rutas dentro de /static/img/nosotros/)
                model.addAttribute("imgResena", "/img/historia.jpg");
                model.addAttribute("imgMision", "/img/mision.jpg");
                model.addAttribute("imgVision", "/img/vision.jpg");
                model.addAttribute("imgCarrusel1", "/img/conferencia.jpg");
                model.addAttribute("imgCarrusel2", "/img/feria_digital.jpg");
                model.addAttribute("imgCarrusel3", "/img/taller_practico.jpg");

                model.addAttribute("textoCarrusel1",
                                "Conferencias Virtuales: Charlas gratuitas dictadas por especialistas que abordan temas de actualidad académica y profesional.");
                model.addAttribute("textoCarrusel2",
                                "Feria Educativa Digital: Espacio virtual donde los estudiantes exploran cursos, talleres y programas de diferentes áreas del conocimiento.");
                model.addAttribute("textoCarrusel3",
                                "Semana de Talleres Prácticos: Jornadas interactivas con actividades en línea para reforzar competencias en ciencia, tecnología y humanidades.");

                return "Nosotros"; // se conecta con templates/Nosotros.html
        }
}
