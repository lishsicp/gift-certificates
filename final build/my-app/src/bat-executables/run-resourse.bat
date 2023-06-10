@echo off
cmd /k java -jar "D:\gifts\gift-certificates\module #4. Authentication & Spring Security\oauth2-resource-server\target\gift-certificates-oauth2-resource-server-0.0.1-SNAPSHOT.jar" "--spring.profiles.active=dev"
if %errorlevel% equ 0 exit
