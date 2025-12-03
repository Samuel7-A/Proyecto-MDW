package com.example.MDW.controller;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.CarritoItem;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Inscripcion;
import com.example.MDW.model.Persona;
import com.example.MDW.service.CarritoService;
import com.example.MDW.service.CursoService;
import com.example.MDW.service.InscripcionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private InscripcionService inscripcionService;

    // Ver el carrito
    @GetMapping
    public String verCarrito(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión como alumno para ver tu carrito");
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        List<CarritoItem> items = carritoService.obtenerCarrito(alumno.getId());
        double total = carritoService.calcularTotal(alumno.getId());

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("persona", persona);

        return "carrito";
    }

    // Agregar curso al carrito
    @PostMapping("/agregar/{idCurso}")
    public String agregarAlCarrito(@PathVariable Long idCurso, 
                                   HttpSession session, 
                                   RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión como alumno");
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        Curso curso = cursoService.findById(idCurso);

        if (curso == null) {
            redirectAttrs.addFlashAttribute("error", "Curso no encontrado");
            return "redirect:/cursos";
        }

        // Verificar si ya está inscrito
        if (inscripcionService.estaInscrito(alumno, curso)) {
            redirectAttrs.addFlashAttribute("error", "Ya estás inscrito en este curso");
            return "redirect:/cursos";
        }

        boolean agregado = carritoService.agregarAlCarrito(alumno, curso);
        
        if (agregado) {
            redirectAttrs.addFlashAttribute("success", "Curso agregado al carrito");
        } else {
            redirectAttrs.addFlashAttribute("error", "El curso ya está en tu carrito");
        }

        return "redirect:/cursos";
    }

    // Eliminar item del carrito
    @PostMapping("/eliminar/{idCurso}")
    public String eliminarDelCarrito(@PathVariable Long idCurso, 
                                     HttpSession session, 
                                     RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        boolean eliminado = carritoService.eliminarDelCarrito(alumno.getId(), idCurso);

        if (eliminado) {
            redirectAttrs.addFlashAttribute("success", "Curso eliminado del carrito");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo eliminar el curso");
        }

        return "redirect:/carrito";
    }

    // Vaciar carrito
    @PostMapping("/vaciar")
    public String vaciarCarrito(HttpSession session, RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        carritoService.vaciarCarrito(alumno.getId());
        
        redirectAttrs.addFlashAttribute("success", "Carrito vaciado");
        return "redirect:/carrito";
    }

    // Proceder al pago (muestra formulario)
    @GetMapping("/pago")
    public String mostrarPago(Model model, HttpSession session, RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        List<CarritoItem> items = carritoService.obtenerCarrito(alumno.getId());
        
        if (items.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío");
            return "redirect:/carrito";
        }

        double total = carritoService.calcularTotal(alumno.getId());
        
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("persona", persona);

        return "pago";
    }

    // Confirmar compra (procesar pago y crear inscripciones)
    @PostMapping("/confirmar")
    public String confirmarCompra(@RequestParam(required = false) String numeroTarjeta,
                                  @RequestParam(required = false) String nombreTitular,
                                  @RequestParam(required = false) String fechaExpiracion,
                                  @RequestParam(required = false) String cvv,
                                  HttpSession session, 
                                  RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");
        
        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/cursos";
        }

        Alumno alumno = persona.getAlumno();
        List<CarritoItem> items = carritoService.obtenerCarrito(alumno.getId());

        if (items.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío");
            return "redirect:/carrito";
        }

        // Simulación de validación de pago (opcional)
        if (numeroTarjeta != null && !numeroTarjeta.isEmpty()) {
            if (numeroTarjeta.length() < 16) {
                redirectAttrs.addFlashAttribute("error", "Número de tarjeta inválido");
                return "redirect:/carrito/pago";
            }
        }

        try {
            // Crear inscripciones para cada curso en el carrito
            for (CarritoItem item : items) {
                Inscripcion inscripcion = new Inscripcion();
                inscripcion.setAlumno(alumno);
                inscripcion.setCurso(item.getCurso());
                inscripcion.setFecha(LocalDate.now());
                inscripcionService.registrarInscripcion(inscripcion);
            }

            // Vaciar el carrito después de completar la compra
            carritoService.vaciarCarrito(alumno.getId());

            redirectAttrs.addFlashAttribute("success", "¡Compra realizada con éxito! Tus cursos están disponibles en 'Mis Cursos'");
            return "redirect:/cursos/mis-cursos";

        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", "Hubo un error al procesar tu compra: " + e.getMessage());
            return "redirect:/carrito/pago";
        }
    }
}
