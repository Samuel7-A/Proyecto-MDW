package com.example.MDW.Repositorio;

import com.example.MDW.model.Inscripcion;
import com.example.MDW.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByAlumno(Alumno alumno);
    boolean existsByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);
    void deleteByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);
}
