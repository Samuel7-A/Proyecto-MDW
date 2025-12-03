package com.example.MDW.controller;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import com.example.MDW.model.Persona;
import com.example.MDW.model.Profesor;
import com.example.MDW.service.CursoService;
import com.example.MDW.service.InscripcionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    private final CursoService cursoService;
    private final InscripcionService inscripcionService;

    @Autowired
    public CursoController(CursoService cursoService, InscripcionService inscripcionService) {
        this.cursoService = cursoService;
        this.inscripcionService = inscripcionService;
    }

    @PostMapping("/inscribirse")
    public String inscribirse(@RequestParam Long courseId,
                              @RequestParam String registrationDate,
                              HttpSession session,
                              RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null || persona.getAlumno() == null) {
            //VALIDACION
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para inscribirte en un curso."); //El usuario debe estar logueado y ser un alumno.
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        Curso curso = cursoService.findById(courseId); //Validación: Se busca el curso por su ID.
        if (curso == null) { //El curso debe existir.
            redirectAttrs.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/cursos";
        }

        //Validación de duplicado antes de registrar
        if (inscripcionService.existeInscripcion(alumno.getId(), courseId)) {
            redirectAttrs.addFlashAttribute("error", "Ya estás inscrito en este curso.");
            return "redirect:/cursos";
        }

        LocalDate fecha = LocalDate.parse(registrationDate);

        inscripcionService.registrar(alumno, curso, fecha);

        redirectAttrs.addFlashAttribute("success", "Inscripción realizada con éxito.");
        return "redirect:/cursos";
    }

    @PostMapping("/desinscribirse")
    public String desinscribirse(@RequestParam Long courseId,
            HttpSession session,
            RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para gestionar tus cursos.");
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();

        boolean eliminado = inscripcionService.eliminarInscripcion(alumno.getId(), courseId);

        if (eliminado) {
            redirectAttrs.addFlashAttribute("success", "Te has desinscrito del curso correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No estabas inscrito en este curso.");
        }

        return "redirect:/cursos/mis-cursos";
    }

    // 🔹 Listar cursos
  @GetMapping
public String listarCursos(Model model, HttpSession session) {
    List<Curso> cursos = cursoService.listarCursos();
    model.addAttribute("cursos", cursos);

    // 🔹 Obtener persona logueada desde sesión
    Persona persona = (Persona) session.getAttribute("personaLogueado");
    model.addAttribute("personaLogueado", persona);

    // 🔹 Mostrar cursos inscritos en el sidebar si hay sesión activa
    if (persona != null && persona.getAlumno() != null) {
        Alumno alumno = persona.getAlumno();
        List<Curso> cursosInscritos = inscripcionService.obtenerCursosPorAlumno(alumno);
        model.addAttribute("cursosInscritosSidebar", cursosInscritos);
    } else {
        model.addAttribute("cursosInscritosSidebar", List.of());
    }

        return "cursos";
    }

    // Mostrar los cursos en los que el alumno está inscrito
    @GetMapping("/mis-cursos")
    public String misCursos(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();

        List<Curso> cursos = inscripcionService.obtenerCursosPorAlumno(alumno);
        model.addAttribute("cursos", cursos);
        return "mis-cursos";
    }

    // LOGICA DEL PROFESOR
    @GetMapping("/gestion")
    public String gestionarCursos(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona == null || persona.getProfesor() == null) {
            return "redirect:/login";
        }

        // ✅ Log para verificar el ID del profesor
        System.out.println("ID del profesor: " + persona.getProfesor().getIdProfesor());

        List<Curso> cursos = cursoService.obtenerCursosPorProfesor(idProfesor);

        // ✅ Log para verificar cuántos cursos se obtienen
        System.out.println("Cursos encontrados: " + cursos.size());

        model.addAttribute("cursos", cursos);
        model.addAttribute("cursoEdit", new Curso());

        return "gestion-cursos";
    }

    @PostMapping("/crear")
    public String crearCurso(@RequestParam String nombre,
                            @RequestParam String descripcion,
                            @RequestParam("imagen") MultipartFile imagen,
                            @RequestParam int horas,
                            @RequestParam double precio,
                            @RequestParam String nivel,
                            HttpSession session,
                            RedirectAttributes redirectAttrs) {

        Persona persona = (Persona) session.getAttribute("personaLogueado");
        if (persona == null || persona.getProfesor() == null) {
            redirectAttrs.addFlashAttribute("error", "No tienes permisos para crear cursos.");
            return "redirect:/cursos/gestion";
        }

        Profesor profesor = persona.getProfesor();

        try {
            String nombreImagen = "default.jpg"; // Imagen por defecto.

            //  Si el usuario sube una imagen, la guardamos físicamente
            if (imagen != null && !imagen.isEmpty()) {
                // Carpeta donde guardarás las imágenes
                String carpeta = "src/main/resources/static/img/";
                java.nio.file.Path rutaCarpeta = java.nio.file.Paths.get(carpeta);

                // Guardar el archivo con su nombre original
                nombreImagen = imagen.getOriginalFilename();
                java.nio.file.Path rutaArchivo = rutaCarpeta.resolve(nombreImagen);

                // Copiar archivo (si existe, se reemplaza)
                java.nio.file.Files.copy(imagen.getInputStream(), rutaArchivo,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }

            // Crear curso con el nombre de la imagen (guardado en BD)
            Curso nuevoCurso = new Curso(null, nombre, descripcion, nombreImagen, horas, precio, nivel, profesor);
            cursoService.guardar(nuevoCurso);

            redirectAttrs.addFlashAttribute("success", "Curso creado con éxito.");
            return "redirect:/cursos/gestion";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al subir la imagen o crear el curso.");
            return "redirect:/cursos/gestion";
        }
    }

    @PostMapping("/editar")
    public String editarCurso(@RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam("imagen") MultipartFile imagen,
            @RequestParam String descripcion,
            @RequestParam int horas,
            @RequestParam double precio,
            @RequestParam String nivel,
            RedirectAttributes redirectAttrs) {

        System.out.println("ID recibido: " + id);
        System.out.println("Nombre recibido: " + nombre);
        System.out.println("Imagen vacía: " + (imagen == null || imagen.isEmpty()));

        Curso curso = cursoService.findById(id);
        System.out.println("Curso encontrado: " + (curso != null ? curso.getNombre() : "null"));

        if (curso == null) {
            redirectAttrs.addFlashAttribute("error", "El curso no existe.");
            return "redirect:/cursos/gestion";
        }

        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setHoras(horas);
        curso.setPrecio(precio);
        curso.setNivel(nivel);

        try {
            if (imagen != null && !imagen.isEmpty()) {
                // Carpeta donde guardarás las imágenes
                String carpeta = "src/main/resources/static/img/";
                java.nio.file.Path rutaCarpeta = java.nio.file.Paths.get(carpeta);

                // Guardar el archivo con su nombre original
                String nombreImagen = imagen.getOriginalFilename();
                java.nio.file.Path rutaArchivo = rutaCarpeta.resolve(nombreImagen);

                // Copiar archivo (si existe, se reemplaza)
                java.nio.file.Files.copy(imagen.getInputStream(), rutaArchivo,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                // Guardar nombre de archivo en la entidad
                curso.setImagen(nombreImagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttrs.addFlashAttribute("error", "Error al subir la imagen.");
            return "redirect:/cursos/gestion";
        }

        cursoService.guardar(curso);

        redirectAttrs.addFlashAttribute("success", "Curso actualizado correctamente.");
        return "redirect:/cursos/gestion";
    }

    @PostMapping("/eliminar")
    public String eliminarCurso(@RequestParam Long id, RedirectAttributes redirectAttrs) {
        cursoService.eliminar(id);
        redirectAttrs.addFlashAttribute("success", "Curso eliminado correctamente.");
        return "redirect:/cursos/gestion";
    }

}
