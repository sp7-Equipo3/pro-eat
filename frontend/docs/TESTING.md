# Testing Unitario en Frontend - Guía Completa

Este documento explica la teoría y práctica del testing unitario en el frontend de Pro Eat, desde conceptos fundamentales hasta técnicas avanzadas como mocks.

## Tabla de Contenidos

1. [¿Qué es el Testing Unitario?](#qué-es-el-testing-unitario)
2. [¿Por qué hacer Testing Unitario?](#por-qué-hacer-testing-unitario)
3. [Tecnologías para Testing en React](#tecnologías-para-testing-en-react)
4. [La Lógica del Testing Unitario](#la-lógica-del-testing-unitario)
5. [¿Qué Testear y Qué No?](#qué-testear-y-qué-no)
6. [Estrategia de Implementación](#estrategia-de-implementación)
7. [Mocks y Simulaciones](#mocks-y-simulaciones)
8. [Ejemplos Prácticos](#ejemplos-prácticos)
9. [Mejores Prácticas](#mejores-prácticas)
10. [Errores Comunes](#errores-comunes)
11. [Preguntas para Reflexionar](#preguntas-para-reflexionar)

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

## Mocks y Simulaciones

### ¿Qué es un Mock?

Un **mock** (simulación) es una versión falsa de un módulo, función o dependencia que reemplazas durante los tests para controlar su comportamiento.

### Analogía Simple

Imagina que estás probando un auto:
- **Sin mock**: Necesitas un motor real, gasolina real, carretera real
- **Con mock**: Simulas el motor, la gasolina y la carretera para probar solo el auto

En testing es igual:
- **Sin mock**: Necesitas API real, localStorage real, navegación real
- **Con mock**: Simulas todo para probar solo tu código

### ¿Por qué usar Mocks?

#### Problema sin Mocks

Cuando testeas `LoginForm`, internamente usa:

```javascript
// LoginForm.jsx
const login = useLogin({ ... })  // ← Llama a API real
setAuthToken(token)              // ← Escribe en localStorage real
navigate('/dashboard')           // ← Navega realmente
```

**Problemas:**
1. **Lento**: Cada test haría llamadas reales a la API
2. **Inestable**: Si la API está caída, tus tests fallan
3. **Efectos secundarios**: Escribe en localStorage real
4. **Complejo**: Necesitas servidor funcionando
5. **Costoso**: Consume recursos reales

#### Solución con Mocks

Con mocks, controlas todo:

```javascript
// En el test
vi.mock('../hooks/useAuth.js')  // ← Reemplaza useLogin con versión falsa
vi.mock('../utils/authStorage.js')  // ← Reemplaza setAuthToken con versión falsa

// Ahora puedes controlar qué hacen
useAuth.useLogin.mockReturnValue({
  mutate: mockMutate,
  isPending: false,
})
```

**Ventajas:**
1. **Rápido**: No hace llamadas reales
2. **Estable**: Siempre funciona igual
3. **Aislado**: No afecta el sistema real
4. **Simple**: No necesitas servidor
5. **Controlable**: Puedes simular cualquier escenario

### Cómo Funciona `vi.mock`

`vi.mock` es una función de Vitest que reemplaza un módulo con una versión simulada durante los tests.

#### Sintaxis Básica

```javascript
vi.mock('ruta/al/modulo.js')
```

Esto hace que:
- Todas las exportaciones del módulo se conviertan en funciones mock
- Puedas controlar qué retornan
- Puedas verificar si fueron llamadas

#### Ejemplo Simple

```javascript
// Código real (LoginForm.jsx)
import { useLogin } from '../hooks/useAuth.js'

const login = useLogin({ ... })  // ← Esto normalmente llama a la API
```

```javascript
// En el test
vi.mock('../hooks/useAuth.js')  // ← Reemplaza el módulo

// Ahora useLogin es una función mock que puedes controlar
useAuth.useLogin.mockReturnValue({
  mutate: vi.fn(),
  isPending: false,
})
```

### Tipos de Mocks

#### 1. Mock Básico (Reemplaza Todo)

```javascript
vi.mock('../hooks/useAuth.js')
```

**Qué hace:**
- Reemplaza todas las exportaciones con funciones mock vacías
- Necesitas configurar qué retornan manualmente

**Cuándo usar:**
- Cuando quieres control total sobre todas las exportaciones

**Ejemplo:**
```javascript
vi.mock('../hooks/useAuth.js')

// Luego configuras manualmente
useAuth.useLogin.mockReturnValue({ mutate: vi.fn() })
useAuth.useRegister.mockReturnValue({ mutate: vi.fn() })
```

#### 2. Mock con Implementación Personalizada

```javascript
vi.mock('@/shared/services/apiClient.js', () => ({
  default: {
    post: vi.fn(),
    get: vi.fn(),
    put: vi.fn(),
  },
}))
```

**Qué hace:**
- Reemplaza el módulo con una implementación específica
- Define la estructura exacta que quieres

**Cuándo usar:**
- Cuando necesitas una estructura específica
- Cuando el módulo tiene export default

**Ejemplo:**
```javascript
vi.mock('@/shared/services/apiClient.js', () => ({
  default: {
    post: vi.fn(),
  },
}))

// Ahora apiClient.post es una función mock
apiClient.post.mockResolvedValue({ data: { token: 'fake' } })
```

#### 3. Mock Parcial (Mantiene Algo Real)

```javascript
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,  // ← Mantiene todo lo real
    useNavigate: () => mockNavigate,  // ← Solo reemplaza useNavigate
    useLocation: () => mockLocation,  // ← Solo reemplaza useLocation
  }
})
```

**Qué hace:**
- Importa el módulo real primero
- Solo reemplaza las partes que necesitas
- Mantiene el resto funcionando normalmente

**Cuándo usar:**
- Cuando solo necesitas mockear algunas funciones
- Cuando quieres mantener el comportamiento del resto

**Ejemplo:**
```javascript
// Necesitas BrowserRouter real, pero useNavigate mockeado
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,  // BrowserRouter, Link, etc. funcionan normal
    useNavigate: () => mockNavigate,  // Solo esto es mock
  }
})
```

### Funciones de Vitest para Mocks

#### `vi.fn()` - Crea una Función Mock

Crea una función falsa que puedes controlar y verificar.

```javascript
const mockFunction = vi.fn()

// Puedes hacer que retorne algo
mockFunction.mockReturnValue('resultado')
mockFunction()  // Retorna 'resultado'

// Puedes hacer que retorne una promesa exitosa
mockFunction.mockResolvedValue({ data: 'success' })
await mockFunction()  // Retorna { data: 'success' }

// Puedes hacer que lance un error
mockFunction.mockRejectedValue(new Error('Error'))
await mockFunction()  // Lanza Error

// Puedes verificar si fue llamada
expect(mockFunction).toHaveBeenCalled()
expect(mockFunction).toHaveBeenCalledWith('argumento')
expect(mockFunction).toHaveBeenCalledTimes(2)
```

#### `mockReturnValue()` - Define Qué Retorna

Configura qué retorna una función mock cuando se llama.

```javascript
useAuth.useLogin.mockReturnValue({
  mutate: mockMutate,
  isPending: false,
  isError: false,
})
```

**Ejemplo práctico:**
```javascript
// En LoginForm.test.jsx
useAuth.useLogin.mockReturnValue({
  mutate: mockMutate,
  isPending: false,
})

// Cuando LoginForm hace:
const login = useLogin({ ... })

// En realidad obtiene:
const login = {
  mutate: mockMutate,
  isPending: false,
}
```

#### `mockResolvedValue()` - Para Promesas Exitosas

Configura qué retorna una promesa cuando se resuelve exitosamente.

```javascript
apiClient.post.mockResolvedValue({
  data: { token: 'fake-token', user: { username: 'test' } }
})

// Cuando se llama:
const result = await apiClient.post('/api/login', credentials)
// result = { data: { token: 'fake-token', user: { username: 'test' } } }
```

**Ejemplo práctico:**
```javascript
// En authService.test.js
apiClient.post.mockResolvedValue({
  data: { token: 'fake-token' }
})

// Cuando authService.login() hace:
const response = await apiClient.post('/api/auth/login', credentials)
// response = { data: { token: 'fake-token' } }
```

#### `mockRejectedValue()` - Para Promesas que Fallan

Configura qué error lanza una promesa cuando falla.

```javascript
apiClient.post.mockRejectedValue(new Error('Invalid credentials'))

// Cuando se llama:
try {
  await apiClient.post('/api/login', credentials)
} catch (error) {
  // error = Error('Invalid credentials')
}
```

**Ejemplo práctico:**
```javascript
// En authService.test.js
apiClient.post.mockRejectedValue(new Error('Network error'))

// Cuando testeas el manejo de errores:
await expect(login(credentials)).rejects.toThrow('Network error')
```

#### `mockImplementation()` - Implementación Personalizada

Define una función completa personalizada.

```javascript
useAuth.useLogin.mockImplementation((options) => {
  // Tu lógica personalizada aquí
  setTimeout(() => {
    options.onSuccess({ token: 'fake-token' })
  }, 0)
  
  return {
    mutate: mockMutate,
    isPending: false,
  }
})
```

**Cuándo usar:**
- Cuando necesitas lógica compleja
- Cuando quieres simular comportamiento asíncrono
- Cuando necesitas llamar callbacks

**Ejemplo práctico:**
```javascript
// Simular login exitoso después de un delay
useAuth.useLogin.mockImplementation((options) => {
  setTimeout(() => {
    options.onSuccess({
      success: true,
      data: { token: 'fake-token' }
    })
  }, 0)
  
  return { mutate: mockMutate, isPending: false }
})
```

#### `mockClear()` y `clearAllMocks()` - Limpiar Mocks

Limpia el historial de llamadas de los mocks.

```javascript
beforeEach(() => {
  vi.clearAllMocks()  // ← Limpia todos los mocks
  mockNavigate.mockClear()  // ← Limpia solo este mock
})
```

**Por qué es importante:**
- Cada test debe empezar limpio
- Evita que un test afecte a otro
- Los contadores de llamadas se resetean

### Cuándo Usar Mocks

#### ✅ **SÍ Mockear**

1. **Llamadas a API**
   - Son lentas
   - Dependen de servicios externos
   - Pueden fallar por razones ajenas a tu código

2. **Funciones con Efectos Secundarios**
   - `localStorage.setItem()` (aunque jsdom lo simula)
   - `navigate()` de React Router
   - `window.location`

3. **Dependencias Externas**
   - Librerías de terceros que hacen llamadas
   - Servicios que escriben archivos
   - Funciones que envían emails

4. **Código Lento**
   - Funciones que hacen cálculos pesados
   - Operaciones de base de datos
   - Procesamiento de imágenes

5. **Para Testear Casos de Error**
   - Simular errores de red
   - Simular timeouts
   - Simular respuestas inválidas

#### ❌ **NO Mockear**

1. **Tu Propio Código Simple**
   - Funciones puras (mismo input = mismo output)
   - Utilidades matemáticas
   - Transformaciones de datos

2. **Librerías de Terceros Bien Testeadas**
   - React (ya está testeado)
   - Zod (ya está testeado)
   - Librerías estándar

3. **Código que Quieres Testear Realmente**
   - La lógica de negocio que estás testando
   - Las funciones que quieres verificar

### Conceptos Adicionales

#### Coverage (Cobertura)

El **coverage** es el porcentaje de código que está siendo ejecutado por tus tests.

**Objetivo común:** 70-80% en código crítico.

**No obsesionarse:** 100% de coverage no significa 0 bugs. Es mejor tener tests buenos al 70% que tests malos al 100%.

#### Test-Driven Development (TDD)

**TDD** es escribir tests **antes** de escribir el código.

**Flujo:**

1. Escribes un test que falla (rojo)
2. Escribes el código mínimo para que pase (verde)
3. Refactorizas el código (refactor)

**No es obligatorio**, pero puede ser útil en algunos casos.

#### Snapshots

Los **snapshots** guardan una "foto" del output de un componente y la comparan en ejecuciones futuras.

**Útil para:** Detectar cambios visuales inesperados.

**Cuidado:** Pueden generar falsos positivos si cambias el diseño intencionalmente.

---

## Ejemplos Prácticos

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

### Ejemplo 3: Mock de Hook Personalizado

**Código real:**
```javascript
// LoginForm.jsx
import { useLogin } from '../hooks/useAuth.js'

const login = useLogin({
  onSuccess: (data) => {
    setAuthToken(data.token)
  }
})
```

**Test:**
```javascript
// LoginForm.test.jsx
import * as useAuth from '../hooks/useAuth.js'

// 1. Mock el módulo
vi.mock('../hooks/useAuth.js')

describe('LoginForm', () => {
  const mockMutate = vi.fn()

  beforeEach(() => {
    // 2. Configura qué retorna useLogin
    useAuth.useLogin.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })
  })

  test('debe llamar a login.mutate', async () => {
    render(<LoginForm />)
    
    // 3. Simula interacción del usuario
    await user.type(usernameInput, 'test')
    await user.type(passwordInput, '123456')
    await user.click(submitButton)

    // 4. Verifica que fue llamado
    expect(mockMutate).toHaveBeenCalledWith({
      username: 'test',
      password: '123456',
    })
  })
})
```

### Ejemplo 4: Mock de Servicio API

**Código real:**
```javascript
// authService.js
import apiClient from '@/shared/services/apiClient.js'

export const login = async (credentials) => {
  const response = await apiClient.post('/api/auth/login', credentials)
  return response.data
}
```

**Test:**
```javascript
// authService.test.js
import apiClient from '@/shared/services/apiClient.js'

// 1. Mock el módulo con estructura específica
vi.mock('@/shared/services/apiClient.js', () => ({
  default: {
    post: vi.fn(),
  },
}))

describe('authService', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  test('debe llamar a apiClient.post correctamente', async () => {
    // 2. Configura qué retorna
    apiClient.post.mockResolvedValue({
      data: { token: 'fake-token' }
    })

    // 3. Ejecuta la función
    const result = await login({ username: 'test', password: '123456' })

    // 4. Verifica que fue llamado correctamente
    expect(apiClient.post).toHaveBeenCalledWith(
      '/api/auth/login',
      { username: 'test', password: '123456' }
    )
    
    // 5. Verifica el resultado
    expect(result).toEqual({ token: 'fake-token' })
  })
})
```

### Ejemplo 5: Mock Parcial de React Router

**Código real:**
```javascript
// LoginForm.jsx
import { useNavigate, useLocation } from 'react-router-dom'

const navigate = useNavigate()
const location = useLocation()
```

**Test:**
```javascript
// LoginForm.test.jsx
const mockNavigate = vi.fn()
const mockLocation = { state: { from: { pathname: '/dashboard' } } }

// 1. Mock parcial: mantiene BrowserRouter real, mockea hooks
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,  // BrowserRouter, Link, etc. funcionan normal
    useNavigate: () => mockNavigate,
    useLocation: () => mockLocation,
  }
})

test('debe navegar después de login exitoso', async () => {
  // Configura mock de useLogin para éxito
  useAuth.useLogin.mockImplementation((options) => {
    setTimeout(() => {
      options.onSuccess({ success: true, data: { token: 'fake' } })
    }, 0)
    return { mutate: mockMutate, isPending: false }
  })

  render(<LoginForm />)
  
  // Simula login
  await user.type(usernameInput, 'test')
  await user.type(passwordInput, '123456')
  await user.click(submitButton)

  // Verifica navegación
  await waitFor(() => {
    expect(mockNavigate).toHaveBeenCalledWith('/dashboard', { replace: true })
  })
})
```

### Ejemplo 6: Flujo Completo de un Test con Mocks

```javascript
// 1. IMPORTS
import { describe, test, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '@testing-library/react'
import { LoginForm } from './LoginForm.jsx'
import * as useAuth from '../hooks/useAuth.js'

// 2. MOCKS (al inicio del archivo)
vi.mock('../hooks/useAuth.js')
vi.mock('../utils/authStorage.js')

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

// 3. SETUP
describe('LoginForm', () => {
  const mockMutate = vi.fn()

  beforeEach(() => {
    // 4. LIMPIAR MOCKS
    vi.clearAllMocks()
    
    // 5. CONFIGURAR MOCKS
    useAuth.useLogin.mockReturnValue({
      mutate: mockMutate,
      isPending: false,
    })
  })

  // 6. TESTS
  test('debe funcionar correctamente', async () => {
    // 7. RENDERIZAR
    render(<LoginForm />)
    
    // 8. INTERACTUAR
    await user.type(usernameInput, 'test')
    await user.click(submitButton)
    
    // 9. VERIFICAR
    expect(mockMutate).toHaveBeenCalledWith({
      username: 'test',
      password: '123456',
    })
  })
})
```

---

## Mejores Prácticas

### 1. Limpiar Mocks Entre Tests

```javascript
beforeEach(() => {
  vi.clearAllMocks()  // ← Siempre limpia antes de cada test
})
```

**Por qué:**
- Cada test debe ser independiente
- Evita que un test afecte a otro
- Los contadores se resetean

### 2. Mockear en el Nivel Correcto

**✅ BIEN:**
```javascript
// Mock el servicio, no la función que estás testando
vi.mock('@/shared/services/apiClient.js')
```

**❌ MAL:**
```javascript
// No mockees la función que quieres testear
vi.mock('./authService.js')  // ← Esto no tiene sentido
```

### 3. Usar `mockImplementation` para Lógica Compleja

```javascript
// Cuando necesitas simular comportamiento asíncrono
useAuth.useLogin.mockImplementation((options) => {
  setTimeout(() => {
    options.onSuccess({ token: 'fake' })
  }, 0)
  return { mutate: mockMutate, isPending: false }
})
```

### 4. Verificar Llamadas con Argumentos Correctos

```javascript
// ✅ BIEN: Verifica argumentos específicos
expect(apiClient.post).toHaveBeenCalledWith(
  '/api/auth/login',
  { username: 'test', password: '123456' }
)

// ❌ MAL: Solo verifica que fue llamado
expect(apiClient.post).toHaveBeenCalled()  // ← Muy vago
```

### 5. Mock Parcial cuando Sea Posible

```javascript
// ✅ BIEN: Solo mockea lo necesario
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

// ❌ MAL: Mockea todo cuando no es necesario
vi.mock('react-router-dom')  // ← Pierdes BrowserRouter real
```

### 6. Colocar Mocks al Inicio del Archivo

**✅ BIEN:**
```javascript
vi.mock('./modulo.js')  // ← Al inicio del archivo

test('mi test', () => {
  // Tu test aquí
})
```

**❌ MAL:**
```javascript
test('mi test', () => {
  vi.mock('./modulo.js')  // ← Muy tarde, debe estar al inicio
})
```

---

## Errores Comunes

### Error 1: Mock en el Lugar Incorrecto

**❌ MAL:**
```javascript
test('mi test', () => {
  vi.mock('./modulo.js')  // ← Muy tarde, debe estar al inicio
})
```

**✅ BIEN:**
```javascript
vi.mock('./modulo.js')  // ← Al inicio del archivo

test('mi test', () => {
  // Tu test aquí
})
```

### Error 2: No Limpiar Mocks

**❌ MAL:**
```javascript
describe('mi suite', () => {
  test('test 1', () => {
    mockFunction.mockReturnValue('valor1')
    // ...
  })
  
  test('test 2', () => {
    // mockFunction todavía tiene el valor de test 1
  })
})
```

**✅ BIEN:**
```javascript
describe('mi suite', () => {
  beforeEach(() => {
    vi.clearAllMocks()  // ← Limpia antes de cada test
  })
  
  test('test 1', () => {
    mockFunction.mockReturnValue('valor1')
  })
  
  test('test 2', () => {
    // mockFunction está limpio
  })
})
```

### Error 3: Mockear lo que Estás Testeando

**❌ MAL:**
```javascript
// Estás testando authService, pero lo mockeas
vi.mock('./authService.js')

test('login debe funcionar', () => {
  // Esto no tiene sentido, estás testando el mock, no el código real
})
```

**✅ BIEN:**
```javascript
// Mock el servicio que usa authService
vi.mock('@/shared/services/apiClient.js')

test('login debe funcionar', () => {
  // Ahora sí testeas authService real, pero con apiClient mockeado
})
```

### Error 4: Olvidar `await` en Promesas

**❌ MAL:**
```javascript
apiClient.post.mockResolvedValue({ data: 'success' })
const result = apiClient.post()  // ← Falta await
expect(result).toBe('success')  // ← result es una Promise, no 'success'
```

**✅ BIEN:**
```javascript
apiClient.post.mockResolvedValue({ data: 'success' })
const result = await apiClient.post()  // ← Con await
expect(result.data).toBe('success')
```

### Error 5: Mock Parcial Mal Hecho

**❌ MAL:**
```javascript
vi.mock('react-router-dom', () => ({
  useNavigate: () => mockNavigate,
  // ← Falta el resto, BrowserRouter no funcionará
}))
```

**✅ BIEN:**
```javascript
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,  // ← Mantiene todo lo demás
    useNavigate: () => mockNavigate,
  }
})
```

### Error 6: Comparación: Con vs Sin Mocks

**Sin Mocks (Problemas):**
```javascript
// Test sin mocks
test('login debe funcionar', async () => {
  render(<LoginForm />)
  
  // ❌ Hace llamada REAL a la API
  // ❌ Escribe en localStorage REAL
  // ❌ Intenta navegar REALMENTE
  // ❌ Depende de servidor funcionando
  // ❌ Lento (espera respuesta de red)
  // ❌ Puede fallar por razones ajenas
})
```

**Con Mocks (Solución):**
```javascript
// Test con mocks
vi.mock('../hooks/useAuth.js')

test('login debe funcionar', async () => {
  useAuth.useLogin.mockReturnValue({
    mutate: vi.fn(),
    isPending: false,
  })
  
  render(<LoginForm />)
  
  // ✅ No hace llamadas reales
  // ✅ No escribe en localStorage real
  // ✅ No navega realmente
  // ✅ No depende de servidor
  // ✅ Rápido (inmediato)
  // ✅ Siempre funciona igual
})
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

## Resumen de Comandos Útiles

### Crear Mocks

```javascript
vi.fn()                    // Función mock vacía
vi.fn(() => 'valor')       // Función mock que retorna valor
vi.mock('./modulo.js')     // Mock módulo completo
```

### Configurar Mocks

```javascript
mockFn.mockReturnValue('valor')           // Retorna valor
mockFn.mockResolvedValue({ data })       // Promesa exitosa
mockFn.mockRejectedValue(new Error())     // Promesa que falla
mockFn.mockImplementation(() => { ... })  // Implementación personalizada
```

### Verificar Mocks

```javascript
expect(mockFn).toHaveBeenCalled()                    // Fue llamado
expect(mockFn).toHaveBeenCalledWith('arg')          // Fue llamado con arg
expect(mockFn).toHaveBeenCalledTimes(2)             // Fue llamado 2 veces
expect(mockFn.mock.calls[0][0]).toEqual('arg')      // Primer argumento
```

### Limpiar Mocks

```javascript
vi.clearAllMocks()        // Limpia todos los mocks
mockFn.mockClear()        // Limpia un mock específico
mockFn.mockReset()        // Limpia y resetea implementación
```

---

## Preguntas Frecuentes

### ¿Cuándo debo usar `mockReturnValue` vs `mockResolvedValue`?

- **`mockReturnValue`**: Para funciones síncronas
  ```javascript
  mockFn.mockReturnValue('valor')  // Retorna inmediatamente
  ```

- **`mockResolvedValue`**: Para funciones asíncronas (promesas)
  ```javascript
  mockFn.mockResolvedValue({ data: 'valor' })  // Retorna Promise
  await mockFn()  // { data: 'valor' }
  ```

### ¿Debo mockear siempre las dependencias?

No. Solo mockea:
- Dependencias externas (API, localStorage, navegación)
- Código lento o inestable
- Funciones con efectos secundarios

No mockees:
- Tu código que quieres testear
- Funciones puras simples
- Librerías bien testeadas

### ¿Cómo sé qué mockear?

Pregúntate:
1. ¿Esta función hace algo fuera de mi control? → Mock
2. ¿Esta función es lenta? → Mock
3. ¿Esta función tiene efectos secundarios? → Mock
4. ¿Esta función es lo que quiero testear? → NO mock

### ¿Los mocks afectan el código de producción?

**NO**. Los mocks solo existen durante los tests. Tu código de producción nunca los ve.

---

## Recursos Adicionales

- [Documentación de Vitest](https://vitest.dev/)
- [Documentación de React Testing Library](https://testing-library.com/react)
- [Testing Best Practices](https://kentcdodds.com/blog/common-mistakes-with-react-testing-library)
- [Filosofía de React Testing Library](https://testing-library.com/docs/guiding-principles/)
- [Documentación de Vitest - Mocks](https://vitest.dev/guide/mocking.html)

---

## Siguiente Paso

Una vez que entiendas estos conceptos, el siguiente paso es:

1. **Configurar el entorno de testing** (Vitest + React Testing Library)
2. **Escribir tu primer test** (empezar con `authStorage.js` o `utils.js`)
3. **Ir agregando tests gradualmente** siguiendo la estrategia de implementación

¿Listo para empezar? Este documento te guía en la configuración e implementación de tests reales en tu proyecto.

