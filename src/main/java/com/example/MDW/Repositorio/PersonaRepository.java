package com.example.MDW.Repositorio;

import com.example.MDW.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    // 🔹 Búsqueda simple (puedes mantenerla si la usas en otros lugares)
    Optional<Persona> findFirstByEmailAndPassword(String email, String password);

    Persona findByEmail(String email);

    boolean existsByEmail(String email);

    // 🔹 NUEVO: Búsqueda con relaciones (profesor y alumno)
    @Query("""
        SELECT p FROM Persona p
        LEFT JOIN FETCH p.profesor
        LEFT JOIN FETCH p.alumno
        WHERE p.email = :email AND p.password = :password
    """)
    Optional<Persona> findWithRelationsByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );
}
