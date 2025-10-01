package com.example.MDW.service;

import com.example.MDW.model.Curso;
import com.example.MDW.model.Registro;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CursoService {

    private final List<Curso> cursos = new ArrayList<>();
    private final List<Registro> registros = new ArrayList<>();
    private final AtomicLong registroIdSeq = new AtomicLong(1);

    public CursoService() {
        cursos.add(new Curso(1L, "Introducción a la Ciberseguridad",
                "Explora el emocionante campo de la ciberseguridad y crece con nosotros", "curso1.jpg", 20, 0.0, "Básico"));
        cursos.add(new Curso(2L, "Redes y Conectividad",
                "Aprende los fundamentos de redes, protocolos y conectividad", "curso2.jpg", 30, 50.0, "Intermedio"));
        cursos.add(new Curso(3L, "Seguridad en Redes Avanzada",
                "Domina técnicas avanzadas para proteger infraestructuras críticas", "curso3.jpg", 40, 75.0, "Avanzado"));
        cursos.add(new Curso(4L, "Seguridad en Aplicaciones Web",
                "Aprende a proteger aplicaciones web frente a ataques como XSS, CSRF y SQL Injection", "curso4.jpg", 35, 70.0, "Intermedio"));
        cursos.add(new Curso(5L, "Hacking Ético y Pentesting",
                "Realiza pruebas de penetración y evalúa vulnerabilidades en sistemas reales", "curso5.jpg", 55, 120.0, "Avanzado"));

    }


    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos); // devuelve copia para evitar cambios externos
    }

    public List<Registro> listarRegistros() {
        return new ArrayList<>(registros);
    }

    // Registra usando un objeto Registro
    public Registro registrarCurso(Registro registro) {
        registro.setId(registroIdSeq.getAndIncrement());
        registros.add(registro);
        return registro;
    }

    // Conveniencia: registrar con campos primitivos
    public Registro registrarCurso(Long courseId, String userId, LocalDate fecha) {
        Registro r = new Registro();
        r.setCourseId(courseId);
        r.setUserId(userId);
        r.setRegistrationDate(fecha);
        return registrarCurso(r);
    }
}
