@echo off
cmd /k mvn -f "D:\gifts\gift-certificates\module #4. Authentication & Spring Security\oauth2-server" package spring-boot:repackage -DskipTests
if %errorlevel% equ 0 exit
