# 🚀 Level-Up - E-commerce de Videojuegos y Tecnología

**Level-Up** es una aplicación móvil diseñada para la asignatura de Desarrollo de Aplicaciones Móviles. Consiste en una tienda virtual (e-commerce) que integra una aplicación Android moderna construida con Jetpack Compose y un backend robusto desarrollado con Spring Boot.

## 👥 Información Académica
* **Asignatura:** Desarrollo de Aplicaciones Móviles
* **Profesor:** Ronald Villalobos
* **Integrantes:** * Maximiliano Kresse
    * Vicente Pacheco
    * Alonzo Vergara

---

## 🛠️ Tecnologías Utilizadas

### Frontend (Android App)
* **Lenguaje:** Kotlin
* **Interfaz:** Jetpack Compose (Kotlin Compose Plugin)
* **Arquitectura:** ViewModel y Navigation Compose
* **Red:** Retrofit 2 con conversor GSON para comunicación API
* **Imágenes:** Coil-compose para carga de imágenes asíncronas

### Backend (API REST)
* **Framework:** Spring Boot 3.3.0 (Starter Web, Data JPA)
* **Lenguaje:** Kotlin
* **Base de Datos:** H2 Database (en memoria para desarrollo)
* **Gestión de dependencias:** Gradle (Kotlin DSL)

---

## ⚙️ Configuración y Requisitos

### Backend
1. **Acceso a la Consola DB:** Al ejecutar el servidor, puedes acceder a la consola de H2 en `http://localhost:8080/h2-console`.
2. **Credenciales por defecto:**
   - **JDBC URL:** `jdbc:h2:mem:leveldb`
   - **Usuario:** `sa`
   - **Contraseña:** `123`

### Aplicación Android
* **SDK Mínimo:** API 24 (Android 7.0).
* **SDK de Compilación:** API 34.
* **Java:** Versión 11 para el compilador de la App y Versión 17 para el Backend.

---

## 🚀 Pasos para la Instalación y Uso

Sigue estos pasos para poner en marcha el proyecto:

### 1. Preparar el Backend
1. Navega a la carpeta `/level-up` (donde reside el servidor Spring Boot).
2. Ejecuta el servidor usando Gradle:
   ```bash
   ./gradlew bootRun
