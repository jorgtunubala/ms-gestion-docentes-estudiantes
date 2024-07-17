@echo off
set SPRING_DATASOURCE_URL=jdbc:mysql://docker.unicauca.edu.co:3307/computacion
set SPRING_DATASOURCE_USERNAME=computacion
set SPRING_DATASOURCE_PASSWORD=DB-C0mput4c10N
set SPRING_SERVER_ESTUDIANTES_PORT=8082

mvn clean package
