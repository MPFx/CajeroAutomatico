package model.cuentas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Clase que representa una cuenta de ahorro bancaria.
 * Hereda de CuentaBancaria y añade funcionalidades especificas como
 * acumulacion de intereses, saldo minimo requerido y control de inactividad.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 */
public class CuentaAhorro extends CuentaBancaria {

    // ==================== ATRIBUTOS ====================
    
    /** Intereses acumulados generados por la cuenta de ahorro. */
    private BigDecimal interesAcumulado;
    
    /** Cantidad de meses sin movimiento (sin depositos, retiros o transferencias). */
    private int mesesSinMovimiento;
    
    /** Saldo minimo que debe mantener la cuenta para evitar penalizaciones. */
    private BigDecimal saldoMinimoRequerido;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva cuenta de ahorro.
     * Inicializa la cuenta con interes acumulado en cero, cero meses sin movimiento,
     * saldo minimo requerido de $50,000 y tasa de interes del 1.5% mensual.
     * 
     * @param numeroCuenta Numero unico identificador de la cuenta
     * @param nombreTitular Nombre completo del titular
     * @param documentoTitular Documento de identidad del titular
     */
    public CuentaAhorro(String numeroCuenta, String nombreTitular, String documentoTitular) {
        super(numeroCuenta, nombreTitular, documentoTitular);
        this.interesAcumulado = BigDecimal.ZERO;
        this.mesesSinMovimiento = 0;
        this.saldoMinimoRequerido = new BigDecimal("50000");
        this.tasaInteresMensual = new BigDecimal("0.015");
    }

    // ==================== METODOS ABSTRACTOS IMPLEMENTADOS ====================
    
    /**
     * Calcula los intereses generados por la cuenta de ahorro en el periodo actual.
     * El calculo se realiza multiplicando el saldo disponible por la tasa de interes mensual.
     * El interes generado se acumula en interesAcumulado.
     * 
     * @return Intereses calculados del periodo
     */
    @Override
    public BigDecimal calcularIntereses() {
        BigDecimal intereses = saldoDisponible.multiply(tasaInteresMensual);
        this.interesAcumulado = interesAcumulado.add(intereses);
        return intereses;
    }

    /**
     * Valida si la cuenta de ahorro cumple con sus condiciones especiales.
     * La condicion especial es que el saldo disponible no sea inferior al saldo minimo requerido.
     * 
     * @return true si el saldo es mayor o igual al saldo minimo requerido, false en caso contrario
     */
    @Override
    public boolean validarCondicionesEspeciales() {
        return saldoDisponible.compareTo(saldoMinimoRequerido) >= 0;
    }

    // ==================== METODOS ESPECIFICOS DE CUENTA AHORRO ====================
    
    /**
     * Actualiza la cantidad de meses sin movimiento en la cuenta.
     * Calcula los meses transcurridos desde la fecha del ultimo acceso hasta la fecha actual.
     */
    public void actualizarMesesSinMovimiento() {
        long meses = ChronoUnit.MONTHS.between(fechaUltimoAcceso, LocalDateTime.now());
        this.mesesSinMovimiento = (int) meses;
    }

    /**
     * Verifica si la cuenta de ahorro esta inactiva.
     * Una cuenta se considera inactiva cuando lleva 6 meses o mas sin movimientos.
     * 
     * @return true si tiene 6 meses o mas sin movimiento, false en caso contrario
     */
    public boolean tieneInactividad() {
        return mesesSinMovimiento >= 6;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Intereses acumulados de la cuenta */
    public BigDecimal getInteresAcumulado() { return interesAcumulado; }
    
    /** @param interesAcumulado Nuevos intereses acumulados */
    public void setInteresAcumulado(BigDecimal interesAcumulado) { this.interesAcumulado = interesAcumulado; }

    /** @return Cantidad de meses sin movimiento */
    public int getMesesSinMovimiento() { return mesesSinMovimiento; }
    
    /** @param mesesSinMovimiento Nueva cantidad de meses sin movimiento */
    public void setMesesSinMovimiento(int mesesSinMovimiento) { this.mesesSinMovimiento = mesesSinMovimiento; }

    /** @return Saldo minimo requerido para la cuenta */
    public BigDecimal getSaldoMinimoRequerido() { return saldoMinimoRequerido; }
    
    /** @param saldoMinimoRequerido Nuevo saldo minimo requerido */
    public void setSaldoMinimoRequerido(BigDecimal saldoMinimoRequerido) { this.saldoMinimoRequerido = saldoMinimoRequerido; }
}//fin de la clase