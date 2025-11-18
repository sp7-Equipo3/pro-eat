# Arquitectura del Proyecto

Este documento describe la arquitectura del proyecto backend de Pro-Eat, desarrollado con Spring Boot.

## 1. Descripción General

El proyecto sigue una arquitectura en capas, un patrón de diseño común para aplicaciones web que separa las responsabilidades en diferentes capas lógicas. Esto facilita el mantenimiento, la escalabilidad y las pruebas del código.

Las capas principales son:

- **Capa de Presentación (Controllers)**: Expone la API REST y maneja las solicitudes HTTP.
- **Capa de Servicio (Services)**: Contiene la lógica de negocio de la aplicación.
- **Capa de Acceso a Datos (Repositories)**: Se encarga de la comunicación con la base de datos.
- **Capa de Dominio (Models)**: Define las entidades del negocio.

Además, el proyecto incluye paquetes para la configuración, seguridad, manejo de excepciones, DTOs y utilidades.

## 2. Estructura de Paquetes

A continuación se detalla la estructura de paquetes y la responsabilidad de cada uno:

- **`com.example.spring`**: Paquete raíz de la aplicación.
  - **`config`**: Clases de configuración de Spring, OpenAPI (Swagger) y CORS.
    - `ApplicationConfig.java`: Configuración de beans de la aplicación, como `AuthenticationManager` y `PasswordEncoder`.
    - `CorsConfig.java`: Configuración global de CORS para permitir peticiones desde el frontend.
    - `OpenApiConfig.java`: Configuración de la documentación de la API con OpenAPI/Swagger.
  - **`controllers`**: Controladores REST que manejan las peticiones HTTP.
    - `AuthController.java`: Endpoints para autenticación (registro y login).
    - `ProductController.java`: Endpoints para operaciones CRUD de productos.
    - `UserController.java`: Endpoints para operaciones relacionadas con usuarios.
  - **`dtos`**: Data Transfer Objects (DTOs) para la comunicación entre el cliente y el servidor.
    - `auth`: DTOs para autenticación.
    - `product`: DTOs para productos.
    - `user`: DTOs para usuarios.
  - **`enums`**: Enumeraciones utilizadas en la aplicación.
    - `Role.java`: Define los roles de los usuarios (p. ej., `USER`, `ADMIN`).
  - **`exceptions`**: Clases para el manejo de excepciones personalizadas y un manejador global.
    - `GlobalExceptionHandler.java`: Captura y maneja las excepciones lanzadas en la aplicación, devolviendo respuestas de error estandarizadas.
  - **`mappers`**: Interfaces de MapStruct para mapear entre entidades y DTOs.
    - `UserMapper.java`: Mapea entre la entidad `User` y sus DTOs.
    - `ProductMapper.java`: Mapea entre la entidad `Product` y sus DTOs.
  - **`models`**: Entidades JPA que representan las tablas de la base de datos.
    - `User.java`: Entidad para los usuarios.
    - `Product.java`: Entidad para los productos.
    - `TokenBlacklist.java`: Entidad para almacenar tokens JWT invalidados (logout).
  - **`repositories`**: Interfaces de Spring Data JPA para el acceso a datos.
    - `UserRepository.java`: Repositorio para la entidad `User`.
    - `ProductRepository.java`: Repositorio para la entidad `Product`.
    - `TokenBlacklistRepository.java`: Repositorio para la lista negra de tokens.
  - **`security`**: Clases relacionadas con la seguridad de la aplicación.
    - `config/SecurityConfig.java`: Configuración de la seguridad de Spring, incluyendo las reglas de autorización de endpoints.
    - `filter/JwtAuthenticationFilter.java`: Filtro que intercepta las peticiones para validar el token JWT.
    - `service/JwtService.java`: Servicio para la creación y validación de tokens JWT.
    - `service/TokenBlacklistService.java`: Servicio para gestionar la lista negra de tokens.
  - **`services`**: Lógica de negocio de la aplicación.
    - `impl`: Implementaciones de las interfaces de servicio.
      - `AuthServiceImpl.java`: Implementación de la lógica de autenticación.
      - `ProductServiceImpl.java`: Implementación de la lógica de negocio para productos.
      - `UserServiceImpl.java`: Implementación de la lógica de negocio para usuarios.
  - **`utils`**: Clases de utilidad.
    - `ApiResult.java`: Clase para estandarizar las respuestas de la API.

## 3. Flujo de una Petición

1.  Un cliente (p. ej., una aplicación frontend) envía una petición HTTP a un endpoint de la API (p. ej., `POST /api/auth/register`).
2.  El `DispatcherServlet` de Spring recibe la petición y la dirige al controlador correspondiente (`AuthController`).
3.  Si el endpoint está protegido, el `JwtAuthenticationFilter` intercepta la petición, valida el token JWT y establece el contexto de seguridad.
4.  El método del controlador recibe la petición y, si es necesario, un DTO con los datos (`RegisterRequestDto`).
5.  El controlador llama al método correspondiente en la capa de servicio (`authService.register(...)`).
6.  El servicio ejecuta la lógica de negocio (p. ej., valida los datos, codifica la contraseña, etc.).
7.  El servicio utiliza los mappers para convertir DTOs a entidades (`userMapper.toUser(...)`).
8.  El servicio utiliza los repositorios para interactuar con la base de datos (`userRepository.save(...)`).
9.  El repositorio, a través de JPA/Hibernate, ejecuta las sentencias SQL correspondientes.
10. La base de datos devuelve los resultados.
11. El servicio procesa los resultados y, si es necesario, los mapea de nuevo a DTOs.
12. El servicio devuelve el resultado al controlador.
13. El controlador envuelve el resultado en una respuesta HTTP (`ResponseEntity`) y la devuelve al cliente.
14. Si se produce una excepción en cualquier punto, el `GlobalExceptionHandler` la captura y devuelve una respuesta de error formateada.

## 4. Seguridad

La seguridad se gestiona con Spring Security y JWT (JSON Web Tokens).

- **Autenticación**: Se realiza a través del endpoint `/api/auth/login`. Si las credenciales son válidas, se genera un token JWT que se devuelve al cliente.
- **Autorización**: El `JwtAuthenticationFilter` valida el token en cada petición a endpoints protegidos. La anotación `@PreAuthorize` se puede usar en los métodos de los controladores para restringir el acceso basado en roles.
- **Logout**: Se implementa una lista negra de tokens. Al hacer logout, el token se añade a esta lista y no puede ser reutilizado.

## 5. Documentación de la API

La API está documentada con OpenAPI 3 (anteriormente Swagger). La documentación se puede consultar en la ruta `/swagger-ui.html` cuando la aplicación está en ejecución.

## 6. Manejo de Errores

El proyecto implementa un manejo de errores centralizado a través de la clase `GlobalExceptionHandler.java`. Esta clase, anotada con `@ControllerAdvice`, intercepta las excepciones lanzadas en cualquier parte de la aplicación y las procesa para devolver respuestas de error consistentes y amigables al cliente.

Características clave del manejo de errores:

-   **Excepciones Personalizadas**: Se han definido excepciones personalizadas (p. ej., `ResourceNotFoundException`, `DuplicateResourceException`, `BadRequestException`) para representar escenarios de error específicos de la lógica de negocio.
-   **Respuestas Estandarizadas**: Todas las respuestas de error siguen un formato estandarizado, generalmente incluyendo un mensaje descriptivo, un código de error y un timestamp, utilizando la clase `ErrorResponse`.
-   **Validación de Entradas**: Los errores de validación de entradas (p. ej., `@Valid` en DTOs) son capturados y se devuelve una respuesta detallada indicando qué campos son inválidos.
-   **Códigos de Estado HTTP**: Se utilizan los códigos de estado HTTP apropiados para cada tipo de error (p. ej., 404 Not Found, 400 Bad Request, 409 Conflict, 500 Internal Server Error).