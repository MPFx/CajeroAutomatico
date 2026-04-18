package model.transacciones;

import java.math.BigDecimal;

/**
 * Clase que representa una transaccion de transferencia bancaria.
 * Hereda de Transaccion y almacena informacion completa de la transferencia:
 * cuenta origen, cuenta destino, titular destino y una referencia unica.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Transaccion
 */
public class TransaccionTransferencia extends Transaccion {

    // ==================== ATRIBUTOS ====================
    
    /** Numero de cuenta de origen de la transferencia. */
    private String cuentaOrigen;
    
    /** Numero de cuenta de destino de la transferencia. */
    private String cuentaDestino;
    
    /** Nombre del titular de la cuenta destino. */
    private String titularDestino;
    
    /** Referencia unica de la transferencia (formato: REF-XXXXXXXXX). */
    private String referenciaTransferencia;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva transaccion de transferencia.
     * El tipo de transaccion se establece automaticamente como "TRANSFERENCIA".
     * Se genera automaticamente una referencia unica para la transferencia.
     * 
     * @param monto Monto de dinero a transferir
     * @param cuentaOrigen Numero de cuenta de origen
     * @param cuentaDestino Numero de cuenta de destino
     * @param titularDestino Nombre del titular de la cuenta destino
     */
    public TransaccionTransferencia(BigDecimal monto, String cuentaOrigen, 
                                   String cuentaDestino, String titularDestino) {
        super(monto, "TRANSFERENCIA");
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.titularDestino = titularDestino;
        this.referenciaTransferencia = generarReferencia();
    }

    // ==================== METODOS PRIVADOS ====================
    
    /**
     * Genera una referencia unica para la transferencia.
     * El formato es "REF-" seguido del timestamp actual en milisegundos.
     * 
     * @return Referencia unica de la transferencia
     */
    private String generarReferencia() {
        return "REF-" + System.currentTimeMillis();
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Numero de cuenta de origen */
    public String getCuentaOrigen() { return cuentaOrigen; }
    
    /** @param cuentaOrigen Nuevo numero de cuenta de origen */
    public void setCuentaOrigen(String cuentaOrigen) { this.cuentaOrigen = cuentaOrigen; }

    /** @return Numero de cuenta de destino */
    public String getCuentaDestino() { return cuentaDestino; }
    
    /** @param cuentaDestino Nuevo numero de cuenta de destino */
    public void setCuentaDestino(String cuentaDestino) { this.cuentaDestino = cuentaDestino; }

    /** @return Nombre del titular de la cuenta destino */
    public String getTitularDestino() { return titularDestino; }
    
    /** @param titularDestino Nuevo nombre del titular destino */
    public void setTitularDestino(String titularDestino) { this.titularDestino = titularDestino; }

    /** @return Referencia unica de la transferencia */
    public String getReferenciaTransferencia() { return referenciaTransferencia; }
    
    /** @param referenciaTransferencia Nueva referencia de transferencia */
    public void setReferenciaTransferencia(String referenciaTransferencia) { this.referenciaTransferencia = referenciaTransferencia; }
}//fin de la clase