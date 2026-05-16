@echo off
title Kafka Consumer (Date Pendul - Live Only)
cd /d %KAFKA_HOME%
echo Astept date NOI de la pendul...
:: Am scos --from-beginning pentru a ignora mesajele vechi ramase accidental
.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic pendul-date
pause

