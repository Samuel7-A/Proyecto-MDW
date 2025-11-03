
package com.example.MDW.config;

import com.example.MDW.Repositorio.AlumnoRepository;
import com.example.MDW.Repositorio.PersonaRepository;
import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.Repositorio.CursoRepository;
import com.example.MDW.model.Persona;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CursoRepository cursoRepository;
    private final PersonaRepository personaRepository;

    public DataLoader(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (cursoRepository.count() == 0) { // Solo si la tabla está vacía
            cursoRepository.save(new Curso(null, "Introducción a la Ciberseguridad",
                    "Explora el emocionante campo de la ciberseguridad y crece con nosotros",
                    "curso1.jpg", 20, 0.0, "Básico"));

            cursoRepository.save(new Curso(null, "Redes y Conectividad",
                    "Aprende los fundamentos de redes, protocolos y conectividad",
                    "curso2.jpg", 30, 50.0, "Intermedio"));

            cursoRepository.save(new Curso(null, "Seguridad en Redes Avanzada",
                    "Domina técnicas avanzadas para proteger infraestructuras críticas",
                    "curso3.jpg", 40, 75.0, "Avanzado"));

            cursoRepository.save(new Curso(null, "Seguridad en Aplicaciones Web",
                    "Aprende a proteger aplicaciones web frente a ataques como XSS, CSRF y SQL Injection",
                    "curso4.jpg", 35, 70.0, "Intermedio"));

            cursoRepository.save(new Curso(null, "Hacking Ético y Pentesting",
                    "Realiza pruebas de penetración y evalúa vulnerabilidades en sistemas reales",
                    "curso5.jpg", 55, 120.0, "Avanzado"));
        }
        
        // --- PERSONA / ALUMNO ---
        // Validar por correo antes de insertar
        String email = "a@a.com";
        if (!personaRepository.existsByEmail(email)) {
            Persona persona = new Persona("Ariel", "Pérez", email, "123");
            Alumno alumno = new Alumno(persona);
            persona.setAlumno(alumno);
            personaRepository.save(persona);
        }

    }
}
