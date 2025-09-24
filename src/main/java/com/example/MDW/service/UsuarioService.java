package com.example.MDW.service;

import com.example.MDW.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    
   public UsuarioService() {
    Usuario admin = new Usuario("admin", "admin@mdw.com", "1234");
    admin.setActivo(true);
    admin.setRol("ADMIN"); // admin por defecto
    usuarios.add(admin);
}


    private final List<Usuario> usuarios = new ArrayList<>();

    // Registrar un nuevo usuario
    public void registrar(Usuario usuario) {
        usuarios.add(usuario);
    }

    // Login solo si está aceptado y credenciales correctas
    public Usuario login(String idUsuario, String password) {
        return usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario)
                        && u.getPassword().equals(password)
                        && u.isActivo()) // solo aceptados pueden entrar
                .findFirst()
                .orElse(null);
    }

    // Listar todos los usuarios
    public List<Usuario> listarUsuarios() {
        return usuarios;
    }

    // Aceptar un usuario
    public void activarUsuario(String idUsuario) {
        usuarios.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst()
                .ifPresent(u -> u.setActivo(true));
    }
}