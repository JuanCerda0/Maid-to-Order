@echo off
echo ========================================
echo Iniciando Backend - Maid to Order
echo ========================================
echo.
cd Maid-to-Order-Backend
echo Ejecutando backend en http://localhost:8080/api
echo.
call gradlew.bat bootRun
pause


