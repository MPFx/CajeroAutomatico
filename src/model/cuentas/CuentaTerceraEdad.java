package model.cuentas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

/**
 * Clase que representa una cuenta bancaria para adultos mayores (tercera edad).
 * Hereda de CuentaBancaria y ofrece beneficios especiales como tasa preferencial
 * del 2.5% mensual, 50% de descuento en comisiones y sin costo de mantenimiento.
 * Requiere que el titular tenga 60 años o mas.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 */
public class CuentaTerceraEdad extends CuentaBancaria {

    // ==================== ATRIBUTOS ====================
    
    /** Fecha de nacimiento del titular para validar edad. */
    private LocalDate fechaNacimientoTitular;
    
    /** Descripcion del beneficio exclusivo para adultos mayores. */
    private String beneficioExclusivo;
    
    /** Porcentaje de descuento en comisiones (ej: 0.5 = 50% de descuento). */
    private BigDecimal descuentoComisiones;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva cuenta de tercera edad.
     * Inicializa la cuenta con fecha de nacimiento del titular,
     * beneficio exclusivo "Sin costo de mantenimiento",
     * descuento del 50% en comisiones y tasa de interes del 2.5% mensual.
     * 
     * @param numeroCuenta Numero unico identificador de la cuenta
     * @param nombreTitular Nombre completo del titular
     * @param documentoTitular Documento de identidad del titular
     * @param fechaNacimiento Fecha de nacimiento del titular
     */
    public CuentaTerceraEdad(String numeroCuenta, String nombreTitular, 
                             String documentoTitular, LocalDate fechaNacimiento) {
        super(numeroCuenta, nombreTitular, documentoTitular);
        this.fechaNacimientoTitular = fechaNacimiento;
        this.beneficioExclusivo = "Sin costo de mantenimiento";
        this.descuentoComisiones = new BigDecimal("0.5");
        this.tasaInteresMensual = new BigDecimal("0.025");
    }

    // ==================== METODOS ABSTRACTOS IMPLEMENTADOS ====================
    
    /**
     * Calcula los intereses generados por la cuenta de tercera edad.
     * Beneficio: tasa preferencial del 2.5% mensual (mayor que cuentas regulares).
     * 
     * @return Intereses calculados como saldo x 0.025
     */
    @Override
    public BigDecimal calcularIntereses() {
        return saldoDisponible.multiply(tasaInteresMensual);
    }

    /**
     * Valida si la cuenta de tercera edad cumple con sus condiciones especiales.
     * La condicion especial es que el titular tenga 60 años o mas.
     * 
     * @return true si el titular tiene 60 años o mas, false en caso contrario
     */
    @Override
    public boolean validarCondicionesEspeciales() {
        int edad = calcularEdad();
        return edad >= 60;
    }

    // ==================== METODOS ESPECIFICOS DE CUENTA TERCERA EDAD ====================
    
    /**
     * Calcula la edad actual del titular basada en su fecha de nacimiento.
     * 
     * @return Edad en años cumplidos
     */
    public int calcularEdad() {
        return Period.between(fechaNacimientoTitular, LocalDate.now()).getYears();
    }

    /**
     * Aplica el descuento de comisiones a una comision original.
     * El descuento es del 50% (factor 0.5).
     * 
     * @param comisionOriginal Monto original de la comision
     * @return Monto de la comision con el descuento aplicado
     */
    public BigDecimal aplicarDescuentoComision(BigDecimal comisionOriginal) {
        return comisionOriginal.multiply(BigDecimal.ONE.subtract(descuentoComisiones));
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Fecha de nacimiento del titular */
    public LocalDate getFechaNacimientoTitular() { return fechaNacimientoTitular; }
    
    /** @param fechaNacimientoTitular Nueva fecha de nacimiento del titular */
    public void setFechaNacimientoTitular(LocalDate fechaNacimientoTitular) { this.fechaNacimientoTitular = fechaNacimientoTitular; }

    /** @return Descripcion del beneficio exclusivo */
    public String getBeneficioExclusivo() { return beneficioExclusivo; }
    
    /** @param beneficioExclusivo Nueva descripcion del beneficio exclusivo */
    public void setBeneficioExclusivo(String beneficioExclusivo) { this.beneficioExclusivo = beneficioExclusivo; }

    /** @return Porcentaje de descuento en comisiones (0.5 = 50%) */
    public BigDecimal getDescuentoComisiones() { return descuentoComisiones; }
    
    /** @param descuentoComisiones Nuevo porcentaje de descuento */
    public void setDescuentoComisiones(BigDecimal descuentoComisiones) { this.descuentoComisiones = descuentoComisiones; }
}//fin de la clase