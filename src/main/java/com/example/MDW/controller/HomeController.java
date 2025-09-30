
package com.example.MDW.controller;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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

    private final UsuarioService usuarioService;

    public HomeController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
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

    @PostMapping("/login")
    public String login(@RequestParam String idUsuario,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        Usuario usuario = usuarioService.login(idUsuario, password);
        if (usuario != null) {
            session.setAttribute("usuarioLogueado", usuario);
            return "redirect:/"; // refresca la página
        }
        model.addAttribute("error", "Credenciales incorrectas o usuario no registrado");
        return "index";
    }

    @PostMapping("/register")
    public String register(@RequestParam String idUsuario,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        Usuario nuevo = new Usuario(idUsuario, email, password);
        usuarioService.registrar(nuevo);
        model.addAttribute("mensaje", "Usuario registrado. Ahora puedes iniciar sesión.");
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttrs) {
        session.invalidate();
        redirectAttrs.addFlashAttribute("mensaje", "Sesión cerrada correctamente");
        return "redirect:/";
    }

}






