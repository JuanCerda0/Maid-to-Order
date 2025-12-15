# Maid to Order

Sistema completo para restaurantes que incluye una app Android en Kotlin/Compose y un backend en Kotlin/Spring Boot dividido en microservicios. El proyecto permite mostrar el menu, gestionar pedidos, administrar platos y revisar un historial local, todo respaldado por pruebas unitarias, persistencia y un APK firmado listo para distribución.

## Arquitectura general

| Capa | Tecnologias | Detalles |
| --- | --- | --- |
| App Android | Kotlin, Jetpack Compose, MVVM | ViewModels con StateFlow, Retrofit, DataStore (modo oscuro y carrito) y Room (historial de pedidos). |
| Backend | Spring Boot 3.2, Kotlin, microservicios | `gateway-service` + `dishes-service` + `orders-service` + `specials-service`, todos con H2 y capas Controller/Service/Repository. |
| API externa | Open-Meteo | Consumida via Retrofit y mostrada en la pantalla Home para enriquecer la experiencia del usuario. |

La app navega mediante `Screen.kt`, cada pantalla (Home, Detalle, Carrito, Formulario, Ajustes, Admin, Historial) usa su ViewModel dedicado y los datos fluyen desde repositorios locales o remotos. El backend mantiene la separación clásica Controller → Service → Repository, equivalente a la capa ViewModel en MVVM.

## Estructura del repositorio
```
.
├── app/                                    # App Android
│   ├── build.gradle.kts                    # Configuración con Compose, Retrofit, Room, DataStore
│   └── src/main/java/pkg/maid_to_order
│       ├── data/                           # Modelos y rutas de navegación
│       ├── network/                        # Retrofit API/DTO/Mappers (propia + Open-Meteo)
│       ├── repository/                     # DataStore helpers + OrderHistoryRepository (Room)
│       ├── ui/                             # Screens, components y tema
│       ├── utils/                          # Utilidades (p.ej. Cámara)
│       └── viewmodel/                      # ViewModels MVVM (Menu, Cart, Form, Settings, Weather, History)
├── Maid-to-Order-Backend/                  # Proyecto multi módulo
│   ├── gateway-service/                    # Spring Cloud Gateway (puerto 8080)
│   ├── dishes-service/                     # CRUD de platos (puerto 8081)
│   ├── orders-service/                     # Pedidos + integración con dishes (puerto 8082)
│   └── specials-service/                   # Platos especiales (puerto 8083)
└── ...                                     # Scripts, keystore y documentación
```

## Funcionalidades destacadas

### App Android
- **Home**: búsqueda por categoría, tarjetas animadas, carrusel de especiales y widget de clima (Open-Meteo).
- **Detalle**: muestra información completa del plato y permite añadirlo al carrito con animaciones.
- **Carrito + Formulario**: valida el número de mesa, envía el pedido al backend y guarda el historial en Room.
- **Administrador**: CRUD de platos consumiendo directamente los endpoints REST (POST/PUT/DELETE) y usando cámara/galería.
- **Ajustes/Historial**: cambios de tema vía DataStore y consulta de pedidos guardados localmente.

### Backend
- **Microservicios independientes** con datos iniciales y endpoints REST (`/api/dishes`, `/api/orders`, `/api/special-dishes`).
- **Orders-service** consulta a dishes-service para validar precios antes de crear el pedido.
- **Gateway-service** expone una única URL (`http://localhost:8080/api`) para simplificar la integración con Android.

## Instalación y ejecución

### Backend
Desde la carpeta `Maid-to-Order-Backend` puedes iniciar cada servicio en terminales separados:
```bash
./gradlew gateway-service:bootRun
./gradlew dishes-service:bootRun
./gradlew orders-service:bootRun
./gradlew specials-service:bootRun
```

Los servicios corren en los puertos 8080 (gateway), 8081 (platos), 8082 (pedidos) y 8083 (especiales). Si prefieres compilar todo en un paso usa `./gradlew build` dentro de la carpeta backend.

### App Android
1. Abre el módulo `app/` en Android Studio Giraffe o superior.
2. Asegúrate de que el backend gateway esté activo y apunta a `http://10.0.2.2:8080/api/` en `RetrofitClient.kt` (para emulador). Para un dispositivo físico, reemplaza por la IP local.
3. Sincroniza el proyecto y ejecuta en emulador o dispositivo.
4. Para obtener el APK firmado basta con `./gradlew :app:assembleRelease`. El archivo queda en `app/build/outputs/apk/release/app-release.apk` firmado con `maid-to-order-keystore.jks`.

## Pruebas unitarias

| Módulo | Comando | Cobertura |
| --- | --- | --- |
| Backend | `cd Maid-to-Order-Backend && ../gradlew test` | Servicios `DishService` y `OrderService` con MockK y validación de reglas de negocio. |
| App | `./gradlew :app:testDebugUnitTest` | `WeatherViewModelTest` usando coroutines-test + MockK (API externa). |

Los reportes de Gradle quedan en `*/build/reports/tests/`. Puedes ejecutar ambos comandos antes de la defensa para demostrar los resultados.

## Persistencia local y recursos nativos
- DataStore guarda el tema oscuro y el carrito (`SettingsViewModel`, `CartViewModel`).
- Room almacena un historial de pedidos (`HistoryViewModel`, `HistoryScreen`), accesible desde Ajustes.
- La pantalla de admin integra cámara y galería usando `CameraUtils` y `ActivityResultContracts` (permisos nativos).

## Consumo de API externa
`WeatherViewModel` consulta Open-Meteo mediante `WeatherClient`. La tarjeta “Clima actual” en Home muestra temperatura, viento y estado, y permite reintentar en caso de error. Esto demuestra la integración de un servicio externo sin interferir con los microservicios propios.

## Defensa recomendada
1. **Setup**: abrir Android Studio, correr el gateway y los módulos desde consola.
2. **Arquitectura**: mostrar carpetas `ui/`, `viewmodel/`, `repository/` y los módulos del backend (controller/service/repository).
3. **UI/UX**: recorrer Home (animaciones + clima), Carrito/Formulario (validaciones) y Ajustes/Historial (Room + DataStore).
4. **Admin**: crear/editar un plato mostrando llamadas reales al backend (usa logcat o el logging interceptor).
5. **Pruebas**: ejecutar `../gradlew test` dentro del backend y `./gradlew :app:testDebugUnitTest` para la app.
6. **APK**: enseñar `app-release.apk` generado por `assembleRelease` y explicar el uso del `.jks` en `signingConfigs`.
7. **Cambio en vivo**: por ejemplo, agregar una validación en `FormViewModel` o un nuevo filtro en `MenuViewModel` para demostrar dominio del código.

Con esta guía tendrás cubiertos los requisitos académicos (MVVM, microservicios, API externa, persistencia, pruebas y APK firmado) y una narrativa clara para la defensa.
