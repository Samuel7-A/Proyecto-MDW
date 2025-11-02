package com.example.MDW.service;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import com.example.MDW.Repositorio.InscripcionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    // 🔹 Registrar inscripción
    public Inscripcion registrar(Alumno alumno, Curso curso, LocalDate fecha) {
        if (inscripcionRepository.existsByAlumnoIdAndCursoId(alumno.getId(), curso.getIdCurso())) {
            throw new IllegalArgumentException("El alumno ya está inscrito en este curso.");
        }

        // ✅ Ajuste: tu constructor tiene 4 parámetros (curso, alumno, fecha, estado)
        Inscripcion inscripcion = new Inscripcion(
            curso,
            alumno,
            fecha != null ? fecha : LocalDate.now(),
            Inscripcion.EstadoInscripcion.PENDIENTE
        );

        return inscripcionRepository.save(inscripcion);
    }

    // 🔹 Listar inscripciones de una persona
    public List<Inscripcion> obtenerPorAlumno(Alumno alumno) {
        return inscripcionRepository.findByAlumno(alumno);
    }

    // 🔹 Listar todas las inscripciones (admin, debug)
    public List<Inscripcion> listarTodas() {
        return inscripcionRepository.findAll();
    }

    // 🔹 Cursos en los que está inscrito una persona
    public List<Curso> obtenerCursosPorAlumno(Alumno alumno) {
        return inscripcionRepository.findByAlumno(alumno).stream()
                .map(Inscripcion::getCurso)
                .collect(Collectors.toList());
    }

    // ✅ Verifica si ya existe una inscripción del alumno en el curso
    public boolean existeInscripcion(Long idAlumno, Long idCurso) {
        return inscripcionRepository.existsByAlumnoIdAndCursoId(idAlumno, idCurso);
    }

    // 🔹 Eliminar inscripción de un curso
    @Transactional
    public boolean eliminarInscripcion(Long alumnoId, Long cursoId) {
        if (inscripcionRepository.existsByAlumnoIdAndCursoId(alumnoId, cursoId)) {
            inscripcionRepository.deleteByAlumnoIdAndCursoId(alumnoId, cursoId);
            return true;
        }
        return false;
    }

}

