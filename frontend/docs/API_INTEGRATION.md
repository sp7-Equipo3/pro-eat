# Integración con la API Backend

## Tabla de Contenidos

- [Configuración](#configuración)
- [Arquitectura de Conexión](#arquitectura-de-conexión)
- [Autenticación](#autenticación)
- [Manejo de Errores](#manejo-de-errores)
- [Endpoints Utilizados](#endpoints-utilizados)
- [Ejemplos de Uso](#ejemplos-de-uso)

---

## Configuración

### Variables de Entorno

El frontend requiere la siguiente variable de entorno para conectarse con el backend:

```env
VITE_API_BASE_URL=http://localhost:8080
```

**Ubicación:** `frontend/.env`

**Nota:** Si la variable no está definida, la aplicación lanzará un error al iniciar.

### Configuración Base

**Archivo:** `frontend/src/infrastructure/constants/config.js`

```javascript
export const API_BASE_URL = getApiBaseUrl(); // Desde VITE_API_BASE_URL
export const API_TIMEOUT = 30000; // 30 segundos
export const DEFAULT_HEADERS = {
  'Content-Type': 'application/json',
  Accept: 'application/json'
};
```

---

## Arquitectura de Conexión

### Flujo de Conexión

```
Componente/Page
    ↓
Service (productService.js, authService.js)
    ↓
apiClient (shared/services/apiClient.js)
    ↓
axiosInstance (infrastructure/api/axios.config.js)
    ↓
Backend API
```

### Capas de Abstracción

#### 1. **axiosInstance** (`infrastructure/api/axios.config.js`)

Configuración base de Axios con:

- URL base de la API
- Timeout configurado
- Headers por defecto
- Interceptor para autenticación automática

```javascript
const axiosInstance = axios.create({
  baseURL: API_BASE_URL,
  timeout: API_TIMEOUT,
  headers: DEFAULT_HEADERS
});
```

#### 2. **apiClient** (`shared/services/apiClient.js`)

Wrapper alrededor de `axiosInstance` que:

- Maneja errores de forma consistente
- Retorna formato estandarizado `{ data, status }`
- Proporciona métodos HTTP: `get`, `post`, `put`, `patch`, `delete`

```javascript
const apiClient = {
  get: async (url, config = {}) => {
    /* ... */
  },
  post: async (url, data, config = {}) => {
    /* ... */
  },
  put: async (url, data, config = {}) => {
    /* ... */
  },
  patch: async (url, data, config = {}) => {
    /* ... */
  },
  delete: async (url, config = {}) => {
    /* ... */
  }
};
```

#### 3. **Services** (`features/*/services/*.js`)

Servicios específicos por feature que encapsulan la lógica de negocio:

```javascript
// features/products/services/productService.js
export const getProducts = async (params = {}) => {
  const { data } = await apiClient.get(`/api/products?${queryParams}`);
  return data;
};
```

#### 4. **Hooks** (`shared/hooks/useApi.js`)

Hooks de React Query para uso en componentes:

- `useApiQuery` - Para peticiones GET con caché
- `useApiPost` - Para peticiones POST
- `useApiPut` - Para peticiones PUT
- `useApiPatch` - Para peticiones PATCH
- `useApiDelete` - Para peticiones DELETE
- `useApiMutation` - Para mutaciones personalizadas

---

## Autenticación

### Interceptor de Autenticación

El frontend agrega automáticamente el token de autenticación a todas las peticiones mediante un interceptor de Axios:

**Archivo:** `frontend/src/infrastructure/api/axios.config.js`

```javascript
axiosInstance.interceptors.request.use(
  config => {
    const token = getAuthToken(); // Obtiene token del localStorage
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);
```

### Almacenamiento del Token

- **Ubicación:** `localStorage`
- **Clave:** Definida en `features/auth/utils/authStorage.js`
- **Formato:** JWT Bearer Token

### Endpoints Protegidos

Todos los endpoints excepto `/api/auth/login` y `/api/auth/register` requieren autenticación.

---

## Manejo de Errores

### Error Handler

**Archivo:** `frontend/src/infrastructure/api/errorHandler.js`

El `apiClient` utiliza `handleApiError` para normalizar los errores de la API:

```javascript
export const handleApiError = error => {
  if (!error.response) {
    return {
      message: 'Error de conexión. Verifica tu internet.',
      type: 'network',
      status: null
    };
  }

  const { status, data } = error.response;
  // ... manejo de diferentes códigos de estado
};
```

### Códigos de Estado Manejados

| Código  | Mensaje                                     | Tipo    |
| ------- | ------------------------------------------- | ------- |
| 400     | Solicitud inválida                          | client  |
| 401     | No autorizado. Por favor inicia sesión.     | client  |
| 403     | No tienes permiso para realizar esta acción | client  |
| 404     | Recurso no encontrado                       | client  |
| 500+    | Error del servidor                          | server  |
| Network | Error de conexión                           | network |

---

## Endpoints Utilizados

### Autenticación

#### `POST /api/auth/login`

**Descripción:** Inicia sesión de usuario

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**

```json
{
  "token": "string",
  "user": {
    /* ... */
  }
}
```

**Ubicación en código:** `features/auth/services/authService.js`

---

#### `POST /api/auth/register`

**Descripción:** Registra un nuevo usuario

**Request Body:**

```json
{
  "username": "string",
  "password": "string"
}
```

**Response:**

```json
{
  "token": "string",
  "user": {
    /* ... */
  }
}
```

**Ubicación en código:** `features/auth/services/authService.js`

---

#### `POST /api/auth/logout`

**Descripción:** Cierra sesión del usuario

**Request Body:** Ninguno

**Response:** Sin contenido

**Ubicación en código:** `features/auth/services/authService.js`

---

### Productos

#### `GET /api/products`

**Descripción:** Obtiene una lista paginada de productos con filtros opcionales

**Query Parameters:**

- `page` (integer, default: 0) - Índice de página (zero-based)
- `size` (integer, default: 20) - Tamaño de página
- `sort` (string, opcional) - Criterio de ordenamiento en formato `campo,direccion` (ej: `price,asc`, `name,desc`)
- `name` (string, opcional) - Filtro por nombre (búsqueda)
- `category` (string[], opcional) - Filtro por categorías (múltiples valores)
- `minPrice` (number, opcional) - Precio mínimo
- `maxPrice` (number, opcional) - Precio máximo

**Ejemplo de URL:**

```
/api/products?page=0&size=4&sort=price,asc&name=café&category=Bebidas%20Calientes&category=Postres
```

**Response:**

```json
{
  "content": [
    {
      "id": 1,
      "name": "string",
      "description": "string",
      "price": 0,
      "category": "string",
      "image": "string"
    }
  ],
  "totalElements": 0,
  "totalPages": 0,
  "size": 0,
  "number": 0
}
```

**Ubicación en código:**

- `features/products/services/productService.js` (función `getProducts`)
- `features/products/pages/ProductsListPage.jsx` (uso directo)

---

#### `GET /api/products/{id}`

**Descripción:** Obtiene un producto por su ID

**Path Parameters:**

- `id` (integer) - ID del producto

**Response:**

```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 0,
  "category": "string",
  "image": "string"
}
```

**Ubicación en código:** `features/products/services/productService.js`

---

#### `POST /api/products`

**Descripción:** Crea un nuevo producto

**Request Body:**

```json
{
  "name": "string",
  "description": "string",
  "price": 0,
  "category": "string"
}
```

**Response:**

```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 0,
  "category": "string",
  "image": "string"
}
```

**Ubicación en código:**

- `features/products/services/productService.js` (función `createProduct`)
- `features/products/components/CreateProductForm.jsx` (uso con `useApiPost`)

---

#### `PUT /api/products/{id}`

**Descripción:** Actualiza un producto existente

**Path Parameters:**

- `id` (integer) - ID del producto

**Request Body:**

```json
{
  "name": "string",
  "description": "string",
  "price": 0,
  "category": "string"
}
```

**Response:**

```json
{
  "id": 1,
  "name": "string",
  "description": "string",
  "price": 0,
  "category": "string",
  "image": "string"
}
```

**Ubicación en código:**

- `features/products/services/productService.js` (función `updateProduct`)
- `features/products/components/CreateProductForm.jsx` (uso con `useApiMutation`)

---

#### `DELETE /api/products/{id}`

**Descripción:** Elimina un producto

**Path Parameters:**

- `id` (integer) - ID del producto

**Response:** Sin contenido (204 No Content)

**Ubicación en código:**

- `features/products/services/productService.js` (función `deleteProduct`)
- `features/products/pages/ProductsListPage.jsx` (uso con `useApiMutation`)

---

## Ejemplos de Uso

### Ejemplo 1: Obtener Lista de Productos con Filtros

```javascript
import { useApiQuery } from '@/shared/hooks/useApi.js';

function ProductsListPage() {
  const queryParams = {
    page: 0,
    size: 4,
    sort: 'price,asc',
    name: 'café',
    category: ['Bebidas Calientes', 'Postres']
  };

  const queryString = `/api/products?page=${queryParams.page}&size=${queryParams.size}&sort=${queryParams.sort}&name=${queryParams.name}`;

  const { data, isLoading, error } = useApiQuery(
    ['products', queryParams],
    queryString
  );

  if (isLoading) return <div>Cargando...</div>;
  if (error) return <div>Error: {error.message}</div>;

  return <div>{/* Renderizar productos */}</div>;
}
```

### Ejemplo 2: Crear un Producto

```javascript
import { useApiPost } from '@/shared/hooks/useApi.js';
import { useQueryClient } from '@tanstack/react-query';
import { toast } from 'sonner';

function CreateProductForm() {
  const queryClient = useQueryClient();

  const createProductMutation = useApiPost('/api/products', {
    onSuccess: () => {
      queryClient.invalidateQueries(['products']);
      toast.success('Producto creado exitosamente');
    },
    onError: error => {
      toast.error('Error al crear producto', {
        description: error.message
      });
    }
  });

  const handleSubmit = data => {
    createProductMutation.mutate({
      name: data.name,
      description: data.description,
      price: data.price,
      category: data.category
    });
  };

  return <form onSubmit={handleSubmit}>{/* Formulario */}</form>;
}
```

### Ejemplo 3: Eliminar un Producto

```javascript
import { useApiMutation } from '@/shared/hooks/useApi.js';
import apiClient from '@/shared/services/apiClient.js';
import { useQueryClient } from '@tanstack/react-query';

function ProductCard({ productId }) {
  const queryClient = useQueryClient();

  const deleteProductMutation = useApiMutation(
    async id => {
      const response = await apiClient.delete(`/api/products/${id}`);
      return response.data;
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries(['products']);
        toast.success('Producto eliminado exitosamente');
      }
    }
  );

  const handleDelete = () => {
    deleteProductMutation.mutate(productId);
  };

  return <button onClick={handleDelete}>Eliminar</button>;
}
```

### Ejemplo 4: Usar Service Directamente

```javascript
import { getProducts } from '@/features/products/services/productService.js';

async function fetchProducts() {
  try {
    const data = await getProducts({
      page: 0,
      size: 4,
      sort: 'name,asc',
      category: ['Bebidas Calientes']
    });
    console.log(data.content); // Array de productos
    console.log(data.totalElements); // Total de elementos
  } catch (error) {
    console.error('Error:', error.message);
  }
}
```

---

## Notas Importantes

### Autenticación Automática

- Todos los endpoints protegidos reciben automáticamente el header `Authorization: Bearer <token>`
- El token se obtiene del `localStorage` en cada petición
- Si el token no existe o es inválido, el backend retornará un error 401

### Paginación

- La paginación es zero-based (primera página es `page=0`)
- El tamaño por defecto es 20, pero se puede configurar
- Los filtros se aplican antes de la paginación en el backend

### Ordenamiento

- Formato: `campo,direccion` (ej: `price,asc`, `name,desc`)
- Se puede aplicar a múltiples campos pasando múltiples valores `sort`
- El ordenamiento se aplica al total de productos filtrados, no solo a la página actual

### Filtros

- Los filtros se envían como query parameters
- Múltiples categorías se envían como múltiples parámetros `category`
- Los filtros se mantienen al cambiar de página

### Caché y Invalidación

- Se usa React Query para el caché de peticiones GET
- Las mutaciones (POST, PUT, DELETE) invalidan automáticamente las queries relacionadas
- El tiempo de vida del caché es de 60 segundos por defecto

---

## Referencias

- [Documentación de Arquitectura](./ARCHITECTURE.md)
- [Documentación de Routing](./ROUTING.md)
- [React Query Documentation](https://tanstack.com/query/latest)
- [Axios Documentation](https://axios-http.com/docs/intro)
