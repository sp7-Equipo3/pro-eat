# Arquitectura del Frontend - Pro Eat

Este documento describe la arquitectura del proyecto frontend y cómo usar los hooks y servicios disponibles.

## Tabla de Contenidos

1. [Estructura del Proyecto](#estructura-del-proyecto)
2. [Variables Globales y Configuración](#variables-globales-y-configuración)
3. [Hooks Disponibles](#hooks-disponibles)
4. [Servicios](#servicios)
5. [Manejo de Errores](#manejo-de-errores)
6. [Ejemplos Prácticos](#ejemplos-prácticos)
7. [Buenas Prácticas](#buenas-prácticas)

---

## Estructura del Proyecto

El proyecto sigue una arquitectura **Feature-First** (también conocida como Screaming Architecture), donde cada feature es autocontenido y tiene su propia estructura:

```
src/
├── features/              # Features/dominios de negocio
│   ├── auth/              # Feature de autenticación
│   │   ├── components/    # Componentes específicos del feature
│   │   ├── pages/         # Páginas del feature
│   │   ├── routes/        # Rutas del feature
│   │   ├── hooks/         # Hooks específicos del feature
│   │   ├── services/      # Lógica de negocio/API del feature
│   │   └── constants.js   # Constantes del feature
│   │
│   └── home/              # Feature de página principal
│       ├── pages/
│       ├── routes/
│       └── hooks/
│
├── shared/                # Código compartido entre features
│   ├── components/        # Componentes UI reutilizables
│   │   ├── ui/           # Componentes shadcn/ui
│   │   └── layout/       # Layouts compartidos
│   ├── hooks/            # Hooks genéricos reutilizables
│   ├── services/         # Servicios compartidos (apiClient)
│   └── utils/            # Utilidades genéricas
│
├── infrastructure/        # Configuración e infraestructura
│   ├── api/              # Configuración de APIs
│   ├── router/           # Configuración de rutas
│   └── constants/        # Variables globales
│
└── lib/                  # Librerías/configuraciones externas
```

### Principios de la Arquitectura

- **Separación de responsabilidades**: Lógica de negocio (`services/`) separada de la UI (`components/`, `pages/`)
- **Encapsulación**: Cada feature contiene todo lo necesario para funcionar
- **Reutilización**: Código compartido en `shared/`
- **Escalabilidad**: Fácil agregar nuevos features sin modificar existentes

---

## Variables Globales y Configuración

### `infrastructure/constants/env.js`

Este archivo contiene todas las variables globales del proyecto:

```javascript
import {
  API_BASE_URL,
  API_TIMEOUT,
  DEFAULT_HEADERS,
  getAuthToken,
  setAuthToken
} from '@/infrastructure/constants/env.js';
```

**Variables disponibles:**

- `API_BASE_URL`: URL base de la API (por defecto desde `VITE_API_BASE_URL` o fallback)
- `API_TIMEOUT`: Timeout para peticiones (30000ms por defecto)
- `DEFAULT_HEADERS`: Headers por defecto para todas las peticiones
- `getAuthToken()`: Obtiene el token de autenticación del localStorage
- `setAuthToken(token)`: Guarda o elimina el token del localStorage

**Configuración:**

Para configurar la URL base de la API, crea un archivo `.env` en la raíz del proyecto:

```env
VITE_API_BASE_URL=https://api.tu-dominio.com
```

---

## Hooks Disponibles

### `useFetch` - Hook Simple para Peticiones GET

**Ubicación:** `shared/hooks/useFetch.js`

**Propósito:** Hook simple para hacer peticiones GET sin caché. Ideal para casos simples donde no necesitas las características avanzadas de React Query.

**Uso:**

```javascript
import { useFetch } from '@/shared/hooks/useFetch.js';

function MyComponent() {
  const { data, loading, error, refetch } = useFetch('/api/users');

  if (loading) return <div>Cargando...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      <h1>{data?.name}</h1>
      <button onClick={refetch}>Recargar</button>
    </div>
  );
}
```

**Retorna:**

- `data`: Datos recibidos de la API
- `loading`: Boolean indicando si está cargando
- `error`: Objeto de error si ocurrió un problema
- `refetch`: Función para volver a ejecutar la petición

**Cuándo usar:**

- ✅ Peticiones simples y puntuales
- ✅ No necesitas caché
- ✅ No necesitas refetch automático
- ❌ Si necesitas mutaciones (POST, PUT, DELETE)
- ❌ Si necesitas invalidar queries después de mutaciones

---

### `useApi` - Hooks con React Query

**Ubicación:** `shared/hooks/useApi.js`

**Propósito:** Hooks wrapper de React Query que proporcionan funcionalidades avanzadas como caché, invalidación de queries, y manejo de mutaciones.

#### `useApiQuery` - Para Peticiones GET con Caché

**Uso:**

```javascript
import { useApiQuery } from '@/shared/hooks/useApi.js';

function ProductsList() {
  const { data, isLoading, error, refetch, isFetching } = useApiQuery(
    ['products'], // queryKey (identificador único para caché)
    '/api/products' // URL del endpoint
  );

  if (isLoading) return <div>Cargando productos...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return (
    <div>
      {data?.map(product => (
        <div key={product.id}>{product.name}</div>
      ))}
      {isFetching && <div>Actualizando...</div>}
    </div>
  );
}
```

**Opciones adicionales:**

```javascript
const { data } = useApiQuery(['products'], '/api/products', {
  staleTime: 5 * 60 * 1000, // Datos válidos por 5 minutos
  enabled: shouldFetch, // Condición para ejecutar la query
  refetchOnWindowFocus: true // Refetch al enfocar la ventana
});
```

**Cuándo usar:**

- ✅ Necesitas caché de datos
- ✅ Quieres evitar peticiones redundantes
- ✅ Necesitas invalidar queries después de mutaciones
- ✅ Quieres refetch automático bajo ciertas condiciones

---

#### `useApiPost` - Para Peticiones POST

**Uso:**

```javascript
import { useApiPost } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';

function CreateProduct() {
  const queryClient = useQueryClient();

  const createProduct = useApiPost('/api/products', {
    onSuccess: data => {
      // Invalidar la query de productos para refrescar la lista
      queryClient.invalidateQueries(['products']);
      console.log('Producto creado:', data);
    },
    onError: error => {
      console.error('Error al crear producto:', error.message);
    }
  });

  const handleSubmit = formData => {
    createProduct.mutate(formData);
  };

  return (
    <form
      onSubmit={e => {
        e.preventDefault();
        handleSubmit({ name: 'Nuevo producto', price: 100 });
      }}
    >
      <button type='submit' disabled={createProduct.isPending}>
        {createProduct.isPending ? 'Creando...' : 'Crear Producto'}
      </button>
    </form>
  );
}
```

**Retorna:**

- `mutate(data)`: Función para ejecutar la mutación
- `mutateAsync(data)`: Versión async de mutate (retorna Promise)
- `isPending`: Boolean indicando si está ejecutando
- `isError`: Boolean indicando si hubo un error
- `error`: Objeto de error si ocurrió un problema
- `data`: Datos retornados después del éxito
- `reset()`: Función para resetear el estado

---

#### `useApiPut` - Para Peticiones PUT

**Uso:**

```javascript
import { useApiPut } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';

function EditProduct({ productId }) {
  const queryClient = useQueryClient();

  const updateProduct = useApiPut(`/api/products/${productId}`, {
    onSuccess: () => {
      queryClient.invalidateQueries(['products']);
      queryClient.invalidateQueries(['product', productId]);
    }
  });

  const handleUpdate = formData => {
    updateProduct.mutate(formData);
  };

  return (
    <button onClick={() => handleUpdate({ name: 'Producto actualizado' })}>
      Actualizar
    </button>
  );
}
```

---

#### `useApiPatch` - Para Peticiones PATCH

**Uso:**

```javascript
import { useApiPatch } from '@/shared/hooks/useApi.js';

const updateUser = useApiPatch('/api/users/123', {
  onSuccess: () => {
    console.log('Usuario actualizado');
  }
});

updateUser.mutate({ name: 'Nuevo nombre' });
```

---

#### `useApiDelete` - Para Peticiones DELETE

**Uso:**

```javascript
import { useApiDelete } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';

function DeleteProduct({ productId }) {
  const queryClient = useQueryClient();

  const deleteProduct = useApiDelete(`/api/products/${productId}`, {
    onSuccess: () => {
      queryClient.invalidateQueries(['products']);
    }
  });

  const handleDelete = () => {
    if (confirm('¿Estás seguro de eliminar este producto?')) {
      deleteProduct.mutate();
    }
  };

  return (
    <button onClick={handleDelete} disabled={deleteProduct.isPending}>
      {deleteProduct.isPending ? 'Eliminando...' : 'Eliminar'}
    </button>
  );
}
```

---

#### `useApiMutation` - Para Mutaciones Personalizadas

**Uso:**

```javascript
import { useApiMutation } from '@/shared/hooks/useApi.js';
import apiClient from '@/shared/services/apiClient.js';

// Mutación personalizada
const customMutation = useApiMutation(
  async data => {
    // Lógica personalizada
    const response = await apiClient.post('/api/custom', data);
    return response.data;
  },
  {
    onSuccess: data => {
      console.log('Éxito:', data);
    }
  }
);

customMutation.mutate({ custom: 'data' });
```

---

## Servicios

### `apiClient` - Cliente HTTP Configurado

**Ubicación:** `shared/services/apiClient.js`

**Propósito:** Cliente axios preconfigurado con manejo de errores y configuración base.

**Uso directo:**

```javascript
import apiClient from '@/shared/services/apiClient.js';

// GET
const { data } = await apiClient.get('/api/users');

// POST
const { data } = await apiClient.post('/api/users', { name: 'Juan' });

// PUT
const { data } = await apiClient.put('/api/users/123', {
  name: 'Juan Updated'
});

// PATCH
const { data } = await apiClient.patch('/api/users/123', {
  name: 'Juan PATCH'
});

// DELETE
const { data } = await apiClient.delete('/api/users/123');
```

**Nota:** En la mayoría de los casos, es mejor usar los hooks (`useApiQuery`, `useApiPost`, etc.) en lugar de `apiClient` directamente.

**Cuándo usar directamente:**

- ✅ En funciones de servicio dentro de un feature
- ✅ Cuando necesitas hacer peticiones fuera de componentes React
- ✅ Para lógica de negocio que no está relacionada con el estado del componente

---

## Manejo de Errores

### Manejo Automático

Todos los hooks y servicios manejan errores automáticamente. Los errores se transforman usando `errorHandler` y tienen esta estructura:

```javascript
{
  message: "Mensaje de error amigable",
  type: "client" | "server" | "network",
  status: 400 | 401 | 404 | 500 | null,
  data: { /* datos adicionales del error */ }
}
```

### Tipos de Errores

- **Network Error**: Cuando no hay conexión o el servidor no responde
- **Client Error (4xx)**: Errores del cliente (400, 401, 403, 404, etc.)
- **Server Error (5xx)**: Errores del servidor (500, 502, etc.)

### Ejemplo de Manejo de Errores

```javascript
const { data, error } = useApiQuery(['users'], '/api/users');

if (error) {
  if (error.type === 'network') {
    // Mostrar mensaje de conexión
  } else if (error.status === 401) {
    // Redirigir al login
  } else {
    // Mostrar mensaje de error genérico
  }
}
```

---

## Ejemplos Prácticos

### Ejemplo 1: Login con `useApiPost`

```javascript
// features/auth/pages/LoginPage.jsx
import { useApiPost } from '@/shared/hooks/useApi.js';
import { useNavigate } from 'react-router-dom';
import { setAuthToken } from '@/infrastructure/constants/env.js';

function LoginPage() {
  const navigate = useNavigate();

  const login = useApiPost('/api/auth/login', {
    onSuccess: data => {
      setAuthToken(data.token);
      navigate('/home');
    },
    onError: error => {
      alert(error.message);
    }
  });

  const handleSubmit = e => {
    e.preventDefault();
    const formData = {
      email: e.target.email.value,
      password: e.target.password.value
    };
    login.mutate(formData);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input name='email' type='email' />
      <input name='password' type='password' />
      <button type='submit' disabled={login.isPending}>
        {login.isPending ? 'Iniciando sesión...' : 'Iniciar Sesión'}
      </button>
    </form>
  );
}
```

### Ejemplo 2: Lista de Productos con Invalidación

```javascript
// features/home/pages/HomePage.jsx
import { useApiQuery, useApiPost } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';

function HomePage() {
  const queryClient = useQueryClient();

  // Obtener productos (con caché)
  const { data: products, isLoading } = useApiQuery(
    ['products'],
    '/api/products'
  );

  // Crear producto
  const createProduct = useApiPost('/api/products', {
    onSuccess: () => {
      // Invalidar y refrescar la lista de productos
      queryClient.invalidateQueries(['products']);
    }
  });

  const handleCreate = () => {
    createProduct.mutate({ name: 'Nuevo producto', price: 100 });
  };

  if (isLoading) return <div>Cargando...</div>;

  return (
    <div>
      <button onClick={handleCreate}>Crear Producto</button>
      <ul>
        {products?.map(product => (
          <li key={product.id}>{product.name}</li>
        ))}
      </ul>
    </div>
  );
}
```

### Ejemplo 3: Servicio dentro de un Feature

```javascript
// features/auth/services/authService.js
import apiClient from '@/shared/services/apiClient.js';

export const login = async credentials => {
  const { data } = await apiClient.post('/api/auth/login', credentials);
  return data;
};

export const register = async userData => {
  const { data } = await apiClient.post('/api/auth/register', userData);
  return data;
};

export const logout = async () => {
  await apiClient.post('/api/auth/logout');
};

// Usar en un hook del feature
// features/auth/hooks/useAuth.js
import {
  login as loginService,
  register as registerService
} from '../services/authService.js';
import { useApiMutation } from '@/shared/hooks/useApi.js';

export const useAuth = () => {
  const login = useApiMutation(loginService);
  const register = useApiMutation(registerService);

  return { login, register };
};
```

---

## Buenas Prácticas

### 1. Usar Hooks en Componentes

✅ **Bien:**

```javascript
const { data } = useApiQuery(['users'], '/api/users');
```

❌ **Evitar:**

```javascript
const [data, setData] = useState(null);
useEffect(() => {
  apiClient.get('/api/users').then(res => setData(res.data));
}, []);
```

### 2. Invalidar Queries Después de Mutaciones

✅ **Bien:**

```javascript
const createProduct = useApiPost('/api/products', {
  onSuccess: () => {
    queryClient.invalidateQueries(['products']);
  }
});
```

### 3. Usar QueryKeys Consistentes

✅ **Bien:**

```javascript
['products'][('product', productId)][('users', userId, 'posts')]; // Lista // Item específico // Anidado
```

### 4. Separar Lógica de Negocio

✅ **Bien:**

```javascript
// services/authService.js
export const login = async credentials => {
  return await apiClient.post('/api/auth/login', credentials);
};

// hooks/useAuth.js
export const useLogin = () => {
  return useApiMutation(login);
};
```

### 5. Manejar Estados de Loading y Error

✅ **Bien:**

```javascript
const { data, isLoading, error } = useApiQuery(['users'], '/api/users');

if (isLoading) return <Loading />;
if (error) return <Error message={error.message} />;
return <UsersList users={data} />;
```

### 6. Usar useFetch Solo para Casos Simples

- ✅ Usar `useFetch` si solo necesitas una petición GET simple
- ✅ Usar `useApi` si necesitas caché, mutaciones o invalidación de queries

---

## Resumen Rápido

| Hook             | Propósito              | Cuándo Usar                       |
| ---------------- | ---------------------- | --------------------------------- |
| `useFetch`       | GET simple sin caché   | Peticiones puntuales y simples    |
| `useApiQuery`    | GET con caché          | Listados, datos que se reutilizan |
| `useApiPost`     | POST                   | Crear recursos                    |
| `useApiPut`      | PUT                    | Actualizar recursos completos     |
| `useApiPatch`    | PATCH                  | Actualizar recursos parciales     |
| `useApiDelete`   | DELETE                 | Eliminar recursos                 |
| `useApiMutation` | Mutación personalizada | Casos especiales                  |

---

## Recursos Adicionales

- [React Query Documentation](https://tanstack.com/query/latest)
- [Axios Documentation](https://axios-http.com/)
- [React Router Documentation](https://reactrouter.com/)

---

¿Necesitas ayuda? Revisa los ejemplos o consulta con el equipo.
