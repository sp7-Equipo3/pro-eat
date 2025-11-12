# Gitflow - Estrategia de Ramas

Este documento describe la estrategia de ramas (Gitflow) para el proyecto Pro Eat. Define cómo trabajar con ramas, cuándo crear nuevas ramas, y el flujo de merge entre ramas.

## Tabla de Contenidos

1. [Resumen Ejecutivo](#resumen-ejecutivo)
2. [Ramas Principales](#ramas-principales)
3. [Tipos de Ramas](#tipos-de-ramas)
4. [Flujo de Trabajo](#flujo-de-trabajo)
5. [Convenciones de Nombres](#convenciones-de-nombres)
6. [Comandos Git Prácticos](#comandos-git-prácticos)
7. [Pull Requests](#pull-requests)
8. [Deploy y Releases](#deploy-y-releases)

---

## Resumen Ejecutivo

### Ramas Principales

El proyecto utiliza ramas separadas para frontend y backend:

**Ramas de Desarrollo:**

- **`front-dev`**: Rama de desarrollo de frontend, deploy automático a ambiente de desarrollo
- **`back-dev`**: Rama de desarrollo de backend, deploy automático a ambiente de desarrollo

**Ramas de Producción:**

- **`front-prod`**: Rama de producción de frontend, deploy a producción
- **`back-prod`**: Rama de producción de backend, deploy a producción

**Rama de Presentación:**

- **`main`**: Rama para unificar todo el código a modo de presentación del repositorio

### Flujo General

```
Feature Branch → front-dev/back-dev → front-prod/back-prod
```

### Principios

- **`front-dev` / `back-dev`**: Código estable listo para QA
- **`front-prod` / `back-prod`**: Código en producción
- **`main`**: Código unificado para presentación del repositorio
- Feature branches: Una por tarea/feature (creadas manualmente desde la issue en GitHub Projects)
- Pull Requests: Obligatorios para merge
- Las ramas se eliminan automáticamente después de merge aprobado

---

## Ramas Principales

### `front-dev` y `back-dev`

**Propósito:**

- Ramas de desarrollo para frontend y backend respectivamente
- Código estable listo para que QA pruebe
- Deploy automático a ambiente de desarrollo

**Características:**

- Siempre deben estar estables
- No se hace commit directo (solo via PR)
- Merge desde feature branches
- Deploy automático cuando hay cambios
- QA realiza testing en estas ramas

**Protección:**

- Requiere PR para merge
- Requiere code review
- Requiere que los tests pasen

### `front-prod` y `back-prod`

**Propósito:**

- Ramas de producción para frontend y backend respectivamente
- Código que está en producción
- Solo código aprobado por QA

**Características:**

- Código estable y probado
- No se hace commit directo (solo via PR)
- Merge desde `front-dev` / `back-dev` cuando QA aprueba
- Deploy a producción

**Protección:**

- Requiere PR para merge
- Requiere code review
- Requiere que los tests pasen
- Requiere aprobación adicional (PO/Lead)

### `main`

**Propósito:**

- Rama para unificar todo el código a modo de presentación del repositorio
- No se usa para desarrollo activo
- Representa el estado completo del proyecto

**Características:**

- Código unificado de frontend y backend
- Se actualiza periódicamente desde las ramas de producción
- No tiene deploy asociado
- Solo para presentación y referencia

---

## Tipos de Ramas

### Feature Branches

**Propósito:**

- Desarrollo de nuevas features o tareas
- Una rama por tarea/feature
- Se crean automáticamente desde GitHub Projects

**Nomenclatura:**

- Las ramas se crean automáticamente desde las issues usando GitHub Projects
- El nombre viene del número de issue + nombre de la issue
- Ejemplo: `123-agregar-filtros-de-busqueda`
- Las ramas están en español porque las issues están en español

**Creación de Ramas:**

1. Desde GitHub Projects, en la issue correspondiente
2. Usar la opción "Create branch" que GitHub Projects proporciona
3. La rama se crea automáticamente con el nombre de la issue
4. Seleccionar la rama base correspondiente (`front-dev` o `back-dev`)

**Flujo:**

1. Crear rama desde issue en GitHub Projects (automático)
2. La rama se crea desde `front-dev` o `back-dev` según corresponda
3. Desarrollar feature
4. Crear PR hacia la rama dev correspondiente
5. Code review
6. Merge a `front-dev` o `back-dev`
7. La rama se elimina automáticamente después del merge aprobado

### Bugfix Branches

**Propósito:**

- Fixes de bugs encontrados en `front-dev` o `back-dev` (no críticos)
- Similar a feature branches pero para arreglar bugs

**Nomenclatura:**

- Se crean desde issue en GitHub Projects (igual que feature branches)
- Ejemplo: `789-corregir-validacion-de-email`

**Flujo:**

1. Crear issue de bugfix en GitHub Projects
2. Crear rama desde issue hacia `front-dev` o `back-dev`
3. Arreglar bug
4. Crear PR hacia la rama dev correspondiente
5. Code review
6. Merge a `front-dev` o `back-dev`
7. La rama se elimina automáticamente después del merge

---

## Flujo de Trabajo

### Flujo Normal (Feature)

```
1. Crear issue en GitHub Projects
   - PO o Representante de equipo crea issue
   - Asignar labels (feature, frontend/backend)
   - Asignar prioridad y estimación

2. Desarrollador crea rama desde issue
   - Desarrollador entra en la issue en GitHub Projects
   - Usar opción "Create branch" en la issue
   - Seleccionar base: front-dev o back-dev según corresponda
   - GitHub crea la rama con nombre de la issue: [número-issue]-[nombre-issue]

3. Clonar y trabajar localmente
   git fetch origin
   git checkout 123-agregar-filtros-de-busqueda

4. Desarrollar feature
   git add .
   git commit -m "feat: agregar filtros de búsqueda"

5. Push branch
   git push origin 123-agregar-filtros-de-busqueda

6. Crear PR hacia front-dev o back-dev
   - Abrir PR en GitHub
   - Base: front-dev o back-dev según corresponda
   - Asignar reviewers
   - Mover card a "In Review"

7. Code review
   - Reviewers revisan
   - Pueden pedir cambios

8. Merge a front-dev o back-dev
   - PR aprobado
   - Merge a front-dev o back-dev
   - Deploy automático a ambiente de desarrollo
   - La rama se elimina automáticamente
   - Mover card a "Ready for QA"

9. QA Testing
   - QA prueba en ambiente de desarrollo (front-dev o back-dev)
   - Si aprueba: mover a "Ready for Prod"
   - Si encuentra bugs: mover a "Needs Fix" y agregar comentario con template de bug

10. Merge a front-prod o back-prod (si QA aprueba)
    - Crear PR desde front-dev hacia front-prod (o back-dev hacia back-prod)
    - Merge a rama de producción
    - Deploy a producción
    - Mover card a "Done"
```

### Flujo de Bugfix (Bugs en Dev)

```
1. QA encuentra bug y reporta en issue original
   - QA agrega comentario con template de bug report
   - Mover card a "Needs Fix"
   - Agregar labels de tipo de problema

2. Crear issue de bugfix en GitHub Projects (si no existe)
   - O usar la issue original si es el mismo bug

3. Desarrollador crea rama desde issue
   - Desarrollador entra en la issue en GitHub Projects
   - Usar opción "Create branch" en la issue
   - Seleccionar base: front-dev o back-dev según corresponda
   - GitHub crea la rama con nombre de la issue

4. Clonar y trabajar localmente
   git fetch origin
   git checkout 789-corregir-validacion-de-email

5. Arreglar bug
   git add .
   git commit -m "fix: corregir validación de email"

6. Push branch
   git push origin 789-corregir-validacion-de-email

7. Crear PR hacia front-dev o back-dev
   - Code review
   - Merge a front-dev o back-dev
   - La rama se elimina automáticamente
   - QA prueba de nuevo
```

---

## Convenciones de Nombres

### Nomenclatura de Ramas

**Regla General:**

- Las ramas se crean manualmente por el desarrollador desde GitHub Projects
- El nombre viene del número de issue + nombre de la issue
- Las ramas están en español (porque las issues están en español)
- **No se usan prefijos** como `feature/`, `bugfix/` en el nombre de la rama
- **Los labels en GitHub Projects** indican el tipo (feature, bug, etc.)

**Ejemplos:**

```
123-agregar-filtros-de-busqueda
456-corregir-crash-en-login
789-mejorar-rendimiento-de-lista
234-agregar-validacion-en-formulario
```

**Reglas:**

- Número de issue al inicio
- Nombre descriptivo en español
- Minúsculas
- Separar palabras con guiones
- El nombre viene directamente de la issue en GitHub Projects
- El tipo (feature, bug, etc.) se indica con labels en la issue, no con prefijos en el nombre de la rama

**Creación desde GitHub Projects:**

1. Desarrollador abre la issue en GitHub Projects
2. Click en "Create branch" (opción disponible en la issue)
3. Seleccionar la rama base (`front-dev`, `back-dev`, `front-prod`, `back-prod`)
4. GitHub crea la rama con el formato: `[número-issue]-[nombre-issue]`
5. El desarrollador puede clonar y trabajar en la rama

---

## Comandos Git Prácticos

### Crear una Rama a partir de Dev

Una vez que hayas creado la rama desde GitHub Projects, necesitas clonarla y trabajar localmente:

```bash
# Actualizar referencias remotas
git fetch origin

# Cambiar a la rama dev correspondiente (front-dev o back-dev)
git checkout front-dev  # o back-dev según corresponda

# Actualizar la rama dev local
git pull origin front-dev

# Cambiar a tu rama de trabajo (ya creada desde GitHub Projects)
git checkout 123-agregar-filtros-de-busqueda
```

**Nota:** La rama ya debe estar creada desde GitHub Projects. Si no existe localmente, usa:

```bash
# Traer la rama remota y crear tracking local
git fetch origin
git checkout -b 123-agregar-filtros-de-busqueda origin/123-agregar-filtros-de-busqueda
```

### Realizar Cambios y Hacer Commits

```bash
# Ver estado de los archivos
git status

# Agregar archivos al staging
git add .
# o archivos específicos
git add src/components/ProductCard.jsx

# Hacer commit siguiendo la convención
git commit -m "feat: agregar filtros de búsqueda en productos"
git commit -m "fix: corregir validación de email en registro"
git commit -m "refactor: extraer lógica de autenticación a hook"
```

**Convenciones de commits:**

- `feat:` - Nueva feature
- `fix:` - Bug fix
- `refactor:` - Refactorización
- `docs:` - Documentación
- `test:` - Tests
- `chore:` - Tareas de mantenimiento

### Hacer Push de la Rama

```bash
# Push de la rama al remoto
git push origin 123-agregar-filtros-de-busqueda

# Si es la primera vez que haces push de esta rama
git push -u origin 123-agregar-filtros-de-busqueda
```

### Crear Pull Request

Después de hacer push:

1. Ir a GitHub
2. Click en "New Pull Request"
3. Base: `front-dev` o `back-dev` (según corresponda)
4. Compare: tu rama (ej: `123-agregar-filtros-de-busqueda`)
5. Asignar reviewers
6. Agregar descripción y labels
7. Crear PR

### Mantenimiento de Ramas

#### Guardar Cambios Temporalmente (Stash)

Útil cuando necesitas cambiar de rama pero tienes cambios sin commitear:

```bash
# Guardar cambios temporalmente
git stash

# Ver lista de stashes
git stash list

# Aplicar cambios guardados más recientes
git stash pop

# Aplicar cambios guardados sin eliminarlos del stash
git stash apply

# Eliminar un stash específico
git stash drop stash@{0}
```

#### Actualizar Rama con Cambios de Dev

Antes de crear el PR o si la rama dev tiene cambios nuevos:

```bash
# Cambiar a la rama dev
git checkout front-dev  # o back-dev

# Actualizar desde remoto
git pull origin front-dev

# Volver a tu rama
git checkout 123-agregar-filtros-de-busqueda

# Mergear cambios de dev a tu rama
git merge front-dev

# O usar rebase (más limpio, pero requiere cuidado)
git rebase front-dev
```

#### Limpiar Ramas Locales y Remotas

```bash
# Actualizar referencias remotas y eliminar ramas remotas que ya no existen
git fetch -p

# Ver ramas locales que ya no existen en el remoto
git branch -vv | grep ': gone]'

# Eliminar ramas locales que ya no existen en el remoto
git branch -vv | grep ': gone]' | awk '{print $1}' | xargs git branch -d

# Eliminar rama local específica (solo si ya fue mergeada)
git branch -d 123-agregar-filtros-de-busqueda

# Forzar eliminación de rama local (aunque no esté mergeada)
git branch -D 123-agregar-filtros-de-busqueda
```

#### Ver Estado y Logs

```bash
# Ver estado de archivos modificados
git status

# Ver historial de commits
git log

# Ver historial de commits en una línea
git log --oneline

# Ver diferencias en archivos modificados
git diff

# Ver diferencias en archivos en staging
git diff --staged

# Ver ramas locales
git branch

# Ver ramas remotas
git branch -r

# Ver todas las ramas (locales y remotas)
git branch -a
```

#### Resolver Conflictos

Si hay conflictos al mergear o hacer rebase:

```bash
# Ver archivos con conflictos
git status

# Resolver conflictos manualmente en los archivos
# Luego marcar como resueltos:
git add archivo-con-conflicto.jsx

# Continuar el merge
git commit

# O si estás haciendo rebase
git rebase --continue
```

---

## Pull Requests

### Crear Pull Request

**Desde Feature Branch hacia `front-dev` o `back-dev`:**

1. Push tu branch
2. Ir a GitHub
3. Click en "New Pull Request"
4. Base: `front-dev` o `back-dev` (según corresponda)
5. Compare: tu feature branch (ej: `123-agregar-filtros-de-busqueda`)
6. Título descriptivo
7. Descripción:
   - Qué cambió
   - Por qué
   - Cómo probar
8. Asignar reviewers
9. Agregar labels apropiados (`feature`, `frontend` o `backend`)
10. Crear PR

**Desde `front-dev` / `back-dev` hacia `front-prod` / `back-prod`:**

1. Ir a GitHub
2. Click en "New Pull Request"
3. Base: `front-prod` o `back-prod` (según corresponda)
4. Compare: `front-dev` o `back-dev`
5. Título: "Deploy a producción: [descripción]"
6. Descripción: Resumen de cambios aprobados por QA
7. Asignar reviewers (PO/Lead)
8. Crear PR

### Título del PR

**Formato recomendado:**

```
feat: agregar filtros de búsqueda
fix: corregir validación de formulario
refactor: reorganizar estructura de componentes
```

**Convenciones:**

- `feat:` - Nueva feature
- `fix:` - Bug fix
- `refactor:` - Refactorización
- `docs:` - Documentación
- `test:` - Tests
- `chore:` - Tareas de mantenimiento

### Descripción del PR

**Template sugerido:**

```markdown
## Descripción

Breve descripción de los cambios

## Tipo de cambio

- [ ] Nueva feature
- [ ] Bug fix
- [ ] Refactorización
- [ ] Documentación

## Cómo probar

1. Paso 1
2. Paso 2
3. Paso 3

## Checklist

- [ ] Tests pasan
- [ ] Documentación actualizada
- [ ] Sin console.logs
- [ ] Código revisado
```

### Code Review

**Requisitos:**

- Mínimo 1 aprobación para merge
- Todos los tests deben pasar
- Sin conflictos con la rama dev correspondiente (`front-dev` o `back-dev`)

**Proceso:**

1. Reviewer revisa código
2. Puede pedir cambios
3. Dev hace cambios y actualiza PR
4. Reviewer aprueba
5. Merge a `front-dev` o `back-dev`
6. La rama se elimina automáticamente después del merge

---

## Deploy y Releases

### Deploy Automático a Dev

**Cuándo:**

- Automático cuando hay merge a `front-dev` o `back-dev`
- Trigger: Push a `front-dev` o `back-dev`

**Proceso:**

1. Merge a `front-dev` o `back-dev`
2. CI/CD detecta cambio
3. Build automático
4. Deploy a ambiente de desarrollo correspondiente
5. QA puede probar en el ambiente de desarrollo

### Deploy a Producción

**Cuándo:**

- Manual o automático cuando hay merge a `front-prod` o `back-prod`
- Solo después de aprobación de QA

**Proceso:**

1. QA aprueba en ambiente de desarrollo
2. Card movida a "Ready for Prod"
3. Crear PR desde `front-dev` hacia `front-prod` (o `back-dev` hacia `back-prod`)
4. Code review (puede ser más rápido)
5. Merge a `front-prod` o `back-prod`
6. Deploy a producción
7. Card movida a "Done"

### Releases

**Estrategia:**

- Releases pueden ser por feature o por sprint
- Tag en `main` cuando se hace release
- Versionado semántico (opcional)

**Ejemplo:**

```bash
git tag -a v1.2.0 -m "Release: Agregar filtros de búsqueda"
git push origin v1.2.0
```
