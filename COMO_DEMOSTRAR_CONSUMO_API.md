# Cómo Demostrar que la App Consume APIs

## 1. Mostrar el Código (Parte Técnica)

### A. Cliente Retrofit (`RetrofitClient.kt`)
**Ubicación:** `app/src/main/java/pkg/maid_to_order/network/api/RetrofitClient.kt`

**Qué mostrar:**
- Configuración de Retrofit con la URL base
- Interceptor de logging (muestra todas las peticiones HTTP)
- Cliente OkHttp configurado

**Puntos clave:**
```kotlin
private const val BASE_URL = "http://10.0.2.2:8080/api/"
val api: MaidToOrderApi = retrofit.create(MaidToOrderApi::class.java)
```

### B. Interface de la API (`MaidToOrderApi.kt`)
**Ubicación:** `app/src/main/java/pkg/maid_to_order/network/api/MaidToOrderApi.kt`

**Qué mostrar:**
- Anotaciones `@GET`, `@POST`, `@PUT`, `@DELETE`
- Endpoints definidos (dishes, orders, special-dishes)
- Parámetros con `@Query` y `@Path`

**Ejemplo:**
```kotlin
@GET("dishes")
suspend fun getDishes(...): Response<List<DishDto>>
```

### C. Uso en ViewModels (`MenuViewModel.kt`)
**Ubicación:** `app/src/main/java/pkg/maid_to_order/viewmodel/MenuViewModel.kt`

**Qué mostrar:**
- Línea 35: `RetrofitClient.api.getDishes()`
- Manejo de respuestas con `response.isSuccessful`
- Mappers que convierten DTOs a modelos de dominio
- Manejo de errores y fallback

## 2. Demostración Práctica (En Vivo)

### Opción A: Usar Logcat de Android Studio (RECOMENDADO)

1. **Abrir Logcat en Android Studio:**
   - View → Tool Windows → Logcat
   - O clic en la pestaña "Logcat" abajo

2. **Filtrar por "OkHttp":**
   - En el buscador de Logcat, escribe: `OkHttp`
   - Verás todas las peticiones HTTP en tiempo real

3. **Qué verás:**
   ```
   D/OkHttp: --> GET http://10.0.2.2:8080/api/dishes
   D/OkHttp: <-- 200 OK http://10.0.2.2:8080/api/dishes (234ms)
   D/OkHttp: [{"id":1,"name":"Katsudon",...}]
   ```

4. **Pasos para demostrar:**
   - Abre la app
   - Muestra Logcat con las peticiones
   - Explica: "Aquí vemos que la app hace GET a /api/dishes"
   - Añade un plato al carrito y muestra el POST a /api/orders

### Opción B: Usar Network Inspector (Android Studio)

1. **Abrir Network Inspector:**
   - View → Tool Windows → App Inspection
   - Pestaña "Network"

2. **Qué muestra:**
   - Lista de todas las peticiones HTTP
   - Request/Response completos
   - Tiempo de respuesta
   - Headers y body

3. **Ventajas:**
   - Interfaz visual más clara
   - Puedes ver el JSON completo
   - Fácil de entender para la audiencia

### Opción C: Usar Postman o Navegador

1. **Mientras la app corre, abre en el navegador:**
   ```
   http://localhost:8080/api/dishes
   ```

2. **Muestra que:**
   - La misma API que consume la app
   - Los mismos datos que aparecen en la app
   - La app consume esta API REST

## 3. Estructura para la Presentación

### Slide 1: Arquitectura de Consumo de API
```
App Android (Kotlin)
    ↓ Retrofit
Cliente HTTP (OkHttp)
    ↓ HTTP Requests
Backend Spring Boot
    ↓ JPA
Base de Datos H2
```

### Slide 2: Flujo de Datos
1. **Usuario abre la app** → MenuViewModel se inicializa
2. **MenuViewModel llama** → `RetrofitClient.api.getDishes()`
3. **Retrofit hace petición HTTP** → `GET http://10.0.2.2:8080/api/dishes`
4. **Backend responde** → JSON con lista de platos
5. **Mapper convierte** → DTOs a modelos de dominio
6. **UI se actualiza** → Lista de platos visible

### Slide 3: Código Clave
- Mostrar `RetrofitClient.kt` (configuración)
- Mostrar `MaidToOrderApi.kt` (endpoints)
- Mostrar `MenuViewModel.kt` líneas 30-51 (uso)

### Slide 4: Demostración en Vivo
- Abrir Logcat
- Mostrar peticiones HTTP en tiempo real
- Explicar cada petición

## 4. Puntos Clave para Mencionar

✅ **Retrofit:** Biblioteca para consumir APIs REST  
✅ **Coroutines:** Llamadas asíncronas (suspend functions)  
✅ **DTOs:** Data Transfer Objects para serialización JSON  
✅ **Mappers:** Conversión entre DTOs y modelos de dominio  
✅ **Manejo de Errores:** Try-catch y fallback a datos locales  
✅ **Logging:** Interceptor muestra todas las peticiones  

## 5. Endpoints que Consume la App

1. **GET /api/dishes** - Al abrir la app (cargar menú)
2. **GET /api/special-dishes?today=true** - Cargar especialidades
3. **POST /api/orders** - Al confirmar un pedido

## 6. Checklist para la Demostración

- [ ] Backend corriendo en `http://localhost:8080`
- [ ] App instalada en emulador/dispositivo
- [ ] Logcat abierto y filtrado por "OkHttp"
- [ ] Mostrar código de RetrofitClient
- [ ] Mostrar código de MenuViewModel
- [ ] Abrir la app y mostrar peticiones en Logcat
- [ ] Hacer un pedido y mostrar POST en Logcat
- [ ] Opcional: Mostrar respuesta en navegador

## 7. Respuestas a Preguntas Comunes

**P: ¿Qué pasa si el backend no está disponible?**  
R: La app tiene fallback a datos locales (línea 42-47 de MenuViewModel)

**P: ¿Cómo se manejan los errores?**  
R: Try-catch captura excepciones y muestra mensajes al usuario

**P: ¿Es seguro?**  
R: Para producción se usaría HTTPS, aquí es HTTP para desarrollo local

**P: ¿Cómo sabes que funciona?**  
R: Logcat muestra las peticiones HTTP en tiempo real, puedes ver request/response


