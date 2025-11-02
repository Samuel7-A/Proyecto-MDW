
package com.example.MDW.Repositorio;

import org.springframework.stereotype.Repository;
import com.example.MDW.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}

