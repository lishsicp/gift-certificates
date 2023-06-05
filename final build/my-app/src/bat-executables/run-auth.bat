@echo off
cmd /k java -jar "D:\gifts\gift-certificates\module #4. Authentication & Spring Security\oauth2-server\target\oauth2-server-0.0.1-SNAPSHOT.jar" "--spring.profiles.active=dev"
if %errorlevel% equ 0 exit
