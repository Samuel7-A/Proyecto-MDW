package com.example.MDW.service;

import com.example.MDW.model.Curso;
import org.springframework.stereotype.Service;

import com.example.MDW.Repositorio.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    public Curso findById(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }

}