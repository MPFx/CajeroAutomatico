package model.transacciones;

import java.math.BigDecimal;

/**
 * Clase que representa una transaccion de deposito bancario.
 * Hereda de Transaccion y especifica el medio a traves del cual se realiza el deposito.
 * Tipos de medio: Efectivo, Cheque, Transferencia.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Transaccion
 */
public class TransaccionDeposito extends Transaccion {

    // ==================== ATRIBUTOS ====================
    
    /** Medio utilizado para realizar el deposito (Efectivo, Cheque, Transferencia). */
    private String medioDeposito;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva transaccion de deposito.
     * El tipo de transaccion se establece automaticamente como "DEPOSITO".
     * 
     * @param monto Monto de dinero a depositar
     * @param medioDeposito Medio utilizado para el deposito (Efectivo, Cheque, Transferencia)
     */
    public TransaccionDeposito(BigDecimal monto, String medioDeposito) {
        super(monto, "DEPOSITO");
        this.medioDeposito = medioDeposito;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Medio utilizado para el deposito */
    public String getMedioDeposito() { return medioDeposito; }
    
    /** @param medioDeposito Nuevo medio de deposito */
    public void setMedioDeposito(String medioDeposito) { this.medioDeposito = medioDeposito; }
}//fin de la clase