package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import model.cuentas.CuentaBancaria;
import model.transacciones.Historial;
import model.transacciones.Transaccion;
import model.transacciones.TransaccionTransferencia;
import repository.DataStore;

/**
 * Clase de servicio que gestiona las operaciones relacionadas con transacciones bancarias.
 * Proporciona funcionalidades para ver historial de transacciones, ver ultimas transacciones,
 * y realizar transferencias entre cuentas.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Historial
 * @see Transaccion
 * @see TransaccionTransferencia
 * @see DataStore
 */
public class TransaccionService {

    // ==================== ATRIBUTOS ====================
    
    /** Almacenamiento de datos del sistema. */
    private DataStore dataStore;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor del servicio de transacciones.
     * Obtiene la instancia unica del DataStore.
     */
    public TransaccionService() {
        this.dataStore = DataStore.getInstance();
    }

    // ==================== METODOS DE CONSULTA DE HISTORIAL ====================
    
    /**
     * Muestra el historial completo de transacciones del sistema.
     * Incluye todas las transacciones con sus detalles y los totales de depositos y retiros.
     * Para transferencias, muestra adicionalmente cuenta origen y destino.
     */
    public void verHistorial() {
        Historial historial = dataStore.getHistorialGlobal();
        System.out.println("\n=== HISTORIAL DE TRANSACCIONES ===");
        System.out.println("Periodo: " + historial.getPeriodoDesde() + " - " + historial.getPeriodoHasta());
        
        List<Transaccion> transacciones = historial.getListaTransacciones();
        if (transacciones.isEmpty()) {
            System.out.println("No hay transacciones registradas");
            return;
        }

        for (Transaccion t : transacciones) {
            System.out.println(t);
            if (t instanceof TransaccionTransferencia) {
                TransaccionTransferencia tt = (TransaccionTransferencia) t;
                System.out.println("   → De: " + tt.getCuentaOrigen() + 
                                 " | A: " + tt.getCuentaDestino());
            }
        }

        System.out.println("\nTotales:");
        System.out.println("Depositos: $" + historial.calcularTotalDepositado());
        System.out.println("Retiros: $" + historial.calcularTotalRetirado());
    }

    /**
     * Muestra las ultimas 5 transacciones registradas en el sistema.
     * Util para ver la actividad reciente.
     */
    public void verUltimasTransacciones() {
        Historial historial = dataStore.getHistorialGlobal();
        List<Transaccion> ultimas = historial.ObtenerUltimasTransacciones(5);
        
        System.out.println("\n=== ULTIMAS 5 TRANSACCIONES ===");
        for (Transaccion t : ultimas) {
            System.out.println(t);
        }
    }

    // ==================== METODO DE TRANSFERENCIA ====================
    
    /**
     * Realiza una transferencia entre cuentas bancarias.
     * Valida existencia de cuenta destino, saldo suficiente y cupos diarios.
     * Detecta transacciones sospechosas y muestra advertencias.
     * Actualiza el saldo de ambas cuentas y registra la transaccion.
     * 
     * @param scanner Scanner para entrada de datos del usuario
     * @param cuentaService Servicio de cuentas para obtener la cuenta origen
     */
    public void transferir(Scanner scanner, CuentaService cuentaService) {
        CuentaBancaria origen = cuentaService.getCuentaActual();
        if (origen == null) {
            System.out.println("No hay sesion activa");
            return;
        }

        System.out.println("\n=== TRANSFERENCIA ===");
        System.out.print("Ingrese cuenta destino: ");
        String numDestino = scanner.nextLine();

        CuentaBancaria destino = dataStore.getCuenta(numDestino);
        if (destino == null) {
            System.out.println("Cuenta destino no encontrada");
            return;
        }

        System.out.println("Titular: " + destino.getNombreTitular());
        System.out.print("Confirma? (S/N): ");
        String confirma = scanner.nextLine();

        if (!confirma.equalsIgnoreCase("S")) {
            System.out.println("Transferencia cancelada");
            return;
        }

        System.out.print("Ingrese monto a transferir: $");
        BigDecimal monto = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Referencia (opcional): ");
        String referencia = scanner.nextLine();

        if (!origen.puedeTransferir(monto)) {
            System.out.println("No puede transferir ese monto. Verifique saldo o cupo diario.");
            return;
        }

        TransaccionTransferencia transaccion = new TransaccionTransferencia(monto, 
            origen.getNumeroCuenta(), destino.getNumeroCuenta(), destino.getNombreTitular());
        
        if (dataStore.getLogicaNegocio().esTransaccionSospechosa(transaccion)) {
            System.out.println("ADVERTENCIA: Transaccion marcada para revision");
        }

        origen.transferir(monto);
        destino.depositar(monto);
        
        transaccion.setReferenciaTransferencia(referencia.isEmpty() ? 
            transaccion.getReferenciaTransferencia() : referencia);
        transaccion.completarTransaccion(origen.getSaldoDisponible());
        
        dataStore.registrarTransaccion(transaccion);
        dataStore.updateCuenta(origen);
        dataStore.updateCuenta(destino);

        System.out.println("Transferencia exitosa!");
        System.out.println("Nuevo saldo: $" + origen.getSaldoDisponible());
    }
}//fin de la clase