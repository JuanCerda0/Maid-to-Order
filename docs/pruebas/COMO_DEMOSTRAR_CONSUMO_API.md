# Cómo demostrar que la app consume APIs

## 1. Mostrar el código relevante (parte técnica)

| Archivo | Ubicación | Qué enseñar |
| --- | --- | --- |
| `RetrofitClient.kt` | `app/src/main/java/pkg/maid_to_order/network/api` | Configuración común para `http://10.0.2.2:8080/api/` (gateway), interceptor OkHttp y timeouts. |
| `MaidToOrderApi.kt` | misma carpeta | Declaraciones `@GET`, `@POST`, `@PUT`, `@DELETE` para `/dishes`, `/orders`, `/special-dishes`. |
| `WeatherClient.kt` / `WeatherApi.kt` | `network/api` | Segundo Retrofit apuntando a `https://api.open-meteo.com/v1/forecast`. |
| `AndroidManifest.xml` + `res/xml/network_security_config.xml` | `app/src/main/...` | `usesCleartextTraffic=true` + dominios permitidos (`10.0.2.2`, `localhost`) que desbloquean los POST/PUT/DELETE del panel Admin. |
| `MenuViewModel.kt`, `FormViewModel.kt`, `WeatherViewModel.kt` | `viewmodel` | Ejemplos de uso real: `RetrofitClient.api.getDishes()`, `createOrder`, `WeatherClient.api.getCurrentWeather()`, manejo de estados/error. |

Explica también la arquitectura de backend: la app solo conoce el **gateway** (`8080`) y éste enruta a los microservicios de platos (8081), pedidos (8082) y especiales (8083). Eso demuestra la integración con microservicios reales.

## 2. Demostración en vivo

### Logcat (más directo)
1. View → Tool Windows → Logcat. Filtra por `OkHttp`.
2. Acciones:
   - Abrir Home → aparecerán `GET /api/dishes` y `GET /api/special-dishes?today=true`.
   - Esperar la tarjeta de clima → `GET https://api.open-meteo.com/...`.
   - Crear/editar plato desde Admin → `POST/PUT /api/dishes`.
   - Confirmar pedido → `POST /api/orders`.
3. Comenta cada línea (método, URL, código HTTP, cuerpo).

### Network Inspector
1. View → Tool Windows → App Inspection → Network.
2. Muestra cada request/response, headers, payload y tiempos.
3. Útil para enseñar cómo se envía el JSON del formulario o del CRUD administrativo.

### Logs del backend
1. Mantén las consolas de `dishes-service`, `orders-service` y `specials-service` abiertas.
2. Cuando la app haga un request, verás en cada servicio los logs correspondientes (ideal para reforzar que son microservicios independientes).

### Postman/Navegador (opcional)
- `http://localhost:8080/api/dishes` devuelve exactamente los datos que la app muestra. Puedes abrirlo para reforzar que la API es pública y la app solo consume esa información.

## 3. Flujo de presentación recomendado

1. **Slide arquitectura:** App → Retrofit → Gateway → Microservicios → H2. Añade la API externa (Open-Meteo) como flujo paralelo.
2. **Slide flujo de datos:** `MenuViewModel` llama `getDishes()`, Retrofit hace `GET`, gateway enruta a `dishes-service`, la respuesta se mapea y la UI se actualiza. Repite para `FormViewModel` y `WeatherViewModel`.
3. **Slide código:** capturas de `RetrofitClient`, `MaidToOrderApi`, `WeatherClient` y los ViewModels.
4. **Demo en vivo:** Logcat/Network Inspector mostrando `GET`, `POST`, etc. Termina con la API externa para destacar la integración adicional.

## 4. Endpoints involucrados
- `GET /api/dishes`, `POST /api/dishes`, `PUT /api/dishes/{id}`, `DELETE /api/dishes/{id}` (desde Home/Admin).
- `GET /api/special-dishes?today=true`.
- `POST /api/orders`.
- `GET https://api.open-meteo.com/v1/forecast?...&current_weather=true` (tarjeta de clima).

## 5. Checklist antes de la demo
- [ ] Servicios Spring Boot levantados: gateway 8080 + dishes 8081 + orders 8082 + specials 8083.
- [ ] App configurada con `BASE_URL = http://10.0.2.2:8080/api/`.
- [ ] Manifest con `usesCleartextTraffic=true` y `network_security_config.xml` en `res/xml` (solo hace falta verificarlo una vez, pero menciónalo si preguntan por seguridad).
- [ ] Logcat filtrado por `OkHttp` o Network Inspector abierto.
- [ ] Mostrar los archivos clave de Retrofit y ViewModels.
- [ ] Ejecutar acciones en la app (listar, editar, crear pedido, refrescar clima) mientras se observan las peticiones.

## 6. FAQs
- **¿Qué pasa si uno de los microservicios falla?** MenuViewModel tiene fallback local para el menú y los ViewModels muestran mensajes de error al usuario.
- **¿Cómo se valida la API externa?** WeatherViewModel expone `WeatherUiState` y, en caso de error, se muestra una tarjeta con botón “Reintentar”.
- **¿Cómo sabemos que la app realmente habla con el backend?** Logcat + los logs de cada microservicio + la inspección de red en Android Studio muestran el tráfico real.
- **¿Por qué dos clientes Retrofit?** Uno para los microservicios propios (gateway) y otro para Open-Meteo, demostrando separación de responsabilidades y consumo de una API pública adicional.
