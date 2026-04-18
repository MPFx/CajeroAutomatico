package model.infraestructura;

import java.math.BigDecimal;

/**
 * Clase que representa un cajero automatico del sistema bancario.
 * Gestiona el efectivo disponible, limites por transaccion, estado operativo
 * y permite dispensar efectivo, cargar dinero y cambiar su estado.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 */
public class Cajero {

    // ==================== ATRIBUTOS ====================
    
    /** Identificador unico del cajero. */
    private String idCajero;
    
    /** Ubicacion fisica del cajero. */
    private String ubicacionCajero;
    
    /** Cantidad de efectivo disponible actualmente en el cajero. */
    private BigDecimal efectivoDisponible;
    
    /** Monto maximo que se puede dispensar por transaccion. */
    private BigDecimal limitePorTransaccion;
    
    /** Indica si el cajero se encuentra operativo. */
    private boolean estaOperativo;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear un nuevo cajero automatico.
     * Inicializa el cajero con efectivo disponible de $10,000,000,
     * limite por transaccion de $2,000,000 y estado operativo verdadero.
     * 
     * @param idCajero Identificador unico del cajero
     * @param ubicacionCajero Ubicacion fisica del cajero
     */
    public Cajero(String idCajero, String ubicacionCajero) {
        this.idCajero = idCajero;
        this.ubicacionCajero = ubicacionCajero;
        this.efectivoDisponible = new BigDecimal("10000000");
        this.limitePorTransaccion = new BigDecimal("2000000");
        this.estaOperativo = true;
    }

    // ==================== METODOS DE OPERACION ====================
    
    /**
     * Dispensa efectivo del cajero si es posible.
     * Condiciones: cajero operativo, monto no mayor al efectivo disponible,
     * monto no mayor al limite por transaccion.
     * 
     * @param monto Monto de dinero a dispensar
     * @return true si se pudo dispensar el efectivo, false en caso contrario
     */
    public boolean DispensarEfectivo(BigDecimal monto) {
        if (!estaOperativo) return false;
        if (monto.compareTo(efectivoDisponible) > 0) return false;
        if (monto.compareTo(limitePorTransaccion) > 0) return false;
        
        this.efectivoDisponible = efectivoDisponible.subtract(monto);
        return true;
    }

    /**
     * Carga efectivo al cajero.
     * Incrementa la cantidad de efectivo disponible en el monto especificado.
     * 
     * @param monto Monto de efectivo a cargar
     */
    public void CargarEfectivo(BigDecimal monto) {
        this.efectivoDisponible = efectivoDisponible.add(monto);
    }

    /**
     * Cambia el estado operativo del cajero.
     * 
     * @param operativo true para poner el cajero operativo, false para desactivarlo
     */
    public void CambiarEstado(boolean operativo) {
        this.estaOperativo = operativo;
    }

    /**
     * Valida si el cajero tiene suficiente efectivo disponible.
     * 
     * @param monto Monto que se desea verificar
     * @return true si hay suficiente efectivo, false en caso contrario
     */
    public boolean validarDisponibilidadEfectivo(BigDecimal monto) {
        return efectivoDisponible.compareTo(monto) >= 0;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Identificador unico del cajero */
    public String getIdCajero() { return idCajero; }
    
    /** @param idCajero Nuevo identificador del cajero */
    public void setIdCajero(String idCajero) { this.idCajero = idCajero; }

    /** @return Ubicacion del cajero */
    public String getUbicacionCajero() { return ubicacionCajero; }
    
    /** @param ubicacionCajero Nueva ubicacion del cajero */
    public void setUbicacionCajero(String ubicacionCajero) { this.ubicacionCajero = ubicacionCajero; }

    /** @return Efectivo disponible en el cajero */
    public BigDecimal getEfectivoDisponible() { return efectivoDisponible; }
    
    /** @param efectivoDisponible Nueva cantidad de efectivo disponible */
    public void setEfectivoDisponible(BigDecimal efectivoDisponible) { this.efectivoDisponible = efectivoDisponible; }

    /** @return Limite maximo por transaccion */
    public BigDecimal getLimitePorTransaccion() { return limitePorTransaccion; }
    
    /** @param limitePorTransaccion Nuevo limite por transaccion */
    public void setLimitePorTransaccion(BigDecimal limitePorTransaccion) { this.limitePorTransaccion = limitePorTransaccion; }

    /** @return true si el cajero esta operativo, false en caso contrario */
    public boolean isEstaOperativo() { return estaOperativo; }
    
    /** @param estaOperativo Nuevo estado operativo */
    public void setEstaOperativo(boolean estaOperativo) { this.estaOperativo = estaOperativo; }
}//fin de la clase