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
        // Datos en memoria (cámbialos por JPA si luego usas BD)
        cursos.add(new Curso(1L, "Introducción a la Ciberseguridad",
                "Explora el emocionante campo de la ciberseguridad y crece con nosotros", "curso1.jpg", 20));
        cursos.add(new Curso(2L, "Redes y Conectividad",
                "Aprende los fundamentos de redes, protocolos y conectividad", "curso2.jpg", 30));
        cursos.add(new Curso(3L, "Seguridad en Redes Avanzada",
                "Domina técnicas avanzadas para proteger infraestructuras críticas", "curso3.jpg", 40));
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
