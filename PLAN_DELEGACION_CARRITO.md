# 🛒 PLAN DE DELEGACIÓN - CARRITO DE COMPRAS

## Proyecto MDW - Sistema de Gestión de Cursos

---

## 📋 RESUMEN DEL PROYECTO

Implementar un sistema de carrito de compras que permita a los usuarios agregar cursos antes de inscribirse, ver un
resumen con el total a pagar, y completar la compra (opcional: método de pago con tarjeta).

---

## 🎯 ARQUITECTURA GENERAL

### Entidades Nuevas

- **CarritoItem**: Representa un curso agregado al carrito de un usuario
    - Relación: ManyToOne con Alumno
    - Relación: ManyToOne con Curso
    - Fecha de agregado al carrito

### Flujo del Sistema

1. Usuario ve cursos → Agrega al carrito (no inscribe directamente)
2. Usuario ve carrito → Muestra resumen de cursos + total
3. Usuario confirma compra → Se crean las inscripciones + se vacía carrito
4. (Opcional) Antes de confirmar → Muestra formulario de pago con tarjeta

---

## 👥 DELEGACIÓN DE TAREAS (3 PERSONAS)

---

## 🔵 PERSONA 1: BACKEND - MODELO Y LÓGICA DEL CARRITO

### **Prioridad: ALTA - DEBE HACERSE PRIMERO**

Esta persona sienta las bases del sistema. Las demás personas dependen de este trabajo.

### Tareas Detalladas:

#### 1️⃣ Crear Entidad CarritoItem (Model)

**Archivo**: `src/main/java/com/example/MDW/model/CarritoItem.java`

```java
package com.example.MDW.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carrito_item")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito_item")
    private Long idCarritoItem;

    @ManyToOne
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Column(name = "fecha_agregado")
    private LocalDate fechaAgregado;

    public CarritoItem(Alumno alumno, Curso curso, LocalDate fechaAgregado) {
        this.alumno = alumno;
        this.curso = curso;
        this.fechaAgregado = fechaAgregado;
    }
}
```

#### 2️⃣ Crear Repository para CarritoItem

**Archivo**: `src/main/java/com/example/MDW/Repositorio/CarritoItemRepository.java`

```java
package com.example.MDW.Repositorio;

import com.example.MDW.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {

    // Obtener todos los items del carrito de un alumno
    List<CarritoItem> findByAlumnoId(Long idAlumno);

    // Verificar si ya existe un curso en el carrito del alumno
    boolean existsByAlumnoIdAndCursoIdCurso(Long idAlumno, Long idCurso);

    // Obtener un item específico del carrito
    Optional<CarritoItem> findByAlumnoIdAndCursoIdCurso(Long idAlumno, Long idCurso);

    // Eliminar todos los items del carrito de un alumno
    void deleteByAlumnoId(Long idAlumno);
}
```

#### 3️⃣ Crear Service para el Carrito

**Archivo**: `src/main/java/com/example/MDW/service/CarritoService.java`

```java
package com.example.MDW.service;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.Curso;
import com.example.MDW.model.CarritoItem;
import com.example.MDW.Repositorio.CarritoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CarritoService {

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    // Agregar curso al carrito
    public boolean agregarAlCarrito(Alumno alumno, Curso curso) {
        // Verificar si ya está en el carrito
        if (carritoItemRepository.existsByAlumnoIdAndCursoIdCurso(alumno.getId(), curso.getIdCurso())) {
            return false; // Ya existe
        }

        CarritoItem item = new CarritoItem(alumno, curso, LocalDate.now());
        carritoItemRepository.save(item);
        return true;
    }

    // Obtener items del carrito
    public List<CarritoItem> obtenerCarrito(Long idAlumno) {
        return carritoItemRepository.findByAlumnoId(idAlumno);
    }

    // Calcular total del carrito
    public double calcularTotal(Long idAlumno) {
        List<CarritoItem> items = carritoItemRepository.findByAlumnoId(idAlumno);
        return items.stream()
                .mapToDouble(item -> item.getCurso().getPrecio())
                .sum();
    }

    // Contar items en el carrito
    public int contarItems(Long idAlumno) {
        return carritoItemRepository.findByAlumnoId(idAlumno).size();
    }

    // Eliminar un item del carrito
    public boolean eliminarDelCarrito(Long idAlumno, Long idCurso) {
        var item = carritoItemRepository.findByAlumnoIdAndCursoIdCurso(idAlumno, idCurso);
        if (item.isPresent()) {
            carritoItemRepository.delete(item.get());
            return true;
        }
        return false;
    }

    // Vaciar carrito completo
    @Transactional
    public void vaciarCarrito(Long idAlumno) {
        carritoItemRepository.deleteByAlumnoId(idAlumno);
    }
}
```

#### 4️⃣ Modificar CursoController - Agregar endpoints del carrito

**Archivo**: `src/main/java/com/example/MDW/controller/CursoController.java`

Agregar al principio del controller (después de las dependencias existentes):

```java

@Autowired
private CarritoService carritoService;
```

Agregar estos nuevos métodos al final del controller:

```java
// ========== MÉTODOS DEL CARRITO ==========

@PostMapping("/agregar-al-carrito")
public String agregarAlCarrito(@RequestParam Long courseId,
                               HttpSession session,
                               RedirectAttributes redirectAttrs) {
    Persona persona = (Persona) session.getAttribute("personaLogueado");
    if (persona == null || persona.getAlumno() == null) {
        redirectAttrs.addFlashAttribute("error", "Debes iniciar sesión para agregar cursos al carrito.");
        return "redirect:/cursos";
    }

    Alumno alumno = persona.getAlumno();
    Curso curso = cursoService.findById(courseId);

    if (curso == null) {
        redirectAttrs.addFlashAttribute("error", "El curso no existe.");
        return "redirect:/cursos";
    }

    // Verificar si ya está inscrito
    if (inscripcionService.existeInscripcion(alumno.getId(), courseId)) {
        redirectAttrs.addFlashAttribute("error", "Ya estás inscrito en este curso.");
        return "redirect:/cursos";
    }

    boolean agregado = carritoService.agregarAlCarrito(alumno, curso);

    if (agregado) {
        redirectAttrs.addFlashAttribute("success", "Curso agregado al carrito.");
    } else {
        redirectAttrs.addFlashAttribute("error", "El curso ya está en tu carrito.");
    }

    return "redirect:/cursos";
}

@PostMapping("/eliminar-del-carrito")
public String eliminarDelCarrito(@RequestParam Long courseId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttrs) {
    Persona persona = (Persona) session.getAttribute("personaLogueado");
    if (persona == null || persona.getAlumno() == null) {
        return "redirect:/login";
    }

    boolean eliminado = carritoService.eliminarDelCarrito(persona.getAlumno().getId(), courseId);

    if (eliminado) {
        redirectAttrs.addFlashAttribute("success", "Curso eliminado del carrito.");
    } else {
        redirectAttrs.addFlashAttribute("error", "No se pudo eliminar el curso.");
    }

    return "redirect:/carrito";
}
```

#### 5️⃣ Actualizar SecurityConfig

**Archivo**: `src/main/java/com/example/MDW/config/securityConfig.java`

Agregar permisos para el carrito en el método `securityFilterChain`:

```java
.requestMatchers(
    "/",
            "/index",
            "/auth/**",
            "/login",
            "/register",
            "/Nosotros",
            "/niveles",
            "/cursos",
            "/carrito",           // ← AGREGAR
            "/css/**",
            "/img/**",
            "/js/**",
            "/*.pdf"
).

permitAll()
```

---

## 🟢 PERSONA 2: BACKEND - PROCESO DE COMPRA Y FRONTEND DEL CARRITO

### **Prioridad: MEDIA - DEPENDE DE PERSONA 1**

Esta persona crea la vista del carrito y el proceso de checkout.

### Tareas Detalladas:

#### 1️⃣ Crear Controller para el Carrito

**Archivo**: `src/main/java/com/example/MDW/controller/CarritoController.java`

```java
package com.example.MDW.controller;

import com.example.MDW.model.Alumno;
import com.example.MDW.model.CarritoItem;
import com.example.MDW.model.Curso;
import com.example.MDW.model.Persona;
import com.example.MDW.service.CarritoService;
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
    private InscripcionService inscripcionService;

    // Ver carrito
    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/login";
        }

        Long idAlumno = persona.getAlumno().getId();
        List<CarritoItem> items = carritoService.obtenerCarrito(idAlumno);
        double total = carritoService.calcularTotal(idAlumno);

        model.addAttribute("items", items);
        model.addAttribute("total", total);
        model.addAttribute("personaLogueado", persona);

        return "carrito";
    }

    // Procesar compra (confirmar inscripciones)
    @PostMapping("/confirmar-compra")
    public String confirmarCompra(HttpSession session,
                                  RedirectAttributes redirectAttrs) {
        Persona persona = (Persona) session.getAttribute("personaLogueado");

        if (persona == null || persona.getAlumno() == null) {
            return "redirect:/login";
        }

        Alumno alumno = persona.getAlumno();
        List<CarritoItem> items = carritoService.obtenerCarrito(alumno.getId());

        if (items.isEmpty()) {
            redirectAttrs.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }

        // Crear inscripciones para cada curso del carrito
        for (CarritoItem item : items) {
            inscripcionService.registrar(alumno, item.getCurso(), LocalDate.now());
        }

        // Vaciar carrito después de la compra
        carritoService.vaciarCarrito(alumno.getId());

        redirectAttrs.addFlashAttribute("success",
                "¡Compra realizada con éxito! Ya puedes ver tus cursos.");
        return "redirect:/cursos/mis-cursos";
    }
}
```

#### 2️⃣ Crear Vista del Carrito

**Archivo**: `src/main/resources/templates/carrito.html`

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Carrito - MDW</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light d-flex flex-column min-vh-100">

<header class="bg-dark shadow-sm">
    <div th:replace="~{fragments/header :: navbar}"></div>
</header>

<main class="container my-5">
    <h1 class="mb-4"><i class="bi bi-cart3"></i> Mi Carrito de Compras</h1>

    <!-- Mensajes -->
    <div th:if="${error}" class="alert alert-danger" role="alert">
        <span th:text="${error}"></span>
    </div>
    <div th:if="${success}" class="alert alert-success" role="alert">
        <span th:text="${success}"></span>
    </div>

    <div class="row">
        <!-- Columna izquierda: Lista de cursos -->
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">Cursos en el carrito</h5>
                </div>
                <div class="card-body">
                    <!-- Si el carrito está vacío -->
                    <div th:if="${#lists.isEmpty(items)}" class="text-center py-5">
                        <i class="bi bi-cart-x" style="font-size: 4rem; color: #ccc;"></i>
                        <p class="mt-3 text-muted">Tu carrito está vacío</p>
                        <a href="/cursos" class="btn btn-primary">Ver Cursos</a>
                    </div>

                    <!-- Lista de items -->
                    <div th:unless="${#lists.isEmpty(items)}">
                        <div th:each="item : ${items}" class="border-bottom py-3">
                            <div class="row align-items-center">
                                <div class="col-md-2">
                                    <img th:src="@{'/img/' + ${item.curso.imagen}}"
                                         class="img-fluid rounded"
                                         alt="Curso">
                                </div>
                                <div class="col-md-6">
                                    <h6 th:text="${item.curso.nombre}"></h6>
                                    <p class="text-muted mb-0" th:text="${item.curso.descripcion}"></p>
                                    <small class="text-muted">
                                        <i class="bi bi-clock"></i>
                                        <span th:text="${item.curso.horas} + ' horas'"></span>
                                    </small>
                                </div>
                                <div class="col-md-2 text-center">
                                    <strong class="text-primary">
                                        S/ <span th:text="${item.curso.precio}"></span>
                                    </strong>
                                </div>
                                <div class="col-md-2 text-end">
                                    <form th:action="@{/cursos/eliminar-del-carrito}" method="post">
                                        <input type="hidden" name="courseId" th:value="${item.curso.idCurso}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                            <i class="bi bi-trash"></i> Eliminar
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Columna derecha: Resumen del pedido -->
        <div class="col-lg-4">
            <div class="card shadow-sm sticky-top" style="top: 20px;">
                <div class="card-header bg-success text-white">
                    <h5 class="mb-0">Resumen del Pedido</h5>
                </div>
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <span>Cursos:</span>
                        <strong th:text="${#lists.size(items)}"></strong>
                    </div>
                    <hr>
                    <div class="d-flex justify-content-between mb-3">
                        <span><strong>Total:</strong></span>
                        <strong class="text-success" style="font-size: 1.5rem;">
                            S/ <span th:text="${total}"></span>
                        </strong>
                    </div>

                    <form th:action="@{/carrito/confirmar-compra}" method="post">
                        <button type="submit"
                                class="btn btn-success w-100 mb-2"
                                th:disabled="${#lists.isEmpty(items)}">
                            <i class="bi bi-check-circle"></i> Confirmar Compra
                        </button>
                    </form>

                    <a href="/cursos" class="btn btn-outline-secondary w-100">
                        <i class="bi bi-arrow-left"></i> Seguir comprando
                    </a>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="bg-dark text-white text-center py-4 mt-auto">
    <div th:replace="~{fragments/footer :: footer}"></div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

#### 3️⃣ Modificar GlobalControllerAdvice (para contador del carrito)

**Archivo**: `src/main/java/com/example/MDW/controller/GlobalControllerAdvice.java`

Agregar al inicio:

```java

@Autowired
private CarritoService carritoService;
```

Modificar el método `addCommonAttributes`:

```java

@ModelAttribute
public void addCommonAttributes(Model model, HttpSession session) {
    Persona persona = (Persona) session.getAttribute("personaLogueado");
    model.addAttribute("personaLogueado", persona);

    // Agregar contador del carrito
    if (persona != null && persona.getAlumno() != null) {
        int itemsCarrito = carritoService.contarItems(persona.getAlumno().getId());
        model.addAttribute("carritoCount", itemsCarrito);
    } else {
        model.addAttribute("carritoCount", 0);
    }
}
```

---

## 🟡 PERSONA 3: FRONTEND - BOTONES Y VISTA DE MÉTODO DE PAGO

### **Prioridad: BAJA - DEPENDE DE PERSONA 1 Y 2**

Esta persona mejora la interfaz de usuario y agrega el formulario de pago.

### Tareas Detalladas:

#### 1️⃣ Modificar header.html - Agregar ícono del carrito

**Archivo**: `src/main/resources/templates/fragments/header.html`

Buscar la sección del navbar y agregar antes del menú de usuario:

```html
<!-- Icono del Carrito (solo para alumnos logueados) -->
<li class="nav-item" th:if="${personaLogueado != null and personaLogueado.alumno != null}">
    <a class="nav-link position-relative" href="/carrito">
        <i class="bi bi-cart3" style="font-size: 1.5rem;"></i>
        <span th:if="${carritoCount > 0}"
              class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger"
              th:text="${carritoCount}">
            0
        </span>
    </a>
</li>
```

#### 2️⃣ Modificar cursos.html - Cambiar botón de inscripción por "Agregar al Carrito"

**Archivo**: `src/main/resources/templates/cursos.html`

Buscar la sección de botones de inscripción y reemplazar por:

```html
<!-- Botón: Agregar al Carrito (si NO está inscrito y NO está en el carrito) -->
<form th:if="${personaLogueado != null and personaLogueado.alumno != null}"
      th:action="@{/cursos/agregar-al-carrito}"
      method="post"
      class="d-inline">
    <input type="hidden" name="courseId" th:value="${curso.idCurso}">
    <button type="submit" class="btn btn-primary">
        <i class="bi bi-cart-plus"></i> Agregar al Carrito
    </button>
</form>

<!-- Si ya está inscrito -->
<span th:if="${cursosInscritosSidebar != null and #lists.contains(cursosInscritosSidebar, curso)}"
      class="badge bg-success">
    <i class="bi bi-check-circle"></i> Ya inscrito
</span>

<!-- Si no está logueado -->
<a th:if="${personaLogueado == null}"
   href="/login"
   class="btn btn-outline-primary">
    Inicia sesión para comprar
</a>
```

#### 3️⃣ Crear vista de método de pago (OPCIONAL)

**Archivo**: `src/main/resources/templates/pago.html`

```html
<!DOCTYPE html>
<html lang="es" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Método de Pago - MDW</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css" rel="stylesheet">
</head>
<body class="bg-light">

<header class="bg-dark shadow-sm">
    <div th:replace="~{fragments/header :: navbar}"></div>
</header>

<main class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0"><i class="bi bi-credit-card"></i> Método de Pago</h4>
                </div>
                <div class="card-body">

                    <!-- Resumen de compra -->
                    <div class="alert alert-info">
                        <h5>Resumen de tu compra</h5>
                        <p class="mb-1"><strong>Total a pagar:</strong> S/ <span th:text="${total}">0.00</span></p>
                        <p class="mb-0"><strong>Cursos:</strong> <span th:text="${#lists.size(items)}">0</span></p>
                    </div>

                    <!-- Formulario de pago -->
                    <form th:action="@{/carrito/procesar-pago}" method="post">

                        <h5 class="mb-3">Datos de la Tarjeta</h5>

                        <div class="mb-3">
                            <label for="numeroTarjeta" class="form-label">Número de Tarjeta</label>
                            <input type="text"
                                   class="form-control"
                                   id="numeroTarjeta"
                                   name="numeroTarjeta"
                                   placeholder="1234 5678 9012 3456"
                                   maxlength="19"
                                   required>
                        </div>

                        <div class="mb-3">
                            <label for="nombreTitular" class="form-label">Nombre del Titular</label>
                            <input type="text"
                                   class="form-control"
                                   id="nombreTitular"
                                   name="nombreTitular"
                                   placeholder="Como aparece en la tarjeta"
                                   required>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="fechaExpiracion" class="form-label">Fecha de Expiración</label>
                                <input type="text"
                                       class="form-control"
                                       id="fechaExpiracion"
                                       name="fechaExpiracion"
                                       placeholder="MM/AA"
                                       maxlength="5"
                                       required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="cvv" class="form-label">CVV</label>
                                <input type="text"
                                       class="form-control"
                                       id="cvv"
                                       name="cvv"
                                       placeholder="123"
                                       maxlength="3"
                                       required>
                            </div>
                        </div>

                        <div class="alert alert-warning">
                            <i class="bi bi-shield-check"></i>
                            Tu pago está protegido con cifrado SSL
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-success btn-lg">
                                <i class="bi bi-lock"></i> Pagar S/ <span th:text="${total}">0.00</span>
                            </button>
                            <a href="/carrito" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Volver al carrito
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<footer class="bg-dark text-white text-center py-4 mt-auto">
    <div th:replace="~{fragments/footer :: footer}"></div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<!-- Script para formatear número de tarjeta -->
<script>
    document.getElementById('numeroTarjeta').addEventListener('input', function (e) {
        let value = e.target.value.replace(/\s/g, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formattedValue;
    });

    document.getElementById('fechaExpiracion').addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.slice(0, 2) + '/' + value.slice(2, 4);
        }
        e.target.value = value;
    });
</script>
</body>
</html>
```

#### 4️⃣ Agregar método de pago en CarritoController (OPCIONAL)

**Archivo**: `src/main/java/com/example/MDW/controller/CarritoController.java`

Agregar estos métodos:

```java
// Mostrar página de pago
@GetMapping("/pago")
public String mostrarPago(Model model, HttpSession session) {
    Persona persona = (Persona) session.getAttribute("personaLogueado");

    if (persona == null || persona.getAlumno() == null) {
        return "redirect:/login";
    }

    Long idAlumno = persona.getAlumno().getId();
    List<CarritoItem> items = carritoService.obtenerCarrito(idAlumno);

    if (items.isEmpty()) {
        return "redirect:/carrito";
    }

    double total = carritoService.calcularTotal(idAlumno);

    model.addAttribute("items", items);
    model.addAttribute("total", total);
    model.addAttribute("personaLogueado", persona);

    return "pago";
}

// Procesar pago
@PostMapping("/procesar-pago")
public String procesarPago(@RequestParam String numeroTarjeta,
                           @RequestParam String nombreTitular,
                           @RequestParam String fechaExpiracion,
                           @RequestParam String cvv,
                           HttpSession session,
                           RedirectAttributes redirectAttrs) {

    // Aquí podrías integrar una pasarela de pago real
    // Por ahora solo validamos que los campos no estén vacíos

    if (numeroTarjeta.length() < 16 || cvv.length() != 3) {
        redirectAttrs.addFlashAttribute("error", "Datos de tarjeta inválidos.");
        return "redirect:/carrito/pago";
    }

    // Procesar la compra
    return confirmarCompra(session, redirectAttrs);
}
```

#### 5️⃣ Modificar botón en carrito.html para ir a pago

En `carrito.html`, cambiar el botón "Confirmar Compra" por:

```html
<a href="/carrito/pago"
   class="btn btn-success w-100 mb-2"
   th:classappend="${#lists.isEmpty(items)} ? 'disabled' : ''">
    <i class="bi bi-credit-card"></i> Proceder al Pago
</a>
```

---

## 📊 ORDEN DE IMPLEMENTACIÓN

### Semana 1:

- **PERSONA 1**: Completa toda la capa de modelo, repositorio y servicios
- Revisar en equipo que todo compile sin errores

### Semana 2:

- **PERSONA 2**: Implementa los controllers y vistas del carrito
- **PERSONA 3**: Inicia modificaciones en el frontend (botones)

### Semana 3:

- **PERSONA 3**: Completa vista de método de pago
- **TODO EL EQUIPO**: Pruebas y ajustes finales

---

## ✅ CHECKLIST DE INTEGRACIÓN

### Antes de empezar:

- [ ] Persona 1: Crear rama `feature/carrito-backend`
- [ ] Persona 2: Crear rama `feature/carrito-checkout`
- [ ] Persona 3: Crear rama `feature/carrito-frontend`

### Durante el desarrollo:

- [ ] Persona 1: Probar que los métodos del service funcionan
- [ ] Persona 2: Verificar que el carrito muestra cursos correctamente
- [ ] Persona 3: Probar que los botones agregan al carrito

### Al finalizar:

- [ ] Hacer merge de `feature/carrito-backend` → `develop`
- [ ] Hacer merge de `feature/carrito-checkout` → `develop`
- [ ] Hacer merge de `feature/carrito-frontend` → `develop`
- [ ] Pruebas de integración completas
- [ ] Merge a `main`

---

## 🔒 SEGURIDAD JWT Y SPRING SECURITY

### Validaciones importantes que TODAS las personas deben considerar:

1. **Verificar sesión en cada endpoint**:

```java
Persona persona = (Persona) session.getAttribute("personaLogueado");
if(persona ==null||persona.

getAlumno() ==null){
        return"redirect:/login";
        }
```

2. **Proteger endpoints POST con CSRF** (ya configurado):

- Spring Security maneja esto automáticamente
- Thymeleaf incluye el token CSRF en los formularios

3. **Validar propiedad de recursos**:

- Siempre verificar que el alumno solo acceda a SU carrito
- Usar `persona.getAlumno().getId()` de la sesión, nunca del parámetro

4. **Permisos en SecurityConfig**:

- `/cursos` → Público
- `/carrito/**` → Requiere autenticación
- `/cursos/agregar-al-carrito` → Requiere autenticación

---

## 🐛 ERRORES COMUNES A EVITAR

1. **No verificar si el curso ya está en inscripciones antes de agregar al carrito**
2. **No vaciar el carrito después de confirmar la compra**
3. **Permitir agregar cursos gratis al carrito (decidir si se permite o no)**
4. **No actualizar el contador del carrito en tiempo real**
5. **Olvidar agregar `@Transactional` en métodos que eliminan múltiples registros**

---

## 📞 COORDINACIÓN DEL EQUIPO

### Reuniones sugeridas:

- **Inicio**: Todos leen este documento juntos
- **Día 3**: Persona 1 muestra avances del backend
- **Día 7**: Persona 2 muestra la vista del carrito funcionando
- **Día 10**: Persona 3 muestra el frontend completo
- **Día 14**: Pruebas finales en equipo

### Comunicación:

- Usar un canal de Slack/WhatsApp para dudas rápidas
- Commits descriptivos: `[CARRITO] Agregar entity CarritoItem`
- Code review antes de cada merge

---

## 🎉 RESULTADO ESPERADO

Al finalizar, el usuario podrá:

1. ✅ Ver cursos y agregar al carrito sin inscribirse
2. ✅ Ver el ícono del carrito con el contador de items
3. ✅ Acceder al carrito y ver resumen de cursos + total
4. ✅ Eliminar cursos individuales del carrito
5. ✅ Confirmar la compra (opcionalmente pasando por página de pago)
6. ✅ Ver los cursos en "Mis Cursos" después de la compra
7. ✅ El carrito se vacía automáticamente

---

## 📝 NOTAS FINALES

- Este documento es una GUÍA COMPLETA con todo el código necesario
- Copiar y pegar con cuidado, respetando los paquetes
- Probar cada funcionalidad antes de integrar
- Mantener comunicación constante entre el equipo

**¡Éxito con el proyecto! 🚀**

