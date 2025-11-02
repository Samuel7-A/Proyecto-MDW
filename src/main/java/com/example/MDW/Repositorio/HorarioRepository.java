package com.example.MDW.Repositorio;

import com.example.MDW.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    java.util.List<Horario> findByDiaSemana(String diaSemana);

    java.util.List<Horario> findByCursoId(Long idCurso);
}
