# Flujo de Trabajo y Configuración del GitHub Project

Este documento describe la configuración estándar del flujo de trabajo del equipo usando GitHub Projects, estados, labels y mejores prácticas para un equipo con desarrolladores, QAs y Product Owner.

## Tabla de Contenidos

1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Configuración de Estados (Status)](#configuración-de-estados-status)
3. [Configuración de Labels](#configuración-de-labels)
4. [Flujo Completo de Trabajo](#flujo-completo-de-trabajo)
5. [WIP Limits y Reglas](#wip-limits-y-reglas)
6. [Campos Personalizados](#campos-personalizados)
7. [Roles y Responsabilidades](#roles-y-responsabilidades)
8. [Templates de Issues](#templates-de-issues)
9. [Mejores Prácticas](#mejores-prácticas)
10. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## Resumen Ejecutivo

### Principio Fundamental

**Separación clara entre GitHub Projects y Labels:**

- **GitHub Projects (Estados)**: Flujo de trabajo que cambia durante el desarrollo
- **Labels**: Metadata permanente para clasificación y búsqueda
- **Sin redundancia**: Si está en Projects, no va en Labels

### Flujo General

```
Desarrollo → Code Review → QA Testing → Producción
```

**Nota:** Para información sobre ramas del repositorio (`dev`, `main`, feature branches, etc.), consulta el documento [Gitflow](./GITFLOW.md).

---

## Configuración de Estados (Status)

### Estados del Workflow

Los estados representan el flujo de trabajo del equipo. Cada tarea pasa por estos estados en orden:

```
1. Backlog
2. Todo
3. In Progress
4. In Review
5. Ready for QA
6. QA Testing
7. Needs Fix
8. Ready for Prod
9. Done
10. Blocked
```

### Nota sobre "Quién Mueve"

El campo **"Quién mueve"** indica quién es responsable de mover la card **hacia ese estado**. El campo **"Movimiento"** muestra desde qué estado viene. Por ejemplo:

- En "Todo": "Quién mueve: Representante de equipo" + "Movimiento: Backlog → Todo" = El Representante de equipo mueve la card desde Backlog hacia Todo.
- En "In Progress": "Quién mueve: Desarrollador" + "Movimiento: Todo → In Progress" = El Desarrollador mueve la card desde Todo hacia In Progress.

### Descripción de Cada Estado

#### 1. Backlog

- **Descripción**: Tareas planificadas pero no priorizadas aún
- **Quién crea**: Product Owner
- **Cuándo**: Al crear nuevas tareas o ideas futuras

#### 2. Todo

- **Descripción**: Tareas listas para ser trabajadas
- **Quién mueve**: Representante de equipo (Front/Back o Team Lead)
- **Cuándo**: Cuando una tarea está priorizada y lista para desarrollo
- **Movimiento**: Backlog → Todo

#### 3. In Progress

- **Descripción**: Desarrollador trabajando activamente en la tarea
- **Quién mueve**: Desarrollador
- **Cuándo**: Cuando el dev empieza a trabajar
- **Movimiento**: Todo → In Progress
- **WIP Limit**: 1 por desarrollador

#### 4. In Review

- **Descripción**: Pull Request abierto, esperando code review
- **Quién mueve**: Desarrollador
- **Cuándo**: Cuando se crea el PR
- **Movimiento**: In Progress → In Review
- **Significa**: "Este PR está esperando aprobación de reviewers"

#### 5. Ready for QA

- **Descripción**: PR aprobado y mergeado a la rama de desarrollo, listo para que QA pruebe
- **Quién mueve**: Desarrollador
- **Cuándo**: Después de que el PR es aprobado y mergeado (ver [Gitflow](./GITFLOW.md) para detalles de ramas)
- **Movimiento**: In Review → Ready for QA
- **Significa**: "Código en ambiente de desarrollo, QA puede empezar a probar"

#### 6. QA Testing

- **Descripción**: QA está probando la funcionalidad en ambiente de desarrollo
- **Quién mueve**: QA
- **Cuándo**: Cuando QA empieza a probar
- **Movimiento**: Ready for QA → QA Testing
- **WIP Limit**: 2-3 por QA (depende del equipo)

#### 7. Needs Fix

- **Descripción**: QA encontró problemas, necesita arreglos
- **Quién mueve hacia este estado**: QA
- **Quién mueve desde este estado**: Desarrollador (mueve a In Progress cuando toma la tarea)
- **Cuándo**: Cuando QA encuentra bugs o problemas
- **Movimiento hacia**: QA Testing → Needs Fix
- **Movimiento desde**: Needs Fix → In Progress (cuando dev toma la tarea)
- **WIP Limit**: Sin límite (cola de trabajo pendiente)
- **Importante**: NO cuenta en el WIP limit de "In Progress"

#### 8. Ready for Prod

- **Descripción**: QA aprobó en ambiente de desarrollo, listo para deploy a producción
- **Quién mueve**: QA
- **Cuándo**: Cuando QA aprueba la funcionalidad en ambiente de desarrollo
- **Movimiento**: QA Testing → Ready for Prod
- **Significa**: "Listo para deploy a producción" (ver [Gitflow](./GITFLOW.md) para detalles de ramas)

#### 9. Done

- **Descripción**: Tarea completada, código en producción
- **Quién mueve**: DevOps/Dev que hace el deploy a producción
- **Cuándo**: Después de deploy a producción (ver [Gitflow](./GITFLOW.md) para detalles de ramas)
- **Movimiento**: Ready for Prod → Done

#### 10. Blocked

- **Descripción**: Tarea bloqueada por dependencia externa o aclaración necesaria
- **Quién mueve**: Cualquiera que identifique el bloqueo
- **Cuándo**: Cuando hay dependencias, esperando aclaración del PO, etc.
- **Movimiento**: Desde cualquier estado → Blocked
- **Labels**: No se usan labels especiales para blocked (el estado es suficiente)

---

## Configuración de Labels

### Principio: Sin Redundancia

Los labels son para **clasificación y búsqueda**, no para duplicar información que ya está en Projects.

### Labels por Categoría

#### 1. Tipo de Trabajo

```
bug                 - Bug general
feature             - Nueva funcionalidad
enhancement         - Mejora de funcionalidad existente
refactor            - Reestructuración de código
documentation       - Cambios en documentación
technical-debt      - Deuda técnica
testing             - Tests
performance         - Optimización de rendimiento
security            - Seguridad
accessibility       - Accesibilidad
```

#### 2. Área Técnica

```
frontend            - Cliente/UI
backend             - Servidor (incluyendo API, endpoints, etc.)
```

#### 3. Tipo de Problema (para "Needs Fix")

Estos labels ayudan a QA a categorizar visualmente los problemas encontrados:

```
bug                 - Bug general
ui-bug              - Bug de UI/interfaz
functional-bug      - Bug funcional
regression          - Regresión (algo que funcionaba y dejó de funcionar)
performance         - Problema de rendimiento
accessibility       - Problema de accesibilidad
data-issue          - Problema con datos
```

### Colores Sugeridos para Labels

```
bug                 - Rojo (#d73a4a)
ui-bug              - Naranja (#fb8500)
functional-bug      - Rojo oscuro (#b91c1c)
regression          - Rojo brillante (#ef4444)
performance         - Amarillo (#fbbf24)
accessibility       - Azul claro (#3b82f6)
data-issue          - Morado (#a855f7)
feature             - Verde (#28a745)
enhancement         - Azul (#0052cc)
refactor            - Morado (#6f42c1)
documentation       - Azul claro (#0075ca)
technical-debt      - Naranja (#fb8500)
frontend            - Naranja/marrón (#e99695)
backend             - Azul (#0e8a16)
```

---

## Flujo Completo de Trabajo

### Flujo Visual

```
┌─────────────┐
│   Backlog   │ ← Tareas planificadas
└──────┬──────┘
       │
       ↓ (PO prioriza)
┌─────────────┐
│    Todo     │ ← Lista para trabajar
└──────┬──────┘
       │
       ↓ (Dev toma tarea)
┌─────────────┐
│ In Progress │ ← Dev trabajando en la tarea
└──────┬──────┘
       │
       ↓ (Dev crea PR)
┌─────────────┐
│  In Review  │ ← PR esperando code review
└──────┬──────┘
       │
       ↓ (PR aprobado, merge a rama de desarrollo)
┌─────────────┐
│Ready for QA │ ← Deploy automático a ambiente de desarrollo
└──────┬──────┘
       │
       ↓ (QA toma tarea)
┌─────────────┐
│ QA Testing  │ ← QA probando en ambiente de desarrollo
└──────┬──────┘
       │
       ├─→ (Encuentra bugs) → Needs Fix → In Progress
       │
       └─→ (Aprueba) → Ready for Prod → Done
```

### Flujo Detallado Paso a Paso

#### Paso 1: Planificación

```
Backlog → Todo
```

- PO crea tareas en Backlog
- PO asigna prioridad (Critical, High, Medium, Low)
- Representante de equipo prioriza y mueve a Todo cuando está lista para desarrollo

#### Paso 2: Desarrollo

```
Todo → In Progress
```

- Dev toma tarea de Todo
- Mueve a In Progress
- Trabaja en la tarea (ver [Gitflow](./GITFLOW.md) para detalles de ramas)
- Agrega labels: tipo de trabajo + área técnica

#### Paso 3: Code Review

```
In Progress → In Review
```

- Dev crea PR
- Mueve card a In Review
- Reviewers revisan el código
- Pueden pedir cambios (vuelve a In Progress)

#### Paso 4: Merge y Deploy a Desarrollo

```
In Review → Ready for QA
```

- PR aprobado
- Merge a rama de desarrollo (ver [Gitflow](./GITFLOW.md))
- Deploy automático a ambiente de desarrollo
- Dev mueve card a Ready for QA

#### Paso 5: QA Testing

```
Ready for QA → QA Testing
```

- QA toma la card
- Mueve a QA Testing
- Prueba en ambiente de desarrollo

#### Paso 6A: QA Encuentra Problemas

```
QA Testing → Needs Fix
```

- QA encuentra bugs
- Mueve a Needs Fix
- Agrega labels según tipo de problema (bug, ui-bug, regression, etc.)
- Dev termina su tarea actual en In Progress
- Dev toma tarea de Needs Fix
- Dev mueve a In Progress y arregla

#### Paso 6B: QA Aprueba

```
QA Testing → Ready for Prod
```

- QA aprueba la funcionalidad
- Mueve a Ready for Prod
- Listo para deploy a producción (ver [Gitflow](./GITFLOW.md) para detalles de ramas)

#### Paso 7: Producción

```
Ready for Prod → Done
```

- Deploy a producción (ver [Gitflow](./GITFLOW.md) para detalles de ramas)
- Mueve a Done

---

## WIP Limits y Reglas

### WIP Limits Recomendados

```
In Progress:    1 por desarrollador
In Review:      3-5 total (depende del tamaño del equipo)
QA Testing:     2-3 por QA
Needs Fix:       Sin límite (cola de trabajo pendiente)
Ready for QA:    Sin límite (cola de trabajo)
Ready for Prod:  Sin límite (cola de trabajo)
```

### Reglas Importantes

#### Regla 1: WIP Limit en "In Progress"

- Cada desarrollador solo puede tener **1 tarea** en "In Progress"
- Si tiene una tarea ahí, no puede tomar otra
- Debe terminar antes de tomar nueva tarea

#### Regla 2: "Needs Fix" NO cuenta en WIP Limit

- Puede haber múltiples tareas en "Needs Fix"
- No rompe el WIP limit de "In Progress"
- Indica que hay trabajo pendiente

#### Regla 3: Prioridad de Trabajo

```
1. Primero termina lo que está en "In Progress"
2. Luego toma algo de "Needs Fix" (si hay)
3. Luego toma algo de "Todo"
```

#### Regla 4: Bugs Críticos y WIP Limit

Si un desarrollador tiene una tarea en "In Progress" y aparece un **bug crítico** que requiere atención inmediata:

**Procedimiento:**

1. Mover la tarea actual a **"Blocked"**
2. Agregar comentario: "Pausado por bug crítico [número de tarea del bug]"
3. Tomar el bug crítico y moverlo a "In Progress"
4. Cuando termine el bug crítico, volver a tomar la tarea pausada de "Blocked" y moverla a "In Progress"

**Importante:** El WIP limit de 1 tarea en "In Progress" debe respetarse siempre, incluso con bugs críticos. Usar "Blocked" para pausar temporalmente la tarea actual.

#### Regla 5: No Duplicar Información

- Prioridad → GitHub Projects (campo Priority)
- Tipo de problema → Labels
- Estado del workflow → Estados en Projects

---

## Campos Personalizados

### Priority (Prioridad)

Campo personalizado en GitHub Projects:

```
Critical    - Bloquea producción o funcionalidad crítica
High        - Importante, resolver pronto
Medium      - Prioridad normal
Low         - Mejora menor, puede esperar
```

**Uso:**

- PO asigna prioridad al crear tarea
- Puede cambiar durante el desarrollo
- Ayuda a priorizar trabajo

### Size/Estimation (Estimación con Fibonacci)

El equipo utiliza estimación con números de Fibonacci para el tamaño de tareas:

```
1     - Tarea muy pequeña (< 1 día)
2     - Tarea pequeña (1 día)
3     - Tarea mediana (2-3 días)
5     - Tarea grande (3-5 días)
8     - Tarea muy grande (más de 1 sprint)
13    - Tarea épica (requiere dividir en subtareas)
```

**Contexto del equipo:**

- 3 sprints de 5 días cada uno
- Las tareas estimadas en 8 o más puntos deben dividirse en subtareas más pequeñas
- Idealmente, una tarea no debe exceder 5 puntos (1 sprint completo)

**Uso:**

- El Representante de equipo o el desarrollador asigna la estimación al crear o tomar la tarea
- Ayuda a planificar la capacidad del sprint
- Facilita la identificación de tareas que necesitan dividirse

### Assignee (Asignado)

Campo nativo de GitHub:

- Asignar desarrollador responsable
- Asignar QA responsable
- Puede cambiar durante el desarrollo

---

## Roles y Responsabilidades

### Product Manager (PM)

**Responsabilidades:**

- Definir la visión del producto y roadmap
- Priorizar features y funcionalidades a nivel estratégico
- Coordinar con stakeholders
- Asegurar que el producto cumple objetivos de negocio

**Estados que mueve:**

- No mueve estados directamente (trabaja a nivel estratégico)

### Product Owner (PO)

**Responsabilidades:**

- Crear tareas en Backlog basadas en el roadmap del PM
- Asignar prioridad (Critical, High, Medium, Low)
- Aclarar requisitos cuando hay dudas
- Aclarar bloqueos cuando hay dudas
- Validar que las tareas cumplen los criterios de aceptación

**Estados que mueve:**

- Crea tareas en Backlog (no mueve estados)

### Scrum Master (SM)

**Responsabilidades:**

- Facilitar las ceremonias de Scrum (Daily, Sprint Planning, Retrospectiva, Review)
- Remover impedimentos del equipo
- Asegurar que el equipo sigue el proceso y las reglas del workflow
- Ayudar a mantener el WIP limit
- Facilitar la comunicación entre roles

**Estados que mueve:**

- Puede mover estados para facilitar el flujo cuando hay impedimentos
- Ayuda a mantener el workflow organizado

### Representante de Equipo (Front/Back o Team Lead)

**Responsabilidades:**

- Priorizar tareas del Backlog
- Mover tareas de Backlog a Todo cuando están listas para desarrollo
- Asignar tareas a desarrolladores
- Resolver bloqueos técnicos
- Coordinar el trabajo del equipo

**Estados que mueve:**

- Backlog → Todo
- Blocked → Todo (cuando se resuelve bloqueo técnico)

### Desarrollador (Dev)

**Responsabilidades:**

- Tomar tareas de Todo
- Mover a In Progress cuando empieza
- Crear PR y mover a In Review
- Mover a Ready for QA después de merge
- Arreglar bugs de Needs Fix
- Agregar labels apropiados

**Estados que mueve:**

- Todo → In Progress
- In Progress → In Review
- In Review → Ready for QA
- Needs Fix → In Progress (cuando toma tarea de Needs Fix para arreglarla)

### QA

**Responsabilidades:**

- Tomar tareas de Ready for QA
- Mover a QA Testing cuando empieza
- Mover a Needs Fix si encuentra problemas (con labels)
- Mover a Ready for Prod si aprueba
- Agregar labels de tipo de problema

**Estados que mueve:**

- Ready for QA → QA Testing
- QA Testing → Needs Fix (si hay problemas)
- QA Testing → Ready for Prod (si aprueba)

### DevOps/Team Lead

**Responsabilidades:**

- Hacer deploy a producción cuando está Ready for Prod (ver [Gitflow](./GITFLOW.md) para detalles de ramas)
- Mover a Done después de deploy a producción

**Estados que mueve:**

- Ready for Prod → Done

---

## Templates de Issues

El proyecto incluye templates para facilitar la creación y reporte de issues:

### Template para Features (Desarrolladores)

**Archivo:** `.github/ISSUE_TEMPLATE/feature_request.md`

**Cuándo usar:**

- Al crear una nueva issue de feature o funcionalidad
- Cuando el PO o Representante de equipo crea tareas en Backlog

**Cómo usar:**

1. Crear nueva issue en GitHub
2. Seleccionar template "Feature Request"
3. Completar todos los campos:
   - Descripción (Como/Quiero/Para)
   - Criterios de Aceptación
   - Reglas de Negocio
   - Información Técnica (Área, Estimación Fibonacci, Prioridad)
   - Links a recursos (documentación, diseños)

**Importante:**

- Asignar labels apropiados (`feature`, `frontend` o `backend`)
- Asignar prioridad en el campo Priority del GitHub Project
- Asignar estimación en el campo Size (Fibonacci)

### Template para Bug Reports (QA)

**Archivo:** `.github/ISSUE_TEMPLATE/bug_report.md`

**Cuándo usar:**

- Cuando QA encuentra bugs durante el testing
- Cuando QA está en estado "QA Testing" y encuentra problemas

**Cómo usar:**

1. QA está probando una tarea en estado "QA Testing"
2. Encuentra un bug o problema
3. Abrir la issue de la tarea que está probando
4. Copiar el contenido del template `.github/ISSUE_TEMPLATE/bug_report.md`
5. Pegarlo como **comentario** en la issue (no crear nueva issue)
6. Completar toda la información del bug:
   - Descripción del bug
   - Pasos para reproducir
   - Comportamiento actual vs esperado
   - Tipo de problema (marcar checkbox correspondiente)
   - Entorno de pruebas
   - Capturas de pantalla si aplica
7. Mover la card a "Needs Fix"
8. Agregar labels según el tipo de problema (`bug`, `ui-bug`, `regression`, etc.)

**Importante:**

- NO crear una issue separada para el bug
- El reporte va como comentario en la issue original
- Agregar labels apropiados según el tipo de problema encontrado

---

## Mejores Prácticas

### 1. Mantener Estados Actualizados

- **Siempre** mueve la card cuando cambia el estado real
- No dejes cards en estados incorrectos
- Actualiza el estado inmediatamente después de la acción

### 2. Usar Labels Consistentemente

- Agrega labels al crear la tarea
- Usa labels para búsqueda y filtrado
- No dupliques información que ya está en Projects

### 3. Comentarios en Cards

- Agrega comentarios cuando mueves a Blocked (explica por qué)
- Agrega comentarios cuando mueves a Needs Fix (describe el problema)
- Agrega comentarios cuando mueves a Ready for Prod (confirma aprobación)

### 4. WIP Limits

- Respeta el WIP limit de "In Progress"
- No tomes más tareas de las permitidas
- Termina lo que empezaste antes de tomar nuevo trabajo

### 5. Priorización

- Revisa "Needs Fix" antes de tomar nuevas tareas de "Todo"
- Prioriza según el campo Priority
- Considera dependencias entre tareas

### 6. Comunicación

- Comunica cuando mueves a Blocked
- Comunica cuando mueves a Needs Fix (describe el problema)
- Comunica cuando mueves a Ready for Prod

---

## Ejemplos Prácticos

### Ejemplo 1: Flujo Exitoso

**Tarea:** "Agregar filtros de búsqueda en productos"

**Labels:** `feature`, `frontend`

**Flujo:**

1. PO crea tarea → Backlog (Priority: High)
2. Representante de equipo mueve → Todo
3. Dev toma → In Progress
4. Dev crea PR → In Review
5. PR aprobado, merge a rama de desarrollo → Ready for QA
6. QA prueba → QA Testing
7. QA aprueba → Ready for Prod
8. Deploy a producción → Done

### Ejemplo 2: QA Encuentra Bugs

**Tarea:** "Agregar validación en formulario de registro"

**Labels iniciales:** `feature`, `frontend`

**Flujo:**

1. Dev termina → Ready for QA
2. QA prueba → QA Testing
3. QA encuentra bug de validación → Needs Fix
   - Agrega labels: `bug`, `ui-bug`
4. Dev termina otra tarea en In Progress
5. Dev toma esta tarea → In Progress
6. Dev arregla y crea PR → In Review
7. PR aprobado → Ready for QA
8. QA prueba de nuevo → QA Testing
9. QA aprueba → Ready for Prod
10. Deploy a producción → Done

### Ejemplo 3: Tarea Bloqueada

**Tarea:** "Integrar API de pagos"

**Labels:** `feature`, `backend`

**Flujo:**

1. Dev toma → In Progress
2. Dev necesita aclaración del PO → Blocked
   - Comentario: "Necesito saber qué métodos de pago soportar"
3. PO aclara → Todo
4. Dev toma de nuevo → In Progress
5. Continúa flujo normal...

### Ejemplo 4: Regresión Encontrada

**Tarea:** "Mejorar rendimiento de lista de productos"

**Labels iniciales:** `enhancement`, `frontend`, `performance`

**Flujo:**

1. Dev termina → Ready for QA
2. QA prueba → QA Testing
3. QA encuentra que el login dejó de funcionar → Needs Fix
   - Agrega labels: `regression`, `bug`
4. Dev arregla → In Progress
5. Continúa flujo...

---

## Resumen de Configuración

### Estados (10)

```
1. Backlog
2. Todo
3. In Progress (WIP: 1 por dev)
4. In Review
5. Ready for QA
6. QA Testing (WIP: 2-3 por QA)
7. Needs Fix (WIP: sin límite)
8. Ready for Prod
9. Done
10. Blocked
```

### Labels Esenciales

**Tipo de Trabajo:**

- bug, feature, enhancement, refactor, documentation, technical-debt, testing

**Área Técnica:**

- frontend, backend

**Tipo de Problema:**

- bug, ui-bug, functional-bug, regression, performance, accessibility, data-issue

### Campos Personalizados

- **Priority**: Critical, High, Medium, Low
- **Size**: 1, 2, 3, 5, 8, 13 (Fibonacci)
- **Assignee**: Asignación de responsables
