package com.example.MDW.service;

import com.example.MDW.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final List<Usuario> usuarios = new ArrayList<>();

    // Registrar un nuevo usuario
    public void registrar(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Login solo si tiene credenciales correctas
    public Usuario login(String idUsuario, String password) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario)
                        && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

}