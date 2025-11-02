package com.example.MDW.controller;

import com.example.MDW.Repositorio.HorarioRepository; // minúscula
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    // ✅ Mostrar todos los horarios
    @GetMapping("/horarios")
    public String listarHorarios(Model model) {
        model.addAttribute("horarios", horarioRepository.findAll());
        return "horarios";
    }

    // ✅ Mostrar horarios solo del lunes
    @GetMapping("/horarios/lunes")
    public String horariosLunes(Model model) {
        model.addAttribute("horarios", horarioRepository.findByDiaSemana("Lunes"));
        return "horarios";
    }
}
