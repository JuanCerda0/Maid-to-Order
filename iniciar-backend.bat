@echo off
setlocal
echo ========================================
echo  Iniciando Microservicios - Maid to Order
echo ========================================
echo Este script abrira una ventana por servicio.
echo Para detener el backend cierra todas las ventanas que se abren a continuacion.
echo.
pushd Maid-to-Order-Backend

echo [1/4] dishes-service (puerto 8081)
start "dishes-service" cmd /k "call gradlew.bat dishes-service:bootRun"
timeout /t 2 >nul

echo [2/4] specials-service (puerto 8083)
start "specials-service" cmd /k "call gradlew.bat specials-service:bootRun"
timeout /t 2 >nul

echo [3/4] orders-service (puerto 8082)
start "orders-service" cmd /k "call gradlew.bat orders-service:bootRun"
timeout /t 2 >nul

echo [4/4] gateway-service (puerto 8080)
start "gateway-service" cmd /k "call gradlew.bat gateway-service:bootRun"

popd
echo.
echo Todos los servicios fueron lanzados. La app Android debe apuntar a http://10.0.2.2:8080/api/
echo Presiona una tecla para cerrar esta ventana (los servicios seguiran corriendo en las otras ventanas).
pause >nul
endlocal

