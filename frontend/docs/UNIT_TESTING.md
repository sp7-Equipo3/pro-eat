# Testing Unitario en Frontend - Guía Didáctica

Este documento explica la teoría y práctica del testing unitario en el frontend de Pro Eat, desde conceptos fundamentales hasta su aplicación práctica.

## Tabla de Contenidos

1. [¿Qué es el Testing Unitario?](#qué-es-el-testing-unitario)
2. [¿Por qué hacer Testing Unitario?](#por-qué-hacer-testing-unitario)
3. [Tecnologías para Testing en React](#tecnologías-para-testing-en-react)
4. [La Lógica del Testing Unitario](#la-lógica-del-testing-unitario)
5. [¿Qué Testear y Qué No?](#qué-testear-y-qué-no)
6. [Estrategia de Implementación](#estrategia-de-implementación)
7. [Conceptos Importantes](#conceptos-importantes)
8. [Ejemplos Conceptuales](#ejemplos-conceptuales)
9. [Preguntas para Reflexionar](#preguntas-para-reflexionar)

---

## ¿Qué es el Testing Unitario?

El **testing unitario** es una técnica donde verificas que una **unidad de código** (una función, un componente, un hook) funciona correctamente de forma **aislada**.

Piensa en esto como probar cada pieza de un motor antes de armarlo completamente. Si cada pieza funciona bien individualmente, es más probable que el motor completo funcione.

### Características de una Unidad

- **Aislada**: No depende de otras unidades para funcionar
- **Pequeña**: Una función, un componente, un hook
- **Rápida**: Se ejecuta en milisegundos
- **Determinística**: Mismo input = mismo output

---

## ¿Por qué hacer Testing Unitario?

### 1. **Detectar Errores Temprano**

Encontrar bugs cuando escribes el código, no cuando un usuario los reporta.

### 2. **Documentación Viva**

Los tests muestran cómo se usa tu código. Si alguien no entiende cómo funciona una función, puede leer los tests.

### 3. **Refactorizar con Confianza**

Si necesitas cambiar código, los tests te confirman que todo sigue funcionando.

### 4. **Mejor Diseño**

Escribir tests te obliga a pensar en casos límite y en cómo debería comportarse tu código.

### 5. **Menos Bugs en Producción**

Código testeado = menos errores = usuarios más felices = menos trabajo de emergencia.

---

## Tecnologías para Testing en React

Para este proyecto (React + Vite), las tecnologías recomendadas son:

### 1. **Vitest** (Runner de Tests)

- **Qué es**: El equivalente a Jest, pero optimizado para Vite
- **Por qué**: Es rápido, compatible con ESM, y funciona perfectamente con Vite sin configuración extra
- **Responsabilidad**: Ejecutar los tests y generar reportes

### 2. **React Testing Library** (Testing de Componentes)

- **Qué es**: Librería para testear componentes React
- **Filosofía**: "Testea comportamiento, no implementación"
- **Enfoque**: Simula cómo un usuario interactúa con tu UI
- **Responsabilidad**: Renderizar componentes y simular interacciones

### 3. **jsdom** (Entorno del Navegador)

- **Qué es**: Simula el DOM del navegador en Node.js
- **Por qué**: Permite probar componentes React sin necesidad de un navegador real
- **Responsabilidad**: Proporcionar APIs del navegador (localStorage, window, etc.)

### Stack Completo

```
Vitest → Ejecuta los tests
  ↓
React Testing Library → Renderiza componentes
  ↓
jsdom → Simula el navegador
```

---

## La Lógica del Testing Unitario

### 1. Estructura AAA (Arrange, Act, Assert)

Cada test sigue este patrón:

```
Arrange (Preparar): Configuras el estado inicial
  ↓
Act (Actuar): Ejecutas la función/componente que quieres probar
  ↓
Assert (Verificar): Compruebas que el resultado es el esperado
```

**Ejemplo Conceptual:**

```
Test: "getAuthToken debe retornar el token del localStorage"

Arrange: Guardar "mi-token-123" en localStorage
  ↓
Act: Llamar getAuthToken()
  ↓
Assert: El resultado debe ser "mi-token-123"
```

### 2. Casos de Prueba

Debes pensar en diferentes escenarios:

#### **Caso Feliz (Happy Path)**

Cuando todo funciona correctamente con datos válidos.

#### **Casos Límite (Edge Cases)**

Valores extremos o inesperados:

- `null`, `undefined`
- Strings vacíos `""`
- Arrays vacíos `[]`
- Números negativos o cero
- Strings muy largos

#### **Casos de Error**

Cuando algo debe fallar intencionalmente:

- Validaciones que deben rechazar datos inválidos
- Funciones que deben lanzar errores en ciertas condiciones

### 3. Aislamiento

Cada test debe ser:

- **Independiente**: No depende de otros tests
- **Reproducible**: Puede ejecutarse en cualquier orden
- **Aislado**: No afecta el estado global

**Regla de oro**: Si ejecutas los tests en orden aleatorio, deben pasar igual.

---

## ¿Qué Testear y Qué No?

### ✅ **SÍ Testear**

#### 1. **Lógica de Negocio**

Funciones que contienen reglas de tu aplicación.

**Ejemplo en tu código:**

- `authStorage.js`: Lógica de guardar/leer tokens
- `decodeJWT()`: Decodificación de tokens JWT

#### 2. **Transformaciones de Datos**

Funciones que transforman datos de un formato a otro.

**Ejemplo en tu código:**

- Funciones que formatean precios
- Funciones que normalizan respuestas de API

#### 3. **Validaciones**

Reglas de validación específicas de tu aplicación.

**Ejemplo en tu código:**

- `authValidators.js`: Schemas de Zod con tus reglas de negocio

#### 4. **Flujos Críticos del Usuario**

Funcionalidades que si fallan, afectan directamente al usuario.

**Ejemplo en tu código:**

- Login/Logout
- Creación de productos
- Filtros de búsqueda

### ❌ **NO Testear (o Testear Muy Poco)**

#### 1. **Librerías de Terceros**

No testees que Zod funciona, que React funciona, que Axios funciona. Ya están testeadas.

**❌ MAL:**

```javascript
// NO hagas esto
test('z.string() debe validar strings', () => {
  expect(z.string().parse('hola')).toBe('hola');
});
```

**✅ BIEN:**

```javascript
// SÍ haz esto: testea CÓMO usas Zod en tu app
test('loginSchema debe rechazar username con menos de 3 caracteres', () => {
  const result = loginSchema.safeParse({ username: 'ab', password: '123456' });
  expect(result.success).toBe(false);
});
```

#### 2. **Detalles de Implementación Interna**

No testees cómo está implementado algo, testea qué hace.

**❌ MAL:**

```javascript
// NO testees variables internas
test('debe usar useState', () => {
  // Esto es un detalle de implementación
});
```

**✅ BIEN:**

```javascript
// Testea el comportamiento visible
test('debe mostrar mensaje de error cuando el login falla', () => {
  // Testeas lo que el usuario ve
});
```

#### 3. **Estilos CSS**

A menos que los estilos afecten funcionalidad (como mostrar/ocultar elementos), no los testees.

---

## Aclaración Importante: Testear Zod vs Testear tu Uso de Zod

### 🔴 **NO Testear: Zod en sí mismo**

Zod ya está testado por sus creadores. No necesitas verificar que `z.string().min(3)` funciona correctamente.

### 🟢 **SÍ Testear: Cómo usas Zod en tu aplicación**

Lo que SÍ debes testear es:

- Que tu schema valida correctamente los datos que esperas
- Que tu schema rechaza los datos inválidos que no quieres
- Que los mensajes de error son los correctos para tu aplicación
- Que tus reglas de negocio (las que definiste) están bien implementadas

### Ejemplo Práctico

En `authValidators.js` tienes:

```javascript
export const loginSchema = z.object({
  username: z
    .string()
    .min(1, 'El username es obligatorio')
    .min(3, 'El username debe tener al menos 3 caracteres')
    .regex(
      /^[a-zA-Z0-9_]+$/,
      'El nombre de usuario solo puede contener letras, números y guiones bajos'
    ),
  password: z
    .string()
    .min(1, 'La contraseña es obligatoria')
    .min(6, 'La contraseña debe tener al menos 6 caracteres')
});
```

**❌ NO testees esto:**

- Que `z.string()` valida strings (Zod ya lo hace)
- Que `.min(3)` funciona (Zod ya lo hace)

**✅ SÍ testea esto:**

- Que tu schema acepta `{ username: 'alejandro', password: '123456' }` ✅
- Que tu schema rechaza `{ username: 'ab', password: '123456' }` ❌ (menos de 3 caracteres)
- Que tu schema rechaza `{ username: 'alejandro!', password: '123456' }` ❌ (caracteres especiales)
- Que tu schema rechaza `{ username: 'alejandro', password: '123' }` ❌ (menos de 6 caracteres)
- Que los mensajes de error son los que definiste

**¿Por qué?** Porque estas son **tus reglas de negocio**. Si mañana cambias la regla de "mínimo 3 caracteres" a "mínimo 5 caracteres", tu test debe fallar y avisarte que algo cambió.

---

## Estrategia de Implementación

Recomendación para implementar testing gradualmente:

### **Semana 1: Configuración**

- Instalar Vitest + React Testing Library
- Configurar el entorno de testing
- Escribir un test de ejemplo para verificar que todo funciona

### **Semana 2: Utilidades Puras** (Más Fácil)

- `authStorage.js`: Funciones que leen/escriben localStorage
- `utils.js`: Función `cn()` que combina clases

**Por qué empezar aquí:**

- Son funciones puras (mismo input = mismo output)
- No dependen de React
- Fáciles de testear
- Te dan confianza para seguir

### **Semana 3: Validadores**

- `authValidators.js`: Schemas de Zod
- `productValidators.js`: Schemas de validación de productos

**Por qué ahora:**

- Son lógica de negocio importante
- Relativamente fáciles de testear
- Alto impacto (si fallan, afectan directamente al usuario)

### **Semana 4: Servicios**

- `authService.js`: Llamadas a API
- `productService.js`: Operaciones CRUD

**Qué testear:**

- Que las funciones llamen a la API correcta
- Manejo de errores
- Transformación de datos

**Desafío:** Necesitarás mocks (simulaciones) de las llamadas a API.

### **Semana 5: Hooks Personalizados**

- `useAuth.js`: Lógica de autenticación
- `useFetch.js`: Fetching de datos

**Qué testear:**

- Estados que retornan
- Efectos secundarios
- Manejo de errores

**Desafío:** Necesitarás renderizar hooks (usando `renderHook` de React Testing Library).

### **Semana 6: Componentes Críticos**

- `LoginForm.jsx`: Formulario de login
- `ProductCard.jsx`: Tarjeta de producto

**Qué testear:**

- Renderizado correcto
- Interacciones del usuario (clicks, inputs)
- Estados visuales (loading, error, success)

**Desafío:** Más complejo, requiere más setup.

---

## Conceptos Importantes

### 1. **Mocks (Simulaciones)**

Un **mock** es una simulación de una dependencia externa.

**Ejemplo:**
Si quieres testear `authService.login()`, no quieres hacer una llamada real a la API (sería lento y podría fallar). En su lugar, **simulas** la respuesta de la API.

```javascript
// Mock de axios
vi.mock('axios', () => ({
  post: vi.fn(() => Promise.resolve({ data: { token: 'fake-token' } }))
}));
```

### 2. **Coverage (Cobertura)**

El **coverage** es el porcentaje de código que está siendo ejecutado por tus tests.

**Objetivo común:** 70-80% en código crítico.

**No obsesionarse:** 100% de coverage no significa 0 bugs. Es mejor tener tests buenos al 70% que tests malos al 100%.

### 3. **Test-Driven Development (TDD)**

**TDD** es escribir tests **antes** de escribir el código.

**Flujo:**

1. Escribes un test que falla (rojo)
2. Escribes el código mínimo para que pase (verde)
3. Refactorizas el código (refactor)

**No es obligatorio**, pero puede ser útil en algunos casos.

### 4. **Snapshots**

Los **snapshots** guardan una "foto" del output de un componente y la comparan en ejecuciones futuras.

**Útil para:** Detectar cambios visuales inesperados.

**Cuidado:** Pueden generar falsos positivos si cambias el diseño intencionalmente.

---

## Ejemplos Conceptuales

### Ejemplo 1: Testear `getAuthToken()`

**Función a testear:**

```javascript
export const getAuthToken = () => {
  return localStorage.getItem('token') || null;
};
```

**Tests conceptuales:**

```
Test 1: "debe retornar el token cuando existe en localStorage"
  Arrange: Guardar "mi-token-123" en localStorage
  Act: Llamar getAuthToken()
  Assert: Debe retornar "mi-token-123"

Test 2: "debe retornar null cuando no hay token"
  Arrange: Limpiar localStorage
  Act: Llamar getAuthToken()
  Assert: Debe retornar null

Test 3: "debe retornar null cuando el token es una string vacía"
  Arrange: Guardar "" en localStorage
  Act: Llamar getAuthToken()
  Assert: Debe retornar null (porque "" es falsy)
```

### Ejemplo 2: Testear `loginSchema`

**Schema a testear:**

```javascript
export const loginSchema = z.object({
  username: z.string().min(3, 'El username debe tener al menos 3 caracteres'),
  password: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres')
});
```

**Tests conceptuales:**

```
Test 1: "debe aceptar datos válidos"
  Arrange: { username: 'alejandro', password: '123456' }
  Act: Validar con loginSchema
  Assert: Debe ser válido

Test 2: "debe rechazar username con menos de 3 caracteres"
  Arrange: { username: 'ab', password: '123456' }
  Act: Validar con loginSchema
  Assert: Debe ser inválido con mensaje "El username debe tener al menos 3 caracteres"

Test 3: "debe rechazar password con menos de 6 caracteres"
  Arrange: { username: 'alejandro', password: '123' }
  Act: Validar con loginSchema
  Assert: Debe ser inválido con mensaje "La contraseña debe tener al menos 6 caracteres"

Test 4: "debe rechazar cuando username está vacío"
  Arrange: { username: '', password: '123456' }
  Act: Validar con loginSchema
  Assert: Debe ser inválido

Test 5: "debe rechazar cuando password está vacío"
  Arrange: { username: 'alejandro', password: '' }
  Act: Validar con loginSchema
  Assert: Debe ser inválido
```

### Ejemplo 3: Testear `cn()` (utils.js)

**Función a testear:**

```javascript
export function cn(...inputs) {
  return twMerge(clsx(inputs));
}
```

**Tests conceptuales:**

```
Test 1: "debe combinar clases correctamente"
  Arrange: cn('class1', 'class2')
  Act: Ejecutar función
  Assert: Debe retornar "class1 class2"

Test 2: "debe manejar clases condicionales"
  Arrange: cn('class1', condition && 'class2')
  Act: Ejecutar función
  Assert: Debe retornar solo "class1" si condition es false

Test 3: "debe eliminar clases duplicadas de Tailwind"
  Arrange: cn('p-4', 'p-6')
  Act: Ejecutar función
  Assert: Debe retornar solo "p-6" (la última prevalece)
```

---

## Preguntas para Reflexionar

Antes de escribir código, hazte estas preguntas. Las respuestas suelen convertirse en tests:

1. **¿Qué pasaría si un usuario ingresa un token inválido?**
   → Test: `setAuthToken()` con token inválido

2. **¿Qué pasa si la API falla?**
   → Test: `authService.login()` cuando la API retorna error

3. **¿Cómo se comporta el formulario con datos vacíos?**
   → Test: `LoginForm` con campos vacíos

4. **¿Qué pasa si el usuario hace click múltiples veces en "Enviar"?**
   → Test: Prevenir doble submit

5. **¿Qué pasa si el localStorage está lleno?**
   → Test: Manejo de errores de almacenamiento

6. **¿Cómo se comporta el componente cuando está en estado de loading?**
   → Test: Renderizado durante carga

7. **¿Qué pasa si el usuario ingresa caracteres especiales en el username?**
   → Test: Validación de regex en `loginSchema`

---

## Siguiente Paso

Una vez que entiendas estos conceptos, el siguiente paso es:

1. **Configurar el entorno de testing** (Vitest + React Testing Library)
2. **Escribir tu primer test** (empezar con `authStorage.js` o `utils.js`)
3. **Ir agregando tests gradualmente** siguiendo la estrategia de implementación

¿Listo para empezar? El siguiente documento práctico te guiará en la configuración e implementación de tests reales en tu proyecto.

---

## Recursos Adicionales

- [Documentación de Vitest](https://vitest.dev/)
- [Documentación de React Testing Library](https://testing-library.com/react)
- [Testing Best Practices](https://kentcdodds.com/blog/common-mistakes-with-react-testing-library)
- [Filosofía de React Testing Library](https://testing-library.com/docs/guiding-principles/)
