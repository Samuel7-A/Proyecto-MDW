package com.example.MDW.controller;

import com.example.MDW.dto.LoginRequest;
import com.example.MDW.dto.RegisterRequest;
import com.example.MDW.jwt.jwtUtil;
import com.example.MDW.model.Alumno;
import com.example.MDW.model.Persona;
import com.example.MDW.service.AlumnoService;
import com.example.MDW.service.CustomUserDetailsService;
import com.example.MDW.service.PersonaService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private jwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Procesar Registro
     */
    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request,
                           HttpServletResponse response,
                           HttpSession session,
                           RedirectAttributes redirectAttrs) {
        try {
            if (personaService.existsByEmail(request.getEmail())) {
                redirectAttrs.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/";
            }

            Persona nuevaPersona = new Persona(
                    request.getNombre(),
                    request.getApellido(),
                    request.getEmail(),
                    request.getPassword()
            );

            Alumno alumno = new Alumno(nuevaPersona);
            nuevaPersona.setAlumno(alumno);
            nuevaPersona.setRol("ROLE_USER");

            Persona personaGuardada = personaService.registrar(nuevaPersona);

            // ✅ Generar JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(personaGuardada.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            // ✅ Guardar en cookie (15 minutos de duración)
            crearCookie(response, token);
            session.setAttribute("personaLogueado", personaGuardada);

            redirectAttrs.addFlashAttribute("success", "Registro exitoso. ¡Bienvenido!");
            return "redirect:/";

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al registrar: " + e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * Procesar Login
     */
    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request,
                        HttpServletResponse response,
                        HttpSession session,
                        RedirectAttributes redirectAttrs) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            Persona persona = personaService.buscarPorEmail(request.getEmail());

            if (persona == null) {
                redirectAttrs.addFlashAttribute("error", "Usuario no encontrado");
                return "redirect:/";
            }

            // ✅ Generar JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(persona.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            // ✅ Guardar en cookie (15 minutos de duración)
            crearCookie(response, token);
            session.setAttribute("personaLogueado", persona);

            redirectAttrs.addFlashAttribute("success", "Bienvenido " + persona.getNombre());
            return "redirect:/cursos";

        } catch (BadCredentialsException e) {
            redirectAttrs.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/";
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al iniciar sesión");
            return "redirect:/";
        }
    }

    /**
     * Cerrar Sesión (Logout)
     */
    @PostMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpSession session,
                         RedirectAttributes redirectAttrs) {
        try {
            // ✅ Limpiar cookie
            limpiarCookie(response, "JWT_TOKEN");

            // Invalidar sesión
            session.invalidate();

            redirectAttrs.addFlashAttribute("success", "Has cerrado sesión correctamente.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al cerrar sesión");
        }

        return "redirect:/";
    }

    // ✅ Crear cookie con JWT
    private void crearCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);  // true en producción con HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60); // 15 minutos
        response.addCookie(cookie);
    }

    // ✅ Limpiar cookie
    private void limpiarCookie(HttpServletResponse response, String nombre) {
        Cookie cookie = new Cookie(nombre, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}