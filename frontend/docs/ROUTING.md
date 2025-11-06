# Sistema de Rutas - Frontend Pro Eat

## Tabla de Contenidos

1. [Introducción](#introducción)
2. [Arquitectura de Rutas](#arquitectura-de-rutas)
3. [Cómo Funciona](#cómo-funciona)
4. [Crear Rutas de un Feature](#crear-rutas-de-un-feature)
5. [Integrar Rutas en el Router Central](#integrar-rutas-en-el-router-central)
6. [Ejemplos Completos](#ejemplos-completos)
7. [Patrones y Buenas Prácticas](#patrones-y-buenas-prácticas)
8. [Rutas Protegidas](#rutas-protegidas)
9. [FAQ](#faq)

---

## Introducción

El proyecto utiliza **React Router v7** con una arquitectura **Feature-First**, donde cada feature (módulo de negocio) define sus propias rutas de forma encapsulada, y un router central las integra en una sola configuración.

### ¿Por qué este patrón?

- **Encapsulación**: Cada feature es independiente y autocontenido
- **Escalabilidad**: Agregar nuevos features no afecta los existentes
- **Mantenibilidad**: Fácil encontrar y modificar rutas relacionadas
- **Separación de responsabilidades**: El router central solo agrega, cada feature define sus rutas

---

## Arquitectura de Rutas

### Estructura de Directorios

```
src/
├── features/                    # Features/Módulos de negocio
│   ├── auth/                    # Feature de autenticación
│   │   ├── routes/
│   │   │   └── auth.routes.js   # ✅ Rutas del feature auth
│   │   ├── pages/
│   │   │   ├── LoginPage.jsx
│   │   │   └── RegisterPage.jsx
│   │   └── ...
│   │
│   └── home/                    # Feature de página principal
│       ├── routes/
│       │   └── home.routes.js   # ✅ Rutas del feature home
│       └── pages/
│           └── HomePage.jsx
│
└── infrastructure/
    └── router/
        └── routes.config.js     # ✅ Router central que agrega todas las rutas
```

### Flujo de Datos

```
Feature Routes (auth.routes.js)
         ↓
         ↓ export authRoutes
         ↓
Routes Config (routes.config.js) ← Importa todas las rutas
         ↓
         ↓ export router
         ↓
App.jsx ← Usa RouterProvider con router
```

---

## Cómo Funciona

### 1. Router Central (`routes.config.js`)

Este archivo actúa como el **punto de entrada** de todas las rutas. Su única responsabilidad es importar y combinar las rutas de todos los features.

**Ubicación:** `src/infrastructure/router/routes.config.js`

```javascript
import { createBrowserRouter } from 'react-router-dom';
import { authRoutes } from '@/features/auth/routes/auth.routes.js';
import { homeRoutes } from '@/features/home/routes/home.routes.js';

// Combina todas las rutas de los diferentes features
const allRoutes = [
  ...homeRoutes,
  ...authRoutes,
  // Agregar más features aquí cuando los crees
];

export const router = createBrowserRouter(allRoutes);
```

### 2. Feature Routes (`auth.routes.js`, `home.routes.js`, etc.)

Cada feature define sus propias rutas en un archivo dentro de su carpeta `routes/`.

**Ubicación:** `src/features/{feature-name}/routes/{feature-name}.routes.js`

```javascript
// Ejemplo: auth.routes.js
import LoginPage from '../pages/LoginPage.jsx';
import RegisterPage from '../pages/RegisterPage.jsx';
import { AuthLayout } from '@/shared/components/layout/AuthLayout.jsx';

export const authRoutes = [
  {
    path: '/login',
    element: (
      <AuthLayout>
        <LoginPage />
      </AuthLayout>
    )
  },
  {
    path: '/register',
    element: (
      <AuthLayout>
        <RegisterPage />
      </AuthLayout>
    )
  }
];
```

### 3. App.jsx

El componente principal usa el router configurado.

**Ubicación:** `src/App.jsx`

```javascript
import { RouterProvider } from 'react-router-dom';
import { router } from '@/infrastructure/router/routes.config.js';

function App() {
  return <RouterProvider router={router} />;
}

export default App;
```

---

## Crear Rutas de un Feature

### Paso 1: Crear el archivo de rutas

Crea un archivo `{feature-name}.routes.js` dentro de `src/features/{feature-name}/routes/`.

```javascript
// src/features/products/routes/products.routes.js
import ProductsListPage from '../pages/ProductsListPage.jsx';
import ProductDetailPage from '../pages/ProductDetailPage.jsx';
import { MainLayout } from '@/shared/components/layout/MainLayout.jsx';

export const productsRoutes = [
  {
    path: '/products',
    element: (
      <MainLayout>
        <ProductsListPage />
      </MainLayout>
    )
  },
  {
    path: '/products/:id',
    element: (
      <MainLayout>
        <ProductDetailPage />
      </MainLayout>
    )
  }
];
```

### Paso 2: Exportar las rutas

Asegúrate de exportar un array de objetos de ruta con la siguiente estructura:

```javascript
export const {featureName}Routes = [
  {
    path: '/ruta',           // URL path
    element: <Component />    // Componente a renderizar
  }
];
```

### Paso 3: Importar en `routes.config.js`

```javascript
import { productsRoutes } from '@/features/products/routes/products.routes.js';

const allRoutes = [
  ...homeRoutes,
  ...authRoutes,
  ...productsRoutes,  // ✅ Agrega aquí tu nuevo feature
];
```

---

## Integrar Rutas en el Router Central

Solo necesitas hacer **dos cambios simples**:

### 1. Importar las rutas del feature

```javascript
import { nuevoFeatureRoutes } from '@/features/nuevo-feature/routes/nuevo-feature.routes.js';
```

### 2. Agregar al array `allRoutes`

```javascript
const allRoutes = [
  ...homeRoutes,
  ...authRoutes,
  ...nuevoFeatureRoutes,  // ✅ Nueva línea
];
```

**¡Eso es todo!** No necesitas modificar nada más.

---

## Ejemplos Completos

### Ejemplo 1: Feature Auth Completo

**`src/features/auth/routes/auth.routes.js`:**

```javascript
import LoginPage from '../pages/LoginPage.jsx';
import RegisterPage from '../pages/RegisterPage.jsx';
import { AuthLayout } from '@/shared/components/layout/AuthLayout.jsx';

export const authRoutes = [
  {
    path: '/login',
    element: (
      <AuthLayout>
        <LoginPage />
      </AuthLayout>
    )
  },
  {
    path: '/register',
    element: (
      <AuthLayout>
        <RegisterPage />
      </AuthLayout>
    )
  },
  {
    path: '/forgot-password',
    element: (
      <AuthLayout>
        <ForgotPasswordPage />
      </AuthLayout>
    )
  }
];
```

### Ejemplo 2: Feature Home Completo

**`src/features/home/routes/home.routes.js`:**

```javascript
import HomePage from '../pages/HomePage.jsx';
import { MainLayout } from '@/shared/components/layout/MainLayout.jsx';

export const homeRoutes = [
  {
    path: '/',
    element: (
      <MainLayout>
        <HomePage />
      </MainLayout>
    )
  },
  {
    path: '/home',
    element: (
      <MainLayout>
        <HomePage />
      </MainLayout>
    )
  }
];
```

### Ejemplo 3: Rutas con Parámetros Dinámicos

```javascript
// src/features/products/routes/products.routes.js
import ProductDetailPage from '../pages/ProductDetailPage.jsx';
import { MainLayout } from '@/shared/components/layout/MainLayout.jsx';

export const productsRoutes = [
  {
    path: '/products/:id',
    element: (
      <MainLayout>
        <ProductDetailPage />
      </MainLayout>
    )
  },
  {
    path: '/products/:id/edit',
    element: (
      <MainLayout>
        <ProductEditPage />
      </MainLayout>
    )
  }
];

// En ProductDetailPage.jsx puedes acceder al parámetro así:
import { useParams } from 'react-router-dom';

function ProductDetailPage() {
  const { id } = useParams();
  // Usa id para cargar el producto
}
```

---

## Patrones y Buenas Prácticas

### ✅ DO: Mantener Rutas Agrupadas por Feature

```javascript
// ✅ CORRECTO: Rutas del feature auth juntas
export const authRoutes = [
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  { path: '/logout', element: <LogoutPage /> }
];
```

### ❌ DON'T: Crear Rutas Mezcladas

```javascript
// ❌ INCORRECTO: No mezcles rutas de diferentes features
export const allRoutes = [
  { path: '/login', element: <LoginPage /> },      // auth
  { path: '/products', element: <ProductsPage /> }, // products
  { path: '/register', element: <RegisterPage /> }  // auth
];
```

### ✅ DO: Usar Layouts Consistente

```javascript
// ✅ CORRECTO: Usa layouts apropiados
export const authRoutes = [
  {
    path: '/login',
    element: <AuthLayout><LoginPage /></AuthLayout>
  }
];

export const productsRoutes = [
  {
    path: '/products',
    element: <MainLayout><ProductsPage /></MainLayout>
  }
];
```

### ✅ DO: Nombrar Consistente

- Archivo de rutas: `{feature-name}.routes.js`
- Export: `{featureName}Routes` (camelCase)

```javascript
// ✅ CORRECTO
// archivo: products.routes.js
export const productsRoutes = [...];

// archivo: user-profile.routes.js
export const userProfileRoutes = [...];
```

### ✅ DO: Usar Alias @ Correctamente

```javascript
// ✅ CORRECTO: Usa alias @ para imports absolutos
import { MainLayout } from '@/shared/components/layout/MainLayout.jsx';
import { authRoutes } from '@/features/auth/routes/auth.routes.js';
```

---

## Rutas Protegidas

Para crear rutas protegidas (que requieren autenticación), puedes crear un componente wrapper:

```javascript
// src/shared/components/auth/ProtectedRoute.jsx
import { Navigate } from 'react-router-dom';
import { getAuthToken } from '@/infrastructure/constants/env.js';

function ProtectedRoute({ children }) {
  const token = getAuthToken();
  
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
}

export default ProtectedRoute;
```

**Uso en rutas:**

```javascript
// src/features/dashboard/routes/dashboard.routes.js
import ProtectedRoute from '@/shared/components/auth/ProtectedRoute.jsx';
import DashboardPage from '../pages/DashboardPage.jsx';
import { MainLayout } from '@/shared/components/layout/MainLayout.jsx';

export const dashboardRoutes = [
  {
    path: '/dashboard',
    element: (
      <ProtectedRoute>
        <MainLayout>
          <DashboardPage />
        </MainLayout>
      </ProtectedRoute>
    )
  }
];
```

---

## FAQ

### ¿Cómo agrego una nueva ruta a un feature existente?

Simplemente agrega el objeto de ruta al array en el archivo `{feature}.routes.js`:

```javascript
export const authRoutes = [
  // ... rutas existentes
  {
    path: '/reset-password',
    element: <AuthLayout><ResetPasswordPage /></AuthLayout>
  }
];
```

### ¿Cómo creo rutas anidadas?

Para rutas anidadas, usa la propiedad `children`:

```javascript
export const productsRoutes = [
  {
    path: '/products',
    element: <MainLayout><ProductsLayout /></MainLayout>,
    children: [
      {
        path: ':id',
        element: <ProductDetailPage />
      },
      {
        path: ':id/edit',
        element: <ProductEditPage />
      }
    ]
  }
];
```

### ¿Dónde defino el layout de una ruta?

El layout debe envolver el elemento en la propiedad `element`:

```javascript
{
  path: '/login',
  element: (
    <AuthLayout>
      <LoginPage />
    </AuthLayout>
  )
}
```

### ¿Cómo navego programáticamente?

Usa el hook `useNavigate` de React Router:

```javascript
import { useNavigate } from 'react-router-dom';

function MyComponent() {
  const navigate = useNavigate();
  
  const handleClick = () => {
    navigate('/products');
  };
  
  return <button onClick={handleClick}>Ir a productos</button>;
}
```

### ¿Cómo obtengo parámetros de la URL?

Usa el hook `useParams`:

```javascript
import { useParams } from 'react-router-dom';

function ProductPage() {
  const { id } = useParams();
  // id contiene el valor del parámetro :id
}
```

### ¿Cómo obtengo query params?

Usa el hook `useSearchParams`:

```javascript
import { useSearchParams } from 'react-router-dom';

function SearchPage() {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('q'); // ?q=busqueda
}
```

---

## Referencias

- [React Router v7 Documentation](https://reactrouter.com/)
- [Arquitectura del Proyecto](./ARCHITECTURE.md)

---

**¿Preguntas o dudas?** Consulta con el equipo o revisa la documentación oficial de React Router.

