package com.example.MDW.jwt;

import com.example.MDW.service.CustomUserDetailsService;
import com.example.MDW.service.PersonaService;
import com.example.MDW.model.Persona;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final jwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final PersonaService personaService;

    public JwtAuthenticationFilter(@Lazy jwtUtil jwtUtil,
                                   @Lazy CustomUserDetailsService userDetailsService,
                                   @Lazy PersonaService personaService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.personaService = personaService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = null;
        String email = null;

        try {
            // 1. Intentar obtener el token del Header
            final String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
            }

            // 2. Si no vino en Header, buscar en Cookies
            if (jwt == null && request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if ("JWT_TOKEN".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }

            // 3. Si encontramos un token, validar
            if (jwt != null) {
                // ✅ VALIDAR EXPIRACIÓN - Aquí es donde se verifica si expiró
                if (jwtUtil.validateToken(jwt)) {
                    // Token válido
                    email = jwtUtil.extractUsername(jwt);

                    if (email != null) {
                        Persona persona = personaService.buscarPorEmail(email);

                        if (persona != null) {
                            // Cargar usuario en Spring Security
                            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                            // Cargar en sesión HTTP
                            HttpSession session = request.getSession(true);
                            session.setAttribute("personaLogueado", persona);
                        }
                    }
                } else {
                    // ❌ Token EXPIRADO - Limpiar cookies y sesión
                    logger.info("JWT Token expirado - Limpiando sesión");
                    limpiarCookies(response);
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        session.invalidate();
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error procesando JWT: " + e.getMessage());
            // En caso de error, permitir que continúe (puede ser una ruta pública)
        }

        filterChain.doFilter(request, response);
    }

    // ✅ Metodo auxiliar para limpiar cookies
    private void limpiarCookies(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}