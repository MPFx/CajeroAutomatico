package model.cuentas;

import java.math.BigDecimal;

/**
 * Clase que representa una cuenta corriente bancaria.
 * Hereda de CuentaBancaria y añade funcionalidades especificas como
 * comision de manejo, chequera y sobregiro autorizado.
 * Las cuentas corrientes no generan intereses.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 */
public class CuentaCorriente extends CuentaBancaria {

    // ==================== ATRIBUTOS ====================
    
    /** Saldo minimo que debe mantener la cuenta para evitar penalizaciones. */
    private BigDecimal saldoMinimoRequerido;
    
    /** Comision mensual que se cobra por el manejo de la cuenta. */
    private BigDecimal comisionManejo;
    
    /** Cantidad de cheques disponibles para usar en la cuenta. */
    private int chequesDisponibles;
    
    /** Monto maximo autorizado para sobregirar la cuenta. */
    private BigDecimal sobregiroAutorizado;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva cuenta corriente.
     * Inicializa la cuenta con saldo minimo requerido de $100,000,
     * comision de manejo de $15,000 mensual, 10 cheques disponibles
     * y sobregiro autorizado de $500,000.
     * 
     * @param numeroCuenta Numero unico identificador de la cuenta
     * @param nombreTitular Nombre completo del titular
     * @param documentoTitular Documento de identidad del titular
     */
    public CuentaCorriente(String numeroCuenta, String nombreTitular, String documentoTitular) {
        super(numeroCuenta, nombreTitular, documentoTitular);
        this.saldoMinimoRequerido = new BigDecimal("100000");
        this.comisionManejo = new BigDecimal("15000");
        this.chequesDisponibles = 10;
        this.sobregiroAutorizado = new BigDecimal("500000");
    }

    // ==================== METODOS ABSTRACTOS IMPLEMENTADOS ====================
    
    /**
     * Calcula los intereses generados por la cuenta corriente.
     * Las cuentas corrientes no generan intereses.
     * 
     * @return Siempre retorna cero
     */
    @Override
    public BigDecimal calcularIntereses() {
        return BigDecimal.ZERO;
    }

    /**
     * Valida si la cuenta corriente cumple con sus condiciones especiales.
     * La condicion especial es que el saldo disponible sea suficiente
     * para cubrir la comision de manejo.
     * 
     * @return true si el saldo es mayor o igual a la comision de manejo, false en caso contrario
     */
    @Override
    public boolean validarCondicionesEspeciales() {
        return saldoDisponible.compareTo(comisionManejo) >= 0;
    }

    // ==================== METODOS ESPECIFICOS DE CUENTA CORRIENTE ====================
    
    /**
     * Cobra la comision de manejo mensual de la cuenta.
     * Si hay saldo suficiente, se descuenta directamente.
     * Si no hay saldo suficiente, se utiliza el sobregiro autorizado.
     * Si el faltante no supera el sobregiro, se descuenta del sobregiro
     * y el saldo queda en cero.
     */
    public void cobrarComisionManejo() {
        if (saldoDisponible.compareTo(comisionManejo) >= 0) {
            this.saldoDisponible = saldoDisponible.subtract(comisionManejo);
        } else {
            BigDecimal faltante = comisionManejo.subtract(saldoDisponible);
            if (faltante.compareTo(sobregiroAutorizado) <= 0) {
                this.saldoDisponible = BigDecimal.ZERO;
                this.sobregiroAutorizado = sobregiroAutorizado.subtract(faltante);
            }
        }
    }

    /**
     * Utiliza un cheque de la cuenta.
     * Disminuye en uno la cantidad de cheques disponibles.
     * Solo funciona si hay cheques disponibles.
     */
    public void usarCheque() {
        if (chequesDisponibles > 0) {
            this.chequesDisponibles--;
        }
    }

    /**
     * Verifica si se puede realizar un retiro por el monto especificado.
     * Sobrescribe el metodo de la clase padre.
     * En cuenta corriente, se puede retirar hasta el saldo disponible mas el sobregiro autorizado.
     * 
     * @param monto Monto que se desea retirar
     * @return true si el monto no supera el saldo mas el sobregiro, false en caso contrario
     */
    @Override
    public boolean puedeRetirar(BigDecimal monto) {
        BigDecimal disponibleConSobregiro = saldoDisponible.add(sobregiroAutorizado);
        return disponibleConSobregiro.compareTo(monto) >= 0;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Saldo minimo requerido para la cuenta */
    public BigDecimal getSaldoMinimoRequerido() { return saldoMinimoRequerido; }
    
    /** @param saldoMinimoRequerido Nuevo saldo minimo requerido */
    public void setSaldoMinimoRequerido(BigDecimal saldoMinimoRequerido) { this.saldoMinimoRequerido = saldoMinimoRequerido; }

    /** @return Comision de manejo mensual */
    public BigDecimal getComisionManejo() { return comisionManejo; }
    
    /** @param comisionManejo Nueva comision de manejo */
    public void setComisionManejo(BigDecimal comisionManejo) { this.comisionManejo = comisionManejo; }

    /** @return Cantidad de cheques disponibles */
    public int getChequesDisponibles() { return chequesDisponibles; }
    
    /** @param chequesDisponibles Nueva cantidad de cheques disponibles */
    public void setChequesDisponibles(int chequesDisponibles) { this.chequesDisponibles = chequesDisponibles; }

    /** @return Monto de sobregiro autorizado */
    public BigDecimal getSobregiroAutorizado() { return sobregiroAutorizado; }
    
    /** @param sobregiroAutorizado Nuevo monto de sobregiro autorizado */
    public void setSobregiroAutorizado(BigDecimal sobregiroAutorizado) { this.sobregiroAutorizado = sobregiroAutorizado; }
}//fin de la clase