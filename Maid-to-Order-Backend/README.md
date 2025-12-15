# Maid to Order Backend

Backend REST API desarrollado con Spring Boot y Kotlin para el sistema de gestión de restaurante Maid to Order.

## Características

- API RESTful completa
- Persistencia con JPA/H2
- Pruebas unitarias
- Inicialización automática de datos
- CORS habilitado para desarrollo

## Endpoints Principales

### Platos (`/api/dishes`)
- `GET /api/dishes` - Listar todos los platos
- `GET /api/dishes?category=Sopas` - Filtrar por categoría
- `GET /api/dishes?available=true` - Solo disponibles
- `GET /api/dishes?search=ramen` - Buscar platos
- `GET /api/dishes/{id}` - Obtener plato por ID
- `POST /api/dishes` - Crear plato
- `PUT /api/dishes/{id}` - Actualizar plato
- `DELETE /api/dishes/{id}` - Eliminar plato

### Platos Especiales (`/api/special-dishes`)
- `GET /api/special-dishes` - Listar especialidades disponibles
- `GET /api/special-dishes?today=true` - Especiales del día
- `GET /api/special-dishes?type=CHEF_SPECIAL` - Por tipo
- `POST /api/special-dishes` - Crear especialidad
- `DELETE /api/special-dishes/{id}` - Eliminar especialidad

### Pedidos (`/api/orders`)
- `GET /api/orders` - Listar todos los pedidos
- `GET /api/orders?status=PENDING` - Filtrar por estado
- `GET /api/orders/{id}` - Obtener pedido por ID
- `POST /api/orders` - Crear pedido
- `PUT /api/orders/{id}/status` - Actualizar estado
- `DELETE /api/orders/{id}` - Eliminar pedido

## Ejecución

**Desde esta carpeta (Recomendado):**
```bash
.\gradlew.bat bootRun
```

El servidor iniciará en `http://localhost:8080`

## Consola H2

Acceder a `http://localhost:8080/h2-console` con:
- JDBC URL: `jdbc:h2:mem:maidtoorderdb`
- Usuario: `sa`
- Contraseña: (vacía)

## Pruebas

**Desde esta carpeta:**
```bash
.\gradlew.bat test
```

