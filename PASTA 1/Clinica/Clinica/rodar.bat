@echo off
cd /d "%~dp0"
if not exist out\Main.class (
    echo Projeto nao compilado. Compilando...
    call compilar.bat
    if errorlevel 1 exit /b 1
)
java -cp out Main
