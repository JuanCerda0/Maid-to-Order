# Pruebas Unitarias – Maid to Order Backend

Backend compuesto por microservicios Spring Boot/Kotlin. Las pruebas cubren la lógica de negocio de `dishes-service` y `orders-service` usando JUnit 5 + MockK.

---

## 1. Ubicación de los archivos

- **dishes-service**  
  `Maid-to-Order-Backend/dishes-service/src/test/kotlin/com/maidtoorder/dishservice/service/DishServiceTest.kt`

- **orders-service**  
  `Maid-to-Order-Backend/orders-service/src/test/kotlin/com/maidtoorder/orderservice/service/OrderServiceTest.kt`

---

## 2. Resumen de pruebas por clase

### DishServiceTest
| # | Caso | Qué valida |
|---|------|------------|
|1|Obtener todos los platos|Se mockea `findAll` y se verifica que se retorne la lista completa.|
|2|Crear plato|Se guarda un `DishCreateDto` y se revisa que el resultado contenga nombre y precio esperados.|
|3|Actualizar plato|Se ubica un plato existente, se aplica `DishUpdateDto` y se verifica que cambien los campos.|
|4|Eliminar plato|Confirma que `deleteById` se invoque cuando el ID existe.|

### OrderServiceTest
| # | Caso | Qué valida |
|---|------|------------|
|1|Crear pedido y calcular total|Mockea `RestTemplate.getForObject` para obtener datos del plato, crea un pedido y comprueba el total.|
|2|Actualizar estado|Busca un pedido `PENDING`, aplica `OrderStatusUpdateDto(CONFIRMED)` y verifica estado/`updatedAt`.|

---

## 3. Cómo presentarlo

- **Given / When / Then**: explica que cada test sigue este patrón para claridad.
- **Mocks**: repositorios y RestTemplate se mockean, por lo que las pruebas no dependen de H2 ni de otros servicios.
- **Cobertura**: se cubren operaciones CRUD y reglas (cálculo de totales, cambio de estado).
- **Verificaciones**: además del resultado, se usan `verify { ... }` para asegurar interacción correcta con el repositorio.

---

## 4. Comandos de ejecución

Ejecutar todo el backend:
```bash
cd Maid-to-Order-Backend
../gradlew test
```

Ejecutar un módulo en particular:
```bash
../gradlew dishes-service:test
../gradlew orders-service:test
```

Reportes HTML:
- `Maid-to-Order-Backend/dishes-service/build/reports/tests/test/index.html`
- `Maid-to-Order-Backend/orders-service/build/reports/tests/test/index.html`

---

## 5. Buenas prácticas resaltadas

- Los tests de los servicios son unitarios: no tocan la base ni el resto de microservicios.
- Se valida tanto el resultado (totales, estados) como las interacciones con el repositorio.
- Mantienen equivalencia con la capa Service del patrón Controller → Service → Repository.

---

## 6. Checklist para la defensa

- [ ] Mostrar la estructura de submódulos en el IDE (`dishes-service`, `orders-service`).
- [ ] Abrir `DishServiceTest` y `OrderServiceTest`, explicar rápidamente cada caso.
- [ ] Ejecutar `../gradlew test` dentro de `Maid-to-Order-Backend` y mostrar la salida verde.
- [ ] (Opcional) Abrir el reporte HTML para evidenciar los tests pasados.

---
