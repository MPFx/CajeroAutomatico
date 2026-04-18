# Cajero Automático

## Versión
**1.0**

## Descripción
Sistema de cajero automático desarrollado en Java que permite gestionar cuentas bancarias, realizar retiros, depósitos, transferencias y consultar historial de transacciones.

## Alcance

### ✅ Qué hace
- Autenticación de usuarios por número de cuenta y contraseña
- Consulta de saldo disponible
- Retiro de efectivo con validación de saldo y cupo diario
- Depósito de dinero
- Transferencias entre cuentas
- Historial de transacciones
- Cambio de contraseña
- Múltiples tipos de cuenta (Ahorro, Corriente, Juventud, Tercera Edad)
- Cálculo de intereses según tipo de cuenta
- Validación de límites diarios y bloqueo por intentos fallidos

### ❌ Qué NO hace
- No tiene interfaz gráfica (solo consola)
- No persiste datos en base de datos real (usa DataStore en memoria)
- No utiliza cifrado real para contraseñas (hash simulado)
- No se conecta a bancos reales
- No maneja múltiples cajeros simultáneos

## Documentación Javadoc
[https://mpfx.github.io/CajeroAutomatico/](https://mpfx.github.io/CajeroAutomatico/)

## Tecnologías
- Java
- Javadoc
- GitHub Pages

## Modo de uso
**Este proyecto NO es una aplicación visual/gráfica.**
Funciona exclusivamente por consola (CLI - Command Line Interface).

## Propósito
**Educativo / Pedagógico**
Este proyecto fue desarrollado con fines de aprendizaje y práctica de:
- Programación orientada a objetos en Java
- Herencia y clases abstractas
- Documentación técnica con Javadoc
- Control de versiones con Git y GitHub Pages

## Derechos de autor
**© 2026 ISC Israel de Jesus Mar Parada (MPFx++)**

Todos los derechos reservados.

Este software se proporciona "tal cual", sin garantías de ningún tipo. 
No está permitida su reproducción, distribución o modificación sin autorización expresa del autor.
