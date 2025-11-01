package com.example.MDW.service;

import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    private final List<Inscripcion> inscripciones = new ArrayList<>();
    private final AtomicLong inscripcionIdSeq = new AtomicLong(1);

    @Autowired
    private CursoService cursoService;

    // 🔹 Registrar inscripción
    public Inscripcion registrar(Inscripcion inscripcion) {
        // ✅ Validar antes de registrar
        if (existeInscripcion(inscripcion.getUserId(), inscripcion.getCourseId())) {
            throw new IllegalArgumentException("La persona ya está inscrito en este curso.");
        }

        inscripcion.setId(inscripcionIdSeq.getAndIncrement());
        if (inscripcion.getFecha() == null) {
            inscripcion.setFecha(LocalDate.now()); // si no mandan fecha, se pone la actual
        }
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    // 🔹 Registrar por parámetros
    public Inscripcion registrar(Long courseId, Long userId, LocalDate fecha) {
        Inscripcion i = new Inscripcion();
        i.setCourseId(courseId);
        i.setUserId(userId);
        i.setFecha(fecha != null ? fecha : LocalDate.now());
        return registrar(i);
    }

    // 🔹 Listar inscripciones de una persona
   public List<Inscripcion> obtenerPorPersona(Long userId) {
    return inscripciones.stream()
            .filter(i -> i.getUserId() != null && i.getUserId().equals(userId))
            .collect(Collectors.toList());
}

    // 🔹 Listar todas las inscripciones (admin, debug)
    public List<Inscripcion> listarTodas() {
        return new ArrayList<>(inscripciones);
    }

    // 🔹 Cursos en los que está inscrito una persona
    public List<Curso> obtenerCursosPorPersona(Long userId) {
        return obtenerPorPersona(userId).stream()
                .map(insc -> cursoService.findById(insc.getCourseId()))
                .filter(curso -> curso != null)
                .collect(Collectors.toList());
    }

    // ✅ Validar si la persona ya está inscrito en un curso
   public boolean existeInscripcion(Long userId, Long courseId) {
    return inscripciones.stream()
            .anyMatch(i -> 
                i.getUserId() != null && 
                i.getCourseId() != null &&
                i.getUserId().equals(userId) && 
                i.getCourseId().equals(courseId)
            );
}
    // 🔹 Eliminar inscripción de un curso
    public boolean eliminarInscripcion(Long userId, Long courseId) {
    return inscripciones.removeIf(i -> 
        i.getUserId() != null && 
        i.getCourseId() != null &&
        i.getUserId().equals(userId) && 
        i.getCourseId().equals(courseId)
    );
}

}

