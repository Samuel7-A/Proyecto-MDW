
package com.example.MDW.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.MDW.model.Usuario;
import com.example.MDW.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class HomeController {

     

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    @GetMapping("/cursos")
    public String cursos() {
        return "cursos";
    }

    @GetMapping("/niveles")                 
    public String niveles() {
        return "niveles";
    }

    @GetMapping("/Nosotros")
    public String Nosotros() {
        return "Nosotros";
    }

    @GetMapping("/index")
    public String pagprincipal() {
        return "index";
    }

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public String login(@RequestParam String idUsuario,
                        @RequestParam String password,
                        Model model) {
        Usuario usuario = usuarioService.login(idUsuario, password);
        if (usuario != null) {
            model.addAttribute("usuarioLogueado", usuario);
            model.addAttribute("usuarios", usuarioService.listarUsuarios());
            return "index"; // regresa a index
        }
        model.addAttribute("error", "Credenciales incorrectas o usuario no aceptado");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    @PostMapping("/register")
    public String register(@RequestParam String idUsuario,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        Usuario nuevo = new Usuario(idUsuario, email, password);
        usuarioService.registrar(nuevo);
        model.addAttribute("mensaje", "Usuario registrado. Espera aprobación.");
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    @PostMapping("/aceptar")
    public String aceptarUsuario(@RequestParam String idUsuario, Model model) {
        usuarioService.activarUsuario(idUsuario);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "index";
    }

    

@GetMapping("/logout")
public String logout(HttpSession session, Model model) {
    session.invalidate();
    model.addAttribute("mensaje", "Sesión cerrada correctamente");
    return "index";
}
}






