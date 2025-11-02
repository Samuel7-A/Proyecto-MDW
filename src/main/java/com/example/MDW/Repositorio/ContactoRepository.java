package com.example.MDW.Repositorio;

import com.example.MDW.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    // Buscar contactos por nombre
    List<Contacto> findByNombre(String nombre);

    // Buscar contactos por distrito
    List<Contacto> findByDistrito(String distrito);

    // Buscar contactos por nivel (primaria, secundaria, universidad, etc.)
    List<Contacto> findByNivel(String nivel);

    // Buscar contacto por DNI
    Contacto findByDni(String dni);
}
