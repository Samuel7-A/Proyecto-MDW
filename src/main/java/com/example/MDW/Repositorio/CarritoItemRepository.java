package com.example.MDW.Repositorio;

import com.example.MDW.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    // Obtener todos los items del carrito de un alumno
    List<CarritoItem> findByAlumnoId(Long idAlumno);

    // Verificar si ya existe un curso en el carrito del alumno
    boolean existsByAlumnoIdAndCursoIdCurso(Long idAlumno, Long idCurso);

    // Obtener un item específico del carrito
    Optional<CarritoItem> findByAlumnoIdAndCursoIdCurso(Long idAlumno, Long idCurso);

    // Eliminar todos los items del carrito de un alumno
    void deleteByAlumnoId(Long idAlumno);
}