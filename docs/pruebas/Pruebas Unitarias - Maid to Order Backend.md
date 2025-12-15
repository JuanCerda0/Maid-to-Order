# Pruebas Unitarias – Maid to Order

Resumen de las pruebas implementadas en los microservicios Spring Boot y en la app Android basada en Jetpack Compose. Todas las suites usan JUnit 5, MockK y coroutines-test (cuando aplica).

---

## 1. Backend (Spring Boot / Kotlin)

**Rutas de archivo**
- `dishes-service/src/test/kotlin/com/maidtoorder/dishservice/service/DishServiceTest.kt`
- `orders-service/src/test/kotlin/com/maidtoorder/orderservice/service/OrderServiceTest.kt`

**Cobertura**

| Clase | Casos | Descripción |
| --- | --- | --- |
| `DishServiceTest` | 4 | Obtener todos los platos, crear plato, actualizar y eliminar, validando interacciones con el repositorio. |
| `OrderServiceTest` | 2 | Crear pedido con cálculo de total (mock de RestTemplate) y actualizar estado del pedido. |

**Comando backend**
```bash
cd Maid-to-Order-Backend
../gradlew test
```

Reportes HTML: `dishes-service/build/reports/tests/test/index.html` y `orders-service/build/reports/tests/test/index.html`.

---

## 2. App Android (Compose / MVVM)

**Rutas de archivo**
- `app/src/test/java/pkg/maid_to_order/viewmodel/WeatherViewModelTest.kt`
- `app/src/test/java/pkg/maid_to_order/viewmodel/MenuViewModelTest.kt`

**Cobertura**

| Clase | Casos | Descripción |
| --- | --- | --- |
| `WeatherViewModelTest` | 2 | Verifica estados Success/Error al consumir Open-Meteo usando coroutines-test y MockK sobre `WeatherClient`. |
| `MenuViewModelTest` | 2 | Caso exitoso contra `RetrofitClient.api` mockeado y fallback local cuando la API falla. |

**Comando app**
```bash
./gradlew :app:testDebugUnitTest
```

Reporte: `app/build/reports/tests/testDebugUnitTest/index.html`.

---

## 3. Buenas prácticas destacadas
- Pruebas unitarias puras (repositorios y clientes HTTP mockeados).
- Uso de `MainDispatcherRule` para controlar `Dispatchers.Main` en ViewModels.
- Validaciones tanto del resultado como de las interacciones (`verify { ... }` en MockK).

---

## 4. Checklist para la defensa
- [ ] Mostrar las rutas de prueba en el IDE (backend + app) y explicar brevemente cada clase.
- [ ] Ejecutar `../gradlew test` dentro de `Maid-to-Order-Backend`.
- [ ] Ejecutar `./gradlew :app:testDebugUnitTest` en la raíz del proyecto.
- [ ] Enseñar los reportes HTML si se requiere evidencia adicional.

---
