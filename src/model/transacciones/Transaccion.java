package model.transacciones;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase abstracta que representa una transaccion bancaria generica.
 * Contiene los atributos y comportamientos comunes a todos los tipos de transacciones
 * (retiros, depositos, transferencias). Gestiona el estado de la transaccion,
 * su identificador unico y el saldo posterior a la operacion.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see TransaccionRetiro
 * @see TransaccionDeposito
 * @see TransaccionTransferencia
 */
public abstract class Transaccion {

    // ==================== ATRIBUTOS ====================
    
    /** Identificador unico de la transaccion (formato: TXN-XXXXXXXX). */
    protected String idTransaccion;
    
    /** Fecha y hora en que se realizo la transaccion. */
    protected LocalDateTime fechaHora;
    
    /** Monto de dinero involucrado en la transaccion. */
    protected BigDecimal monto;
    
    /** Tipo de transaccion (RETIRO, DEPOSITO, TRANSFERENCIA). */
    protected String tipoTransaccion;
    
    /** Estado actual de la transaccion (PENDIENTE, COMPLETADA, FALLIDA). */
    protected String estadoTransaccion;
    
    /** Saldo de la cuenta despues de ejecutar la transaccion. */
    protected BigDecimal saldoPosterior;
    
    /** Descripcion o motivo adicional de la transaccion. */
    protected String descripcion;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva transaccion.
     * Inicializa la transaccion con un identificador unico,
     * fecha y hora actual, monto especificado, tipo de transaccion
     * y estado inicial "PENDIENTE".
     * 
     * @param monto Monto de dinero involucrado en la transaccion
     * @param tipoTransaccion Tipo de transaccion (RETIRO, DEPOSITO, TRANSFERENCIA)
     */
    public Transaccion(BigDecimal monto, String tipoTransaccion) {
        this.idTransaccion = generarIdUnico();
        this.fechaHora = LocalDateTime.now();
        this.monto = monto;
        this.tipoTransaccion = tipoTransaccion;
        this.estadoTransaccion = "PENDIENTE";
    }

    // ==================== METODOS PRIVADOS ====================
    
    /**
     * Genera un identificador unico para la transaccion.
     * El formato es "TXN-" seguido de 8 caracteres alfanumericos aleatorios.
     * 
     * @return Identificador unico de la transaccion
     */
    private String generarIdUnico() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // ==================== METODOS DE GESTION DE ESTADO ====================
    
    /**
     * Marca la transaccion como completada exitosamente.
     * Actualiza el estado a "COMPLETADA" y registra el saldo final de la cuenta.
     * 
     * @param saldoFinal Saldo de la cuenta despues de ejecutar la transaccion
     */
    public void completarTransaccion(BigDecimal saldoFinal) {
        this.estadoTransaccion = "COMPLETADA";
        this.saldoPosterior = saldoFinal;
    }

    /**
     * Marca la transaccion como fallida.
     * Actualiza el estado a "FALLIDA" y registra el motivo del fallo en la descripcion.
     * 
     * @param motivo Razon por la cual la transaccion fallo
     */
    public void fallarTransaccion(String motivo) {
        this.estadoTransaccion = "FALLIDA";
        this.descripcion = motivo;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Identificador unico de la transaccion */
    public String getIdTransaccion() { return idTransaccion; }
    
    /** @param idTransaccion Nuevo identificador de la transaccion */
    public void setIdTransaccion(String idTransaccion) { this.idTransaccion = idTransaccion; }

    /** @return Fecha y hora de la transaccion */
    public LocalDateTime getFechaHora() { return fechaHora; }
    
    /** @param fechaHora Nueva fecha y hora de la transaccion */
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    /** @return Monto de la transaccion */
    public BigDecimal getMonto() { return monto; }
    
    /** @param monto Nuevo monto de la transaccion */
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    /** @return Tipo de transaccion (RETIRO, DEPOSITO, TRANSFERENCIA) */
    public String getTipoTransaccion() { return tipoTransaccion; }
    
    /** @param tipoTransaccion Nuevo tipo de transaccion */
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    /** @return Estado actual de la transaccion (PENDIENTE, COMPLETADA, FALLIDA) */
    public String getEstadoTransaccion() { return estadoTransaccion; }
    
    /** @param estadoTransaccion Nuevo estado de la transaccion */
    public void setEstadoTransaccion(String estadoTransaccion) { this.estadoTransaccion = estadoTransaccion; }

    /** @return Saldo posterior a la transaccion */
    public BigDecimal getSaldoPosterior() { return saldoPosterior; }
    
    /** @param saldoPosterior Nuevo saldo posterior */
    public void setSaldoPosterior(BigDecimal saldoPosterior) { this.saldoPosterior = saldoPosterior; }

    /** @return Descripcion o motivo de la transaccion */
    public String getDescripcion() { return descripcion; }
    
    /** @param descripcion Nueva descripcion de la transaccion */
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // ==================== SOBREESCRITURAS ====================
    
    /**
     * Devuelve una representacion textual resumida de la transaccion.
     * Formato: "fechaHora | tipo | $monto | estado"
     * 
     * @return Cadena con la informacion principal de la transaccion
     */
    @Override
    public String toString() {
        return fechaHora + " | " + tipoTransaccion + " | $" + monto + " | " + estadoTransaccion;
    }
}//fin de la clase