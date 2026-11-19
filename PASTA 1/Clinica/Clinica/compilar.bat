@echo off
cd /d "%~dp0"
if not exist out mkdir out
javac -encoding UTF-8 -d out src\*.java
if errorlevel 1 (
    echo.
    echo Falha na compilacao.
    exit /b 1
)
echo Compilado com sucesso em "out".
