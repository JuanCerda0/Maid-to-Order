Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Iniciando Backend - Maid to Order" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Set-Location "Maid-to-Order-Backend"
Write-Host "Ejecutando backend en http://localhost:8080/api" -ForegroundColor Green
Write-Host ""
.\gradlew.bat bootRun


