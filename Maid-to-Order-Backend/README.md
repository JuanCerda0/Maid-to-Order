# Maid to Order - Backend Microservices

Este proyecto Spring Boot/Kotlin está dividido en cuatro microservicios independientes más un gateway. Cada módulo expone sus endpoints con capas Controller → Service → Repository y utiliza H2 en memoria para facilitar las demostraciones.

| Servicio | Puerto | Responsabilidad |
| --- | --- | --- |
| `gateway-service` | 8080 | Spring Cloud Gateway que expone `/api/**` y enruta a los demás servicios. |
| `dishes-service` | 8081 | CRUD de platos y carga de datos iniciales. |
| `orders-service` | 8082 | Gestión de pedidos; consulta a `dishes-service` para validar precios. |
| `specials-service` | 8083 | Especialidades del día / chef. |

## Cómo ejecutar
Desde esta carpeta puedes iniciar cada módulo (en terminales separados):
```bash
./gradlew dishes-service:bootRun
./gradlew orders-service:bootRun
./gradlew specials-service:bootRun
./gradlew gateway-service:bootRun
```

Si prefieres compilar todo usa:
```bash
./gradlew build
```

## Endpoints
Todos los endpoints están disponibles a través del gateway (`http://localhost:8080/api/...`). Ejemplos:

- `GET /api/dishes`, `POST /api/dishes`, `PUT /api/dishes/{id}`, `DELETE /api/dishes/{id}`
- `GET /api/special-dishes?today=true`, `POST /api/special-dishes`
- `GET /api/orders?status=PENDING`, `POST /api/orders`, `PUT /api/orders/{id}/status`

Los servicios de platos y especiales inicializan datos de demostración mediante `DataInitializer`.

## Pruebas unitarias
```
./gradlew test
```
Este comando ejecuta los tests de `dishes-service` y `orders-service`, donde se validan las reglas de negocio usando MockK (por ejemplo, cálculo de totales al crear un pedido).

## Consolas H2
- Dishes: `http://localhost:8081/h2-console`
- Orders: `http://localhost:8082/h2-console`
- Specials: `http://localhost:8083/h2-console`

Usuario `sa`, contraseña vacía y URL `jdbc:h2:mem:<nombre>` (`dishesdb`, `ordersdb`, `specialsdb`).
