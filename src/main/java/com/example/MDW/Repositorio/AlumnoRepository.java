package com.example.MDW.Repositorio;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.MDW.model.Alumno;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

}
