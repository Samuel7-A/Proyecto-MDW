package com.example.MDW.service;

import com.example.MDW.model.Curso;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CursoService {

    private final List<Curso> cursos = new ArrayList<>();

    public CursoService() {
        cursos.add(new Curso(1L, "Introducción a la Ciberseguridad",
                "Explora el emocionante campo de la ciberseguridad y crece con nosotros",
                "curso1.jpg", 20, 0.0, "Básico"));

        cursos.add(new Curso(2L, "Redes y Conectividad",
                "Aprende los fundamentos de redes, protocolos y conectividad",
                "curso2.jpg", 30, 50.0, "Intermedio"));

        cursos.add(new Curso(3L, "Seguridad en Redes Avanzada",
                "Domina técnicas avanzadas para proteger infraestructuras críticas",
                "curso3.jpg", 40, 75.0, "Avanzado"));

        cursos.add(new Curso(4L, "Seguridad en Aplicaciones Web",
                "Aprende a proteger aplicaciones web frente a ataques como XSS, CSRF y SQL Injection",
                "curso4.jpg", 35, 70.0, "Intermedio"));

        cursos.add(new Curso(5L, "Hacking Ético y Pentesting",
                "Realiza pruebas de penetración y evalúa vulnerabilidades en sistemas reales",
                "curso5.jpg", 55, 120.0, "Avanzado"));
    }

    // 🔹 Listar todos los cursos
    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos); // copia para evitar modificaciones externas
    }

    // 🔹 Buscar curso por ID
    public Curso findById(Long id) {
        return listarCursos().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
