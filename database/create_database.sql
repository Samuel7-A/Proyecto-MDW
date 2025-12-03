-- ==========================================
-- SCRIPT SQL PARA CREAR BASE DE DATOS
-- Proyecto MDW - MySQL 8.0+
-- ==========================================

-- 1. Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS mdw
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 2. Usar la base de datos
USE mdw;

-- 3. Mostrar mensaje de éxito
SELECT 'Base de datos mdw creada exitosamente' AS mensaje;

-- ==========================================
-- NOTAS IMPORTANTES:
-- ==========================================
-- * Las tablas se crean automáticamente por Hibernate al ejecutar Spring Boot
-- * Configuración: spring.jpa.hibernate.ddl-auto=update en application.properties
-- * No es necesario crear las tablas manualmente
-- * Usuario por defecto: a@a.com / contraseña: 123 (se crea al iniciar la app)
-- ==========================================

-- Si quieres eliminar la base de datos y empezar de cero:
-- DROP DATABASE IF EXISTS proyecto_mdw;

