package com.example.MDW.service;

import com.example.MDW.model.Inscripcion;
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

    // 🔹 Registrar inscripción
    public Inscripcion registrar(Inscripcion inscripcion) {
        inscripcion.setId(inscripcionIdSeq.getAndIncrement());
        if (inscripcion.getFecha() == null) {
            inscripcion.setFecha(LocalDate.now()); // si no mandan fecha, se pone la actual
        }
        inscripciones.add(inscripcion);
        return inscripcion;
    }

    // 🔹 Registrar por parámetros
    public Inscripcion registrar(Long courseId, String userId, LocalDate fecha) {
        Inscripcion i = new Inscripcion();
        i.setCourseId(courseId);
        i.setUserId(userId);
        i.setFecha(fecha != null ? fecha : LocalDate.now());
        return registrar(i);
    }

    // 🔹 Listar inscripciones de un usuario
    public List<Inscripcion> obtenerPorUsuario(String userId) {
        return inscripciones.stream()
                .filter(i -> i.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    // 🔹 Listar todas las inscripciones (admin, debug)
    public List<Inscripcion> listarTodas() {
        return new ArrayList<>(inscripciones);
    }
}
