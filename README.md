# Maid to Order - Sistema de Restaurante

## Descripción del Proyecto

Maid to Order es una aplicación móvil desarrollada en Kotlin con Jetpack Compose que permite a los clientes de un restaurante visualizar el menú, realizar pedidos y gestionar su carrito de compras. El sistema incluye un backend desarrollado con Spring Boot que proporciona una API REST para gestionar platos, pedidos y especialidades.

## Arquitectura

### Frontend (Android App)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Framework UI**: Jetpack Compose
- **Lenguaje**: Kotlin
- **Persistencia Local**: DataStore para preferencias y carrito
- **Comunicación con Backend**: Retrofit para llamadas HTTP

### Backend (Spring Boot)
- **Framework**: Spring Boot 3.2.0
- **Lenguaje**: Kotlin
- **Base de Datos**: H2 (en memoria) con JPA
- **API**: RESTful con controladores para platos, pedidos y especialidades

## Estructura del Proyecto

```
Proyecto/
├── app/
│   └── src/
│       └── main/
│           └── java/
│               └── pkg/
│                   └── maid_to_order/
│                       ├── data/
│                       │   ├── model/          # Modelos de datos
│                       │   └── Screen.kt      # Navegación
│                       ├── network/
│                       │   ├── api/           # Interfaces Retrofit
│                       │   ├── dto/           # DTOs de red
│                       │   └── mapper/        # Mappers DTO -> Domain
│                       ├── repository/        # Repositorios locales
│                       ├── ui/
│                       │   ├── components/    # Componentes reutilizables
│                       │   ├── screens/        # Pantallas de la app
│                       │   └── theme/          # Tema y estilos
│                       ├── utils/              # Utilidades
│                       └── viewmodel/          # ViewModels MVVM
└── Maid-to-Order-Backend/
    └── src/
        └── main/
            └── kotlin/
                └── com/
                    └── maidtoorder/
                        └── backend/
                            ├── config/        # Configuración
                            ├── controller/    # Controladores REST
                            ├── dto/           # DTOs de API
                            ├── model/         # Entidades JPA
                            ├── repository/    # Repositorios JPA
                            └── service/        # Lógica de negocio
```

## Funcionalidades Principales

### App Android

1. **Pantalla Principal (Home)**
   - Visualización del menú completo
   - Búsqueda de platos
   - Filtrado por categorías
   - Sección de platos especiales del día
   - Animaciones fluidas en las transiciones

2. **Detalle de Plato**
   - Información completa del plato
   - Opción para añadir al carrito
   - Visualización de imágenes

3. **Carrito de Compras**
   - Gestión de items en el carrito
   - Cálculo automático del total
   - Persistencia local con DataStore

4. **Formulario de Pedido**
   - Captura de número de mesa
   - Notas adicionales
   - Envío del pedido al backend

5. **Pantalla de Administración**
   - Gestión de platos (añadir, editar, eliminar)
   - Acceso protegido con login

6. **Configuraciones**
   - Modo oscuro/claro
   - Preferencias de usuario

### Backend API

1. **Gestión de Platos (`/api/dishes`)**
   - GET: Obtener todos los platos (con filtros opcionales)
   - GET /{id}: Obtener plato por ID
   - POST: Crear nuevo plato
   - PUT /{id}: Actualizar plato
   - DELETE /{id}: Eliminar plato

2. **Platos Especiales (`/api/special-dishes`)**
   - GET: Obtener platos especiales (del día, por tipo)
   - POST: Crear plato especial
   - DELETE /{id}: Eliminar plato especial

3. **Gestión de Pedidos (`/api/orders`)**
   - GET: Obtener todos los pedidos
   - GET /{id}: Obtener pedido por ID
   - POST: Crear nuevo pedido
   - PUT /{id}/status: Actualizar estado del pedido
   - DELETE /{id}: Eliminar pedido

## Instalación y Configuración

### Requisitos Previos

- Android Studio (para la app)
- JDK 17 o superior (para el backend)
- Gradle 8.x

### Backend

**Opción 1: Desde la raíz del proyecto (Recomendado)**

Desde la carpeta raíz del proyecto, ejecuta:

**Windows (CMD):**
```bash
run-backend.bat
```

**Windows (PowerShell):**
```powershell
.\run-backend.ps1
```

**Opción 2: Desde la carpeta del backend**

1. Navegar a la carpeta del backend:
```bash
cd Maid-to-Order-Backend
```

2. Ejecutar el backend:
```bash
.\gradlew.bat bootRun
```

El backend estará disponible en `http://localhost:8080/api`

**Nota**: La consola H2 estará disponible en `http://localhost:8080/api/h2-console` para inspeccionar la base de datos.

### App Android

1. Abrir el proyecto en Android Studio
2. Configurar la URL del backend en `RetrofitClient.kt`:
   - Para emulador: `http://10.0.2.2:8080/api/`
   - Para dispositivo físico: `http://[IP_LOCAL]:8080/api/`

3. Sincronizar el proyecto con Gradle
4. Ejecutar la app en un dispositivo o emulador

## Uso del Sistema

### Como Cliente

1. **Ver el Menú**
   - Al abrir la app, se muestra automáticamente el menú completo
   - Usa la barra de búsqueda para encontrar platos específicos
   - Filtra por categorías usando los chips en la parte superior
   - Los platos especiales del día aparecen en una sección destacada

2. **Añadir al Carrito**
   - Toca cualquier plato para ver sus detalles
   - Presiona el botón "Añadir al Carrito"
   - El plato se añadirá con cantidad 1
   - Puedes modificar la cantidad desde el carrito

3. **Realizar un Pedido**
   - Ve al carrito desde el icono en la barra superior
   - Revisa los items y el total
   - Presiona "Realizar Pedido"
   - Ingresa el número de mesa (obligatorio)
   - Añade notas opcionales
   - Confirma el pedido

4. **Ver Configuraciones**
   - Accede a ajustes desde el icono de engranaje
   - Cambia entre modo claro y oscuro

### Como Administrador

1. **Acceder al Panel de Administración**
   - Toca el icono de candado en la barra superior
   - Ingresa las credenciales de administrador

2. **Gestionar Platos**
   - Ver lista de todos los platos
   - Añadir nuevos platos con nombre, descripción, precio y categoría
   - Editar platos existentes
   - Eliminar platos

## Persistencia de Datos

### App Android
- **DataStore**: Almacena preferencias de usuario (modo oscuro) y el carrito de compras
- El carrito persiste entre sesiones

### Backend
- **H2 Database**: Base de datos en memoria que se inicializa con datos de ejemplo al arrancar
- Los datos se pierden al reiniciar el servidor (configuración por defecto)
- Para persistencia permanente, cambiar a PostgreSQL o MySQL en `application.yml`

## Pruebas Unitarias

El backend incluye pruebas unitarias para los servicios principales:

**Desde la raíz del proyecto:**

**Windows (CMD):**
```bash
test-backend.bat
```

**Windows (PowerShell):**
```powershell
.\test-backend.ps1
```

**O desde la carpeta del backend:**
```bash
cd Maid-to-Order-Backend
.\gradlew.bat test
```

Las pruebas cubren:
- Creación, lectura, actualización y eliminación de platos
- Creación y gestión de pedidos
- Validaciones de negocio

## Características Técnicas Destacadas

### Animaciones
- Transiciones suaves entre pantallas
- Efectos de escala en botones y tarjetas
- Animaciones de entrada/salida en listas
- Efectos de shimmer y bounce

### Arquitectura MVVM
- Separación clara de responsabilidades
- ViewModels manejan la lógica de negocio
- UI reactiva con Compose
- Estado observable con StateFlow y State

### Integración con API
- Retrofit para llamadas HTTP
- Manejo de errores y estados de carga
- Fallback a datos locales si la API no está disponible
- Mappers para convertir entre DTOs y modelos de dominio

## Solución de Problemas

### El backend no inicia
- Verificar que el puerto 8080 esté libre
- Revisar los logs en la consola
- Asegurarse de tener JDK 17 instalado

### La app no se conecta al backend
- Verificar que el backend esté corriendo
- Revisar la URL en `RetrofitClient.kt`
- Para dispositivo físico, asegurarse de que la IP sea correcta y el dispositivo esté en la misma red
- Verificar permisos de internet en el manifest

### Los datos no persisten
- El backend usa H2 en memoria por defecto (los datos se pierden al reiniciar)
- El carrito de la app persiste en DataStore localmente
- Para persistencia permanente en backend, configurar una base de datos externa

## Próximas Mejoras

- [ ] Autenticación de usuarios
- [ ] Notificaciones push para pedidos
- [ ] Historial de pedidos
- [ ] Sistema de valoraciones
- [ ] Integración con pasarelas de pago
- [ ] Dashboard de administración más completo
- [ ] Reportes y estadísticas

## Licencia

Este proyecto fue desarrollado como parte de un encargo académico.

## Contacto

Para preguntas o sugerencias sobre el sistema, contactar al desarrollador.

---

**Nota**: Este README fue creado para documentar el sistema Maid to Order. El proyecto utiliza tecnologías modernas de Android y Spring Boot para proporcionar una experiencia completa de gestión de restaurante.

