package com.example.MDW.Repositorio;

import org.springframework.stereotype.Repository;
import com.example.MDW.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findFirstByEmailAndPassword(String email, String password);

    Persona findByEmail(String email);

    boolean existsByEmail(String email);
    
}


