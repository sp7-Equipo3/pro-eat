# Mocks y Testing - Guía Completa

Este documento explica qué son los mocks, por qué se usan en testing, y cómo aplicarlos en el frontend de Pro Eat usando Vitest.

## Tabla de Contenidos

1. [¿Qué es un Mock?](#qué-es-un-mock)
2. [¿Por qué usar Mocks?](#por-qué-usar-mocks)
3. [Cómo Funciona `vi.mock`](#cómo-funciona-vimock)
4. [Tipos de Mocks](#tipos-de-mocks)
5. [Funciones de Vitest para Mocks](#funciones-de-vitest-para-mocks)
6. [Ejemplos Prácticos](#ejemplos-prácticos)
7. [Cuándo Usar Mocks](#cuándo-usar-mocks)
8. [Mejores Prácticas](#mejores-prácticas)
9. [Errores Comunes](#errores-comunes)

---

## ¿Qué es un Mock?

Un **mock** (simulación) es una versión falsa de un módulo, función o dependencia que reemplazas durante los tests para controlar su comportamiento.

### Analogía Simple

Imagina que estás probando un auto:
- **Sin mock**: Necesitas un motor real, gasolina real, carretera real
- **Con mock**: Simulas el motor, la gasolina y la carretera para probar solo el auto

En testing es igual:
- **Sin mock**: Necesitas API real, localStorage real, navegación real
- **Con mock**: Simulas todo para probar solo tu código

---

## ¿Por qué usar Mocks?

### Problema sin Mocks

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

### Solución con Mocks

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

---

## Cómo Funciona `vi.mock`

`vi.mock` es una función de Vitest que reemplaza un módulo con una versión simulada durante los tests.

### Sintaxis Básica

```javascript
vi.mock('ruta/al/modulo.js')
```

Esto hace que:
- Todas las exportaciones del módulo se conviertan en funciones mock
- Puedas controlar qué retornan
- Puedas verificar si fueron llamadas

### Ejemplo Simple

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

---

## Tipos de Mocks

### 1. Mock Básico (Reemplaza Todo)

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

### 2. Mock con Implementación Personalizada

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

### 3. Mock Parcial (Mantiene Algo Real)

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

---

## Funciones de Vitest para Mocks

### `vi.fn()` - Crea una Función Mock

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

### `mockReturnValue()` - Define Qué Retorna

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

### `mockResolvedValue()` - Para Promesas Exitosas

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

### `mockRejectedValue()` - Para Promesas que Fallan

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

### `mockImplementation()` - Implementación Personalizada

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

### `mockClear()` y `clearAllMocks()` - Limpiar Mocks

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

---

## Ejemplos Prácticos

### Ejemplo 1: Mock de Hook Personalizado

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

### Ejemplo 2: Mock de Servicio API

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

### Ejemplo 3: Mock Parcial de React Router

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

### Ejemplo 4: Mock de Utilidades (localStorage)

**Código real:**
```javascript
// authStorage.js
export const setAuthToken = (token) => {
  localStorage.setItem('token', token)
}

export const getAuthToken = () => {
  return localStorage.getItem('token') || null
}
```

**Test:**
```javascript
// authStorage.test.js
import * as authStorage from './authStorage.js'

// No necesitas mock aquí porque jsdom simula localStorage
// Pero si quisieras mockearlo:

vi.mock('./authStorage.js', () => ({
  setAuthToken: vi.fn(),
  getAuthToken: vi.fn(),
}))

test('debe guardar token', () => {
  authStorage.setAuthToken('fake-token')
  
  expect(authStorage.setAuthToken).toHaveBeenCalledWith('fake-token')
})
```

---

## Cuándo Usar Mocks

### ✅ **SÍ Mockear**

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

### ❌ **NO Mockear**

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

---

## Flujo Completo de un Test con Mocks

### Paso a Paso

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

## Comparación: Con vs Sin Mocks

### Sin Mocks (Problemas)

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

### Con Mocks (Solución)

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

- [Documentación de Vitest - Mocks](https://vitest.dev/guide/mocking.html)
- [Testing Library - Mocks](https://testing-library.com/docs/react-testing-library/example-intro)
- [Guía de Testing Unitario](./UNIT_TESTING.md) - Documento complementario

---

## Conclusión

Los mocks son herramientas poderosas que te permiten:
- Testear código de forma aislada
- Simular cualquier escenario (éxito, error, timeout)
- Hacer tests rápidos y determinísticos
- Evitar efectos secundarios en el sistema real

La clave está en saber **qué mockear** y **cómo configurarlo** correctamente. Con práctica, se vuelve natural identificar qué necesita ser mockeado en cada test.

