package service;

import java.math.BigDecimal;
import java.util.Scanner;
import model.cuentas.CuentaBancaria;
import model.transacciones.TransaccionDeposito;
import model.transacciones.TransaccionRetiro;
import repository.DataStore;

/**
 * Clase de servicio que gestiona las operaciones relacionadas con cuentas bancarias.
 * Proporciona funcionalidades de autenticacion, consulta de saldo, retiros,
 * depositos, cambio de contrasena y cierre de sesion.
 * Mantiene la cuenta actualmente activa en sesion.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 * @see CajeroService
 * @see DataStore
 */
public class CuentaService {

    // ==================== ATRIBUTOS ====================
    
    /** Almacenamiento de datos del sistema. */
    private DataStore dataStore;
    
    /** Cuenta bancaria actualmente autenticada en sesion. */
    private CuentaBancaria cuentaActual;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor del servicio de cuentas.
     * Obtiene la instancia unica del DataStore.
     */
    public CuentaService() {
        this.dataStore = DataStore.getInstance();
    }

    // ==================== METODOS DE AUTENTICACION ====================
    
    /**
     * Autentica un usuario en el sistema.
     * Verifica que la cuenta exista, no este bloqueada y que la contrasena sea correcta.
     * Registra intentos fallidos y bloquea la cuenta tras 3 intentos.
     * 
     * @param numeroCuenta Numero de cuenta del usuario
     * @param contrasena Contrasena del usuario
     * @return true si la autenticacion es exitosa, false en caso contrario
     */
    public boolean autenticar(String numeroCuenta, String contrasena) {
        CuentaBancaria cuenta = dataStore.getCuenta(numeroCuenta);
        
        if (cuenta == null) {
            System.out.println("Cuenta no encontrada");
            return false;
        }

        if (cuenta.isEstaBloqueada()) {
            System.out.println("Cuenta bloqueada. Contacte al banco.");
            return false;
        }

        if (cuenta.validarContrasena(contrasena)) {
            this.cuentaActual = cuenta;
            cuenta.registrarAcceso();
            dataStore.updateCuenta(cuenta);
            System.out.println("Bienvenido " + cuenta.getNombreTitular());
            return true;
        } else {
            cuenta.registrarIntentoFallido();
            dataStore.updateCuenta(cuenta);
            System.out.println("Contrasena incorrecta. Intentos: " + cuenta.getIntentosFallidos() + "/3");
            return false;
        }
    }

    // ==================== METODOS DE CONSULTA ====================
    
    /**
     * Muestra el saldo actual de la cuenta activa.
     * Incluye numero de cuenta, titular, saldo y tipo de cuenta.
     * Requiere una sesion activa.
     */
    public void consultarSaldo() {
        if (cuentaActual == null) {
            System.out.println("No hay sesion activa");
            return;
        }
        System.out.println("\n=== CONSULTA DE SALDO ===");
        System.out.println("Cuenta: " + cuentaActual.getNumeroCuenta());
        System.out.println("Titular: " + cuentaActual.getNombreTitular());
        System.out.println("Saldo disponible: $" + cuentaActual.getSaldoDisponible());
        System.out.println("Tipo de cuenta: " + cuentaActual.getClass().getSimpleName());
    }

    // ==================== METODOS DE OPERACIONES ====================
    
    /**
     * Realiza un retiro de efectivo desde la cuenta activa.
     * Valida disponibilidad de efectivo en el cajero y limites de la cuenta.
     * Registra la transaccion en el sistema.
     * 
     * @param scanner Scanner para entrada de datos del usuario
     * @param cajeroService Servicio de cajero para dispensar efectivo
     */
    public void realizarRetiro(Scanner scanner, CajeroService cajeroService) {
        if (cuentaActual == null) {
            System.out.println("No hay sesion activa");
            return;
        }

        System.out.println("\n=== RETIRO DE EFECTIVO ===");
        System.out.println("Saldo disponible: $" + cuentaActual.getSaldoDisponible());
        System.out.println("Cupo diario restante: $" + 
            cuentaActual.getCupoDiarioRetiro().subtract(cuentaActual.getMontoRetiradoHoy()));
        
        System.out.print("Ingrese monto a retirar: $");
        BigDecimal monto = scanner.nextBigDecimal();
        scanner.nextLine();

        if (!cajeroService.validarEfectivoDisponible(monto)) {
            System.out.println("Cajero sin suficiente efectivo");
            return;
        }

        if (!cuentaActual.puedeRetirar(monto)) {
            System.out.println("No puede retirar ese monto. Verifique saldo o cupo diario.");
            return;
        }

        cuentaActual.retirar(monto);
        cajeroService.dispensarEfectivo(monto);
        
        TransaccionRetiro transaccion = new TransaccionRetiro(monto, "CAJERO");
        transaccion.completarTransaccion(cuentaActual.getSaldoDisponible());
        dataStore.registrarTransaccion(transaccion);
        dataStore.updateCuenta(cuentaActual);

        System.out.println("Retiro exitoso!");
        System.out.println("Nuevo saldo: $" + cuentaActual.getSaldoDisponible());
    }

    /**
     * Realiza un deposito en la cuenta activa.
     * Permite seleccionar el medio de deposito (efectivo o cheque).
     * Registra la transaccion en el sistema.
     * 
     * @param scanner Scanner para entrada de datos del usuario
     */
    public void realizarDeposito(Scanner scanner) {
        if (cuentaActual == null) {
            System.out.println("No hay sesion activa");
            return;
        }

        System.out.println("\n=== DEPOSITO ===");
        System.out.print("Ingrese monto a depositar: $");
        BigDecimal monto = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.println("Medio de deposito:");
        System.out.println("1. Efectivo");
        System.out.println("2. Cheque");
        System.out.print("Seleccione opcion: ");
        int opcion = scanner.nextInt();
        scanner.nextLine();

        String medio = opcion == 1 ? "EFECTIVO" : "CHEQUE";

        cuentaActual.depositar(monto);
        
        TransaccionDeposito transaccion = new TransaccionDeposito(monto, medio);
        transaccion.completarTransaccion(cuentaActual.getSaldoDisponible());
        dataStore.registrarTransaccion(transaccion);
        dataStore.updateCuenta(cuentaActual);

        System.out.println("Deposito exitoso!");
        System.out.println("Nuevo saldo: $" + cuentaActual.getSaldoDisponible());
    }

    /**
     * Cambia la contrasena de la cuenta activa.
     * Requiere validar la contrasena actual y que la nueva coincida con su confirmacion.
     * 
     * @param scanner Scanner para entrada de datos del usuario
     */
    public void cambiarContrasena(Scanner scanner) {
        if (cuentaActual == null) {
            System.out.println("No hay sesion activa");
            return;
        }

        System.out.println("\n=== CAMBIO DE CONTRASENA ===");
        System.out.print("Ingrese contrasena actual: ");
        String actual = scanner.nextLine();

        if (!cuentaActual.validarContrasena(actual)) {
            System.out.println("Contrasena incorrecta");
            return;
        }

        System.out.print("Ingrese nueva contrasena: ");
        String nueva = scanner.nextLine();
        System.out.print("Confirme nueva contrasena: ");
        String confirmacion = scanner.nextLine();

        if (!nueva.equals(confirmacion)) {
            System.out.println("Las contrasenas no coinciden");
            return;
        }

        cuentaActual.setContrasenaHash(nueva);
        dataStore.updateCuenta(cuentaActual);
        System.out.println("Contrasena actualizada exitosamente");
    }

    /**
     * Cierra la sesion actual.
     * Limpia la cuenta activa y muestra un mensaje de despedida.
     */
    public void cerrarSesion() {
        if (cuentaActual != null) {
            System.out.println("Hasta luego " + cuentaActual.getNombreTitular());
            this.cuentaActual = null;
        }
    }

    // ==================== GETTERS ====================
    
    /**
     * Obtiene la cuenta bancaria actualmente en sesion.
     * 
     * @return Cuenta activa o null si no hay sesion
     */
    public CuentaBancaria getCuentaActual() {
        return cuentaActual;
    }
}//fin de la clase