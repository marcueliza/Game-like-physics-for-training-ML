@echo off
start "Kafka Server" cmd /k "1_Start_Kafka.bat"
timeout /t 5
start "Kafka Consumer" cmd /k "2_Start_Consumer.bat"