package model.transacciones;

import java.math.BigDecimal;

/**
 * Clase que representa una transaccion de retiro bancario.
 * Hereda de Transaccion y especifica el origen desde donde se realiza el retiro.
 * Tipos de origen: Cajero, Ventanilla, App.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Transaccion
 */
public class TransaccionRetiro extends Transaccion {

    // ==================== ATRIBUTOS ====================
    
    /** Origen del retiro (Cajero, Ventanilla, App). */
    private String origenRetiro;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva transaccion de retiro.
     * El tipo de transaccion se establece automaticamente como "RETIRO".
     * 
     * @param monto Monto de dinero a retirar
     * @param origenRetiro Origen del retiro (Cajero, Ventanilla, App)
     */
    public TransaccionRetiro(BigDecimal monto, String origenRetiro) {
        super(monto, "RETIRO");
        this.origenRetiro = origenRetiro;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Origen del retiro */
    public String getOrigenRetiro() { return origenRetiro; }
    
    /** @param origenRetiro Nuevo origen del retiro */
    public void setOrigenRetiro(String origenRetiro) { this.origenRetiro = origenRetiro; }
}//fin de la clase