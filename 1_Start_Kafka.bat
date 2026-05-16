@echo off
title Kafka Server (Motorul) - FRESH START
cd /d %KAFKA_HOME%

:: 1. Sterge datele vechi (pentru a porni de la timpul 0)
echo Curat datele vechi din /tmp...
if exist "C:\tmp\kraft-combined-logs" rd /s /q "C:\tmp\kraft-combined-logs"

:: 2. Formateaza stocarea SI porneste serverul intr-o singura secventa
echo Formatez si pornesc serverul...
set PATH=%PATH%;C:\Windows\System32\wbem
set KAFKA_HEAP_OPTS=-Xmx1G -Xms1G

:: Folosim CALL pentru ca scriptul sa nu se opreasca dupa formatare
call .\bin\windows\kafka-storage.bat format --standalone -t 6zTnOrFOTMisCgQmZ1zx9g -c .\config\server.properties

echo Pornesc serverul propriu-zis...
.\bin\windows\kafka-server-start.bat .\config\server.properties
pause