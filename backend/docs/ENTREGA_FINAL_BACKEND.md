# Documento de Entrega Final - Backend Pro Eat

**Proyecto:** Sistema POS Gastronómico - Backend (Prueba de Concepto)  
**Tipo:** POC (Proof of Concept) - Demostración Representativa  
**Fecha de Entrega:** Noviembre 2025  
**Rama del Repositorio:** `main` (o la rama principal de tu backend)  
**Deploy:** [pro-eat.onrender.com]
**Repositorio:** [https://github.com/sp7-Equipo3/pro-eat/tree/main](https://github.com/sp7-Equipo3/pro-eat/tree/main) (ajusta la rama si es necesario)

> **Nota:** Este proyecto es una **Prueba de Concepto (POC)** desarrollada a modo representativo para demostrar las capacidades técnicas y arquitectónicas del sistema. No es una aplicación de producción completa.

---

## Tabla de Contenidos

1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Tecnologías Utilizadas](#tecnologías-utilizadas)
3. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
4. [Features Implementados](#features-implementados)
5. [Testing](#testing)
6. [Manejo de Errores](#manejo-de-errores)
7. [Documentación de la API](#documentación-de-la-api)
8. [Despliegue](#despliegue)
9. [Estructura del Proyecto](#estructura-del-proyecto)
10. [Enlaces Importantes](#enlaces-importantes)

---

## Resumen Ejecutivo

El backend de **Pro Eat** es una **Prueba de Concepto (POC)** desarrollada con Spring Boot que expone una API RESTful para el sistema POS gastronómico. Esta aplicación representativa maneja la lógica de negocio, la persistencia de datos, la autenticación y autorización de usuarios, y la gestión de recursos como productos. Sirve como demostración de las capacidades técnicas y arquitectónicas del sistema backend.

### Características Principales (POC)

- ✅ **API RESTful** para la gestión de recursos.
- ✅ **Autenticación y Autorización** basada en JWT (JSON Web Tokens).
- ✅ **Gestión de Usuarios**: Registro, login y roles.
- ✅ **Gestión de Productos**: Operaciones CRUD completas.
- ✅ **Manejo de Excepciones** centralizado y respuestas estandarizadas.
- ✅ **Configuración de CORS** para permitir la comunicación con el frontend.
- ✅ **Persistencia de Datos** con JPA y PostgreSQL.
- ✅ **Documentación de API** con OpenAPI (Swagger UI).
- ✅ **Arquitectura en capas** para facilitar el mantenimiento y la escalabilidad.

### Estado del Proyecto

El proyecto es una **Prueba de Concepto funcional** desplegada en producción para su demostración. Los features principales han sido implementados de forma representativa, testeados y documentados para mostrar las capacidades técnicas del sistema.

---

## Tecnologías Utilizadas

### Core Framework y Lenguaje

- **Spring Boot 3.5** - Framework principal para el desarrollo de aplicaciones Java.
- **Java 17** - Lenguaje de programación.
- **Maven** - Herramienta de gestión de proyectos y construcción.

### Persistencia de Datos

- **Spring Data JPA** - Abstracción para el acceso a datos.
- **Hibernate** - Implementación de JPA.
- **PostgreSQL** - Base de datos relacional.
- **HikariCP** - Pool de conexiones a la base de datos.

### Seguridad

- **Spring Security** - Framework de seguridad para aplicaciones Spring.
- **JWT (JSON Web Tokens)** - Para autenticación y autorización sin estado.
- **jjwt** - Librería para la implementación de JWT.

### Mapeo de Objetos

- **MapStruct** - Generador de código para mapeo de objetos (DTOs a Entidades y viceversa).

### Utilidades y Otros

- **Lombok** - Para reducir el código repetitivo (getters, setters, constructores).
- **Springdoc OpenAPI** - Para la generación automática de documentación de API (Swagger UI).
- **dotenv-java** - Para cargar variables de entorno desde archivos `.env` (en desarrollo).
- **Jakarta Validation** - Para la validación de datos de entrada.

---

## Arquitectura del Proyecto

El backend sigue una arquitectura en capas bien definida, como se detalla en el archivo <mcfile name="DOCUMENTATION.md" path="c:\Users\alexa\OneDrive\Documentos\pro-eat\backend\spring\DOCUMENTATION.md"></mcfile>. Los principios clave incluyen:

-   **Separación de Responsabilidades**: Cada capa tiene un propósito específico.
-   **Modularidad**: El código está organizado en paquetes lógicos.
-   **Inyección de Dependencias**: Gestionada por el contenedor de Spring.

Las capas principales son:

-   **Capa de Presentación (`controllers`)**: Expone la API REST.
-   **Capa de Servicio (`services`)**: Contiene la lógica de negocio.
-   **Capa de Acceso a Datos (`repositories`)**: Interactúa con la base de datos.
-   **Capa de Dominio (`models`)**: Define las entidades del negocio.

---

## Features Implementados

### 1. Autenticación y Autorización (Auth)

#### Funcionalidades

-   ✅ **Registro de Usuarios**: Creación de nuevas cuentas con roles predefinidos.
-   ✅ **Login de Usuarios**: Autenticación mediante credenciales y generación de JWT.
-   ✅ **Logout**: Invalidación de tokens JWT mediante una lista negra.
-   ✅ **Rutas Protegidas**: Acceso restringido a endpoints basado en roles y autenticación.
-   ✅ **Validación de Entradas**: Validación de DTOs de registro y login.

#### Componentes Clave

-   `AuthController`: Endpoints para registro, login y logout.
-   `AuthService`, `AuthServiceImpl`: Lógica de negocio de autenticación.
-   `JwtService`: Generación y validación de JWT.
-   `JwtAuthenticationFilter`: Filtro de seguridad para validar tokens.
-   `TokenBlacklistService`: Gestión de tokens invalidados.

### 2. Gestión de Productos (Products)

#### Funcionalidades

-   ✅ **CRUD de Productos**: Creación, lectura, actualización y eliminación de productos.
-   ✅ **Listado de Productos**: Paginación y filtrado de productos.
-   ✅ **Validación de Entradas**: Validación de DTOs para la creación y actualización de productos.

#### Componentes Clave

-   `ProductController`: Endpoints para la gestión de productos.
-   `ProductService`, `ProductServiceImpl`: Lógica de negocio de productos.
-   `ProductRepository`: Acceso a datos de productos.
-   `ProductMapper`: Mapeo entre DTOs y entidades de producto.

### 3. Gestión de Usuarios (Users)

#### Funcionalidades

-   ✅ **Listado de Usuarios**: Obtención de información de usuarios (con restricciones de seguridad).
-   ✅ **Roles de Usuario**: Definición y asignación de roles (`USER`, `ADMIN`).

#### Componentes Clave

-   `UserController`: Endpoints para la gestión de usuarios.
-   `UserService`, `UserServiceImpl`: Lógica de negocio de usuarios.
-   `UserRepository`: Acceso a datos de usuarios.
-   `UserMapper`: Mapeo entre DTOs y entidades de usuario.

### 4. Configuración de CORS

#### Funcionalidades

-   ✅ **Configuración Global de CORS**: Permite la comunicación segura con el frontend.
-   ✅ **Flexibilidad de Orígenes**: Configuración para permitir orígenes específicos o todos (con advertencia de seguridad).

#### Componentes Clave

-   `CorsConfig.java`: Clase de configuración para las políticas de CORS.

---

## Manejo de Errores

El backend implementa un manejo de errores centralizado para proporcionar respuestas consistentes y significativas al cliente.

### Características

-   **`GlobalExceptionHandler`**: Intercepta y procesa excepciones en toda la aplicación.
-   **Excepciones Personalizadas**: Definición de excepciones específicas para escenarios de negocio (ej. `ResourceNotFoundException`, `DuplicateResourceException`).
-   **Respuestas Estandarizadas**: Utilización de `ErrorResponse` para formatear los mensajes de error.
-   **Códigos de Estado HTTP**: Uso de códigos de estado HTTP apropiados (400, 401, 403, 404, 409, 500).

---

## Documentación de la API

La API está documentada utilizando **Springdoc OpenAPI**, lo que permite generar automáticamente una interfaz de usuario interactiva (Swagger UI).

-   **Acceso**: La documentación de la API está disponible en la ruta `/swagger-ui.html` cuando la aplicación está en ejecución.
-   **Anotaciones**: Se utilizan anotaciones de Swagger (`@Operation`, `@ApiResponse`, `@Tag`, etc.) en los controladores y DTOs para enriquecer la documentación.

---

## Despliegue

### Plataforma de Despliegue

-   **Render** - Plataforma de hosting para el despliegue continuo del backend.

### URL de Demostración

**[pro-eat.onrender.com]**

### Configuración de Despliegue

El proyecto está configurado para ser desplegado en Render, utilizando un `Procfile` y variables de entorno.

### Variables de Entorno

Para el despliegue en Render, se deben configurar las siguientes variables de entorno:

-   `SPRING_DATASOURCE_URL`: URL de conexión a la base de datos PostgreSQL.
-   `SPRING_DATASOURCE_USERNAME`: Nombre de usuario de la base de datos.
-   `SPRING_DATASOURCE_PASSWORD`: Contraseña de la base de datos.
-   `JWT_SECRET_KEY`: Clave secreta para firmar y verificar los JWT.
-   `CORS_ALLOWED_ORIGINS`: Orígenes permitidos para CORS (ej. `https://pro-eat.vercel.app`).

### Proceso de Despliegue

1.  Configurar las variables de entorno en Render.
2.  Push a la rama principal del repositorio de Git.
3.  Render detecta los cambios y realiza el build y despliegue automático.

---