
package com.example.MDW.config;

import com.example.MDW.Repositorio.PersonaRepository;
import com.example.MDW.Repositorio.ProfesorRepository;
import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Profesor;
import com.example.MDW.Repositorio.CursoRepository;
import com.example.MDW.model.Persona;
import com.example.MDW.service.PersonaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private ProfesorRepository profesorRepository;
    @Autowired
    private PersonaService personaService;

    @Override
    public void run(String... args) throws Exception {

        // ----- Crear persona/alumno/profesor por defecto -----
        String email = "a@a.com";
        Persona persona = personaRepository.findByEmail(email);

        if (persona == null) {
            // Crear nueva persona con contraseña que será encriptada
            persona = new Persona("Ariel", "Pérez", email, "123");
            Alumno alumno = new Alumno(persona);
            persona.setAlumno(alumno);

            // Usar PersonaService para que encripte la contraseña
            persona = personaService.registrar(persona);
        }

        // Crear profesor vinculado a esa persona si no existe
        Profesor profesor = profesorRepository.findByPersona(persona);
        if (profesor == null) {
            profesor = new Profesor(persona, "Ciberseguridad");
            profesorRepository.save(profesor);

            // Asociar el profesor a la persona
            persona.setProfesor(profesor);
            profesor.setPersona(persona);
            personaRepository.save(persona);
        }

        // ----- Crear cursos por defecto solo si no existen -----
        if (cursoRepository.count() == 0) {
            cursoRepository.save(new Curso(null, "Introducción a la Ciberseguridad",
                    "Explora el emocionante campo de la ciberseguridad y crece con nosotros",
                    "curso1.jpg", 20, 0.0, "Básico", profesor));

            cursoRepository.save(new Curso(null, "Redes y Conectividad",
                    "Aprende los fundamentos de redes, protocolos y conectividad",
                    "curso2.jpg", 30, 50.0, "Intermedio", profesor));

            cursoRepository.save(new Curso(null, "Seguridad en Redes Avanzada",
                    "Domina técnicas avanzadas para proteger infraestructuras críticas",
                    "curso3.jpg", 40, 75.0, "Avanzado", profesor));

            cursoRepository.save(new Curso(null, "Seguridad en Aplicaciones Web",
                    "Aprende a proteger aplicaciones web frente a ataques como XSS, CSRF y SQL Injection",
                    "curso4.jpg", 35, 70.0, "Intermedio", profesor));

            cursoRepository.save(new Curso(null, "Hacking Ético y Pentesting",
                    "Realiza pruebas de penetración y evalúa vulnerabilidades en sistemas reales",
                    "curso5.jpg", 55, 120.0, "Avanzado", profesor));
        }
    }

}
