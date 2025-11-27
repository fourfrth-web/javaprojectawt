@echo off
REM Simple Drawing App - Direct Compilation (No Packages)
REM Compiles the simple Java files in the root directory

echo ========================================
echo  Simple Drawing Studio
echo ========================================
echo.

REM Navigate to project directory
cd /d "C:\Users\vruta\Desktop\VSCode\OtherProjects\JavaProjectSimple"

echo Compiling Java files...
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile to bin directory
javac -d bin SimpleDrawingApp.java DrawingCanvas.java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo  ERROR: Compilation failed!
    echo ========================================
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Compilation successful!
echo ========================================
echo.
echo Running application...
echo.

REM Run the application from bin directory
java -cp bin SimpleDrawingApp

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ========================================
    echo  ERROR: Application failed to run!
    echo ========================================
    pause
    exit /b 1
)

echo.
echo ========================================
echo  Application closed successfully
echo ========================================
pause
