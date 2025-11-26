package com.example.MDW.controller;

import com.example.MDW.dto.LoginRequest;
import com.example.MDW.dto.RegisterRequest;
import com.example.MDW.jwt.jwtUtil;
import com.example.MDW.model.Alumno;
import com.example.MDW.model.Persona;
import com.example.MDW.service.AlumnoService;
import com.example.MDW.service.CustomUserDetailsService;
import com.example.MDW.service.PersonaService;
import com.example.MDW.service.ProfesorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth") // ⚠️ Asegúrate de que tu formulario HTML apunte a /auth/login y /auth/register
public class AuthController {

    @Autowired
    private PersonaService personaService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private ProfesorService profesorService;

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
                           RedirectAttributes redirectAttrs) {
        try {
            // 1. Validar si existe
            if (personaService.existsByEmail(request.getEmail())) {
                redirectAttrs.addFlashAttribute("error", "El email ya está registrado");
                return "redirect:/"; // O redirigir a la vista de registro
            }

            // 2. Crear Persona
            Persona nuevaPersona = new Persona(
                    request.getNombre(),
                    request.getApellido(),
                    request.getEmail(),
                    request.getPassword() // El servicio debe encriptarla
            );

            // 3. Asociar Alumno automáticamente
            Alumno alumno = new Alumno(nuevaPersona);
            nuevaPersona.setAlumno(alumno);

            // 4. Guardar
            Persona personaGuardada = personaService.registrar(nuevaPersona);

            // 5. AUTO-LOGIN: Generar JWT y Cookie para que entre directo
            UserDetails userDetails = userDetailsService.loadUserByUsername(personaGuardada.getEmail());
            String token = jwtUtil.generateToken(userDetails);
            crearCookie(response, token);

            redirectAttrs.addFlashAttribute("success", "Registro exitoso. ¡Bienvenido!");
            return "redirect:/"; // Redirige al home logueado

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
                        RedirectAttributes redirectAttrs) {
        try {
            // 1. Autenticar con Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // 2. Obtener datos del usuario
            Persona persona = personaService.buscarPorEmail(request.getEmail());

            // 3. Generar Claims y Token JWT
            Map<String, Object> claims = new HashMap<>();
            claims.put("idPersona", persona.getIdPersona());
            claims.put("nombre", persona.getNombre());

            // Generar Token
            String token = jwtUtil.generateToken(persona.getEmail(), claims);

            // 4. 🍪 GUARDAR JWT EN COOKIE (Vital para MVC)
            crearCookie(response, token);

            redirectAttrs.addFlashAttribute("success", "Bienvenido " + persona.getNombre());
            return "redirect:/cursos"; // 👈 Redirige a donde quieras tras el login

        } catch (BadCredentialsException e) {
            redirectAttrs.addFlashAttribute("error", "Credenciales incorrectas");
            return "redirect:/"; // Vuelve al formulario
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Error al iniciar sesión");
            return "redirect:/";
        }
    }

    /**
     * Cerrar Sesión (Logout)
     */
    @PostMapping("/logout") // O @GetMapping si prefieres enlace directo, pero POST es más seguro
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttrs) {
        // Eliminar la cookie sobrescribiéndola con tiempo 0
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 0 segundos = borrar

        response.addCookie(cookie);

        redirectAttrs.addFlashAttribute("success", "Has cerrado sesión correctamente.");
        return "redirect:/";
    }

    // --- Método Auxiliar para crear la Cookie ---
    private void crearCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true); // Seguridad: JS no puede leerla
        cookie.setSecure(false);  // false para localhost (pon true en producción con HTTPS)
        cookie.setPath("/");      // Disponible en toda la app
        cookie.setMaxAge(10 * 60 * 60); // 10 horas de duración
        response.addCookie(cookie);
    }
}