package com.example.MDW.service;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.model.CarritoItem;
import com.example.MDW.Repositorio.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    // Agregar curso al carrito
    public boolean agregarAlCarrito(Alumno alumno, Curso curso) {
        // Verificar si ya está en el carrito
        if (carritoItemRepository.existsByAlumnoIdAndCursoIdCurso(alumno.getId(), curso.getIdCurso())) {
            return false; // Ya existe
        }

        CarritoItem item = new CarritoItem(alumno, curso, LocalDate.now());
        carritoItemRepository.save(item);
        return true;
    }

    // Obtener items del carrito
    public List<CarritoItem> obtenerCarrito(Long idAlumno) {
        return carritoItemRepository.findByAlumnoId(idAlumno);
    }

    // Calcular total del carrito
    public double calcularTotal(Long idAlumno) {
        List<CarritoItem> items = carritoItemRepository.findByAlumnoId(idAlumno);
        return items.stream()
                .mapToDouble(item -> item.getCurso().getPrecio())
                .sum();
    }

    // Contar items en el carrito
    public int contarItems(Long idAlumno) {
        return carritoItemRepository.findByAlumnoId(idAlumno).size();
    }

    // Eliminar un item del carrito
    public boolean eliminarDelCarrito(Long idAlumno, Long idCurso) {
        var item = carritoItemRepository.findByAlumnoIdAndCursoIdCurso(idAlumno, idCurso);
        if (item.isPresent()) {
            carritoItemRepository.delete(item.get());
            return true;
        }
        return false;
    }

    // Vaciar carrito completo
    @Transactional
    public void vaciarCarrito(Long idAlumno) {
        carritoItemRepository.deleteByAlumnoId(idAlumno);
    }
}