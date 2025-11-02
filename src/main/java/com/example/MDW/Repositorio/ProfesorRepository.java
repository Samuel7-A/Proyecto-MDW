package com.example.MDW.Repositorio;

import org.springframework.stereotype.Repository;
import com.example.MDW.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor, Long> {

}
