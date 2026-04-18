package service;

import java.math.BigDecimal;
import model.infraestructura.Cajero;
import repository.DataStore;

/**
 * Clase de servicio que gestiona las operaciones relacionadas con el cajero automatico.
 * Proporciona funcionalidades para consultar el estado del cajero, validar disponibilidad
 * de efectivo, dispensar efectivo, cargar efectivo y cambiar el estado operativo.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Cajero
 * @see DataStore
 */
public class CajeroService {

    // ==================== ATRIBUTOS ====================
    
    /** Almacenamiento de datos del sistema. */
    private DataStore dataStore;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor del servicio de cajero.
     * Obtiene la instancia unica del DataStore.
     */
    public CajeroService() {
        this.dataStore = DataStore.getInstance();
    }

    // ==================== METODOS DE CONSULTA ====================
    
    /**
     * Muestra el estado actual del cajero.
     * Incluye ID, ubicacion, efectivo disponible, limite por transaccion y estado operativo.
     */
    public void consultarEstadoCajero() {
        Cajero cajero = dataStore.getCajeroActivo();
        System.out.println("\n=== ESTADO DEL CAJERO ===");
        System.out.println("ID: " + cajero.getIdCajero());
        System.out.println("Ubicacion: " + cajero.getUbicacionCajero());
        System.out.println("Efectivo disponible: $" + cajero.getEfectivoDisponible());
        System.out.println("Limite por transaccion: $" + cajero.getLimitePorTransaccion());
        System.out.println("Estado: " + (cajero.isEstaOperativo() ? "OPERATIVO" : "FUERA DE SERVICIO"));
    }

    /**
     * Valida si el cajero tiene suficiente efectivo disponible.
     * 
     * @param monto Monto a validar
     * @return true si hay suficiente efectivo, false en caso contrario
     */
    public boolean validarEfectivoDisponible(BigDecimal monto) {
        Cajero cajero = dataStore.getCajeroActivo();
        return cajero.validarDisponibilidadEfectivo(monto);
    }

    // ==================== METODOS DE OPERACION ====================
    
    /**
     * Dispensa efectivo del cajero.
     * Disminuye el efectivo disponible en el monto especificado.
     * 
     * @param monto Monto a dispensar
     */
    public void dispensarEfectivo(BigDecimal monto) {
        Cajero cajero = dataStore.getCajeroActivo();
        cajero.DispensarEfectivo(monto);
    }

    /**
     * Carga efectivo en el cajero.
     * Incrementa el efectivo disponible en el monto especificado.
     * 
     * @param monto Monto de efectivo a cargar
     */
    public void cargarEfectivo(BigDecimal monto) {
        Cajero cajero = dataStore.getCajeroActivo();
        cajero.CargarEfectivo(monto);
        System.out.println("Cajero cargado con $" + monto);
    }

    /**
     * Cambia el estado operativo del cajero.
     * 
     * @param operativo true para poner el cajero operativo, false para desactivarlo
     */
    public void cambiarEstado(boolean operativo) {
        Cajero cajero = dataStore.getCajeroActivo();
        cajero.CambiarEstado(operativo);
        System.out.println("Estado del cajero cambiado a: " + 
            (operativo ? "OPERATIVO" : "FUERA DE SERVICIO"));
    }
}//fin de la clase