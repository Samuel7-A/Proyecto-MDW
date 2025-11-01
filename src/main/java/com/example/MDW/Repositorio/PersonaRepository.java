package com.example.MDW.Repositorio;

import org.springframework.stereotype.Repository;
import com.example.MDW.model.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
  Persona findByEmailAndPassword(String email, String password);
}
