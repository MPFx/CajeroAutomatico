package model.cuentas;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Clase que representa una cuenta bancaria para jovenes.
 * Hereda de CuentaBancaria y ofrece beneficios especiales como tasa preferencial
 * del 2% mensual y beneficios por tiempo limitado (5 años).
 * Los cupos diarios son menores: $500,000 para retiros y $1,000,000 para transferencias.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 */
public class CuentaJuventud extends CuentaBancaria {

    // ==================== ATRIBUTOS ====================
    
    /** Fecha de nacimiento del titular para validar edad. */
    private LocalDate fechaNacimientoTitular;
    
    /** Fecha hasta la cual estan vigentes los beneficios especiales. */
    private LocalDateTime fechaFinBeneficio;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva cuenta de juventud.
     * Inicializa la cuenta con fecha de nacimiento del titular,
     * beneficios vigentes por 5 años, cupo diario de retiro de $500,000
     * y cupo diario de transferencia de $1,000,000.
     * 
     * @param numeroCuenta Numero unico identificador de la cuenta
     * @param nombreTitular Nombre completo del titular
     * @param documentoTitular Documento de identidad del titular
     * @param fechaNacimiento Fecha de nacimiento del titular
     */
    public CuentaJuventud(String numeroCuenta, String nombreTitular, 
                          String documentoTitular, LocalDate fechaNacimiento) {
        super(numeroCuenta, nombreTitular, documentoTitular);
        this.fechaNacimientoTitular = fechaNacimiento;
        this.fechaFinBeneficio = LocalDateTime.now().plusYears(5);
        this.cupoDiarioRetiro = new BigDecimal("500000");
        this.cupoDiarioTransferencia = new BigDecimal("1000000");
    }

    // ==================== METODOS ABSTRACTOS IMPLEMENTADOS ====================
    
    /**
     * Calcula los intereses generados por la cuenta de juventud.
     * Beneficio: tasa preferencial del 2% mensual para jovenes.
     * 
     * @return Intereses calculados como saldo x 0.02
     */
    @Override
    public BigDecimal calcularIntereses() {
        return saldoDisponible.multiply(new BigDecimal("0.02"));
    }

    /**
     * Valida si la cuenta de juventud cumple con sus condiciones especiales.
     * La condicion especial es que el titular sea menor o igual a 25 años.
     * 
     * @return true si el titular tiene 25 años o menos, false en caso contrario
     */
    @Override
    public boolean validarCondicionesEspeciales() {
        int edad = calcularEdad();
        return edad <= 25;
    }

    // ==================== METODOS ESPECIFICOS DE CUENTA JUVENTUD ====================
    
    /**
     * Calcula la edad actual del titular basada en su fecha de nacimiento.
     * 
     * @return Edad en años cumplidos
     */
    public int calcularEdad() {
        return Period.between(fechaNacimientoTitular, LocalDate.now()).getYears();
    }

    /**
     * Verifica si los beneficios de la cuenta de juventud estan activos.
     * Los beneficios estan activos si la fecha actual es anterior a la fecha de fin
     * y el titular cumple con la condicion de edad (menor o igual a 25 años).
     * 
     * @return true si los beneficios estan activos, false en caso contrario
     */
    public boolean beneficioActivo() {
        return LocalDateTime.now().isBefore(fechaFinBeneficio) && validarCondicionesEspeciales();
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Fecha de nacimiento del titular */
    public LocalDate getFechaNacimientoTitular() { return fechaNacimientoTitular; }
    
    /** @param fechaNacimientoTitular Nueva fecha de nacimiento del titular */
    public void setFechaNacimientoTitular(LocalDate fechaNacimientoTitular) { this.fechaNacimientoTitular = fechaNacimientoTitular; }

    /** @return Fecha de fin de los beneficios especiales */
    public LocalDateTime getFechaFinBeneficio() { return fechaFinBeneficio; }
    
    /** @param fechaFinBeneficio Nueva fecha de fin de beneficios */
    public void setFechaFinBeneficio(LocalDateTime fechaFinBeneficio) { this.fechaFinBeneficio = fechaFinBeneficio; }
}//fin de la clase