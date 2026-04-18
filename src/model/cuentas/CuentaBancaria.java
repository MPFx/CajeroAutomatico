package model.cuentas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que representa una cuenta bancaria generica.
 * Contiene los atributos y comportamientos comunes a todos los tipos de cuentas.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaAhorro
 * @see CuentaCorriente
 * @see CuentaJuventud
 * @see CuentaTerceraEdad
 */
public abstract class CuentaBancaria {

    // ==================== ATRIBUTOS ====================
    
    /** Numero unico identificador de la cuenta bancaria. */
    protected String numeroCuenta;
    
    /** Hash de la contrasena para autenticacion del titular. */
    protected String contrasenaHash;
    
    /** Nombre completo del titular de la cuenta. */
    protected String nombreTitular;
    
    /** Documento de identidad (cedula, NIT, pasaporte) del titular. */
    protected String documentoTitular;
    
    /** Saldo actual disponible en la cuenta. */
    protected BigDecimal saldoDisponible;
    
    /** Fecha y hora de apertura de la cuenta. */
    protected LocalDateTime fechaApertura;
    
    /** Fecha y hora del ultimo acceso exitoso a la cuenta. */
    protected LocalDateTime fechaUltimoAcceso;
    
    /** Numero de intentos fallidos consecutivos de autenticacion. */
    protected int intentosFallidos;
    
    /** Indica si la cuenta esta bloqueada por seguridad. */
    protected boolean estaBloqueada;
    
    /** Tasa de interes mensual aplicable (ej: 0.01 = 1%). */
    protected BigDecimal tasaInteresMensual;
    
    /** Limite maximo de dinero que se puede retirar por dia. */
    protected BigDecimal cupoDiarioRetiro;
    
    /** Limite maximo de dinero que se puede transferir por dia. */
    protected BigDecimal cupoDiarioTransferencia;
    
    /** Monto total retirado en el dia actual. */
    protected BigDecimal montoRetiradoHoy;
    
    /** Monto total transferido en el dia actual. */
    protected BigDecimal montoTransferidoHoy;
    
    /** Lista de numeros de cuenta favoritas para transferencias rapidas. */
    protected List<String> cuentasFavoritas;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear una nueva cuenta bancaria con los datos basicos del titular.
     * Inicializa la cuenta con saldo en cero, fecha actual, sin bloqueo,
     * tasa de interes del 1%, cupo diario de retiro de $1,000,000,
     * cupo diario de transferencia de $2,000,000, montos del dia en cero
     * y lista de favoritas vacia.
     * 
     * @param numeroCuenta Numero unico identificador de la cuenta
     * @param nombreTitular Nombre completo del titular
     * @param documentoTitular Documento de identidad del titular
     */
    public CuentaBancaria(String numeroCuenta, String nombreTitular, String documentoTitular) {
        this.numeroCuenta = numeroCuenta;
        this.nombreTitular = nombreTitular;
        this.documentoTitular = documentoTitular;
        this.saldoDisponible = BigDecimal.ZERO;
        this.fechaApertura = LocalDateTime.now();
        this.fechaUltimoAcceso = LocalDateTime.now();
        this.intentosFallidos = 0;
        this.estaBloqueada = false;
        this.tasaInteresMensual = new BigDecimal("0.01");
        this.cupoDiarioRetiro = new BigDecimal("1000000");
        this.cupoDiarioTransferencia = new BigDecimal("2000000");
        this.montoRetiradoHoy = BigDecimal.ZERO;
        this.montoTransferidoHoy = BigDecimal.ZERO;
        this.cuentasFavoritas = new ArrayList<>();
    }

    // ==================== METODOS ABSTRACTOS ====================
    
    /**
     * Calcula los intereses generados por la cuenta segun su tipo especifico.
     * Cada subclase debe implementar su propia logica de calculo.
     * 
     * @return Intereses calculados como valor BigDecimal
     */
    public abstract BigDecimal calcularIntereses();

    /**
     * Valida si la cuenta cumple con las condiciones especiales de su tipo.
     * Ejemplos: CuentaJuventud requiere titular menor de 25 años,
     * CuentaTerceraEdad requiere titular mayor de 60 años.
     * 
     * @return true si cumple todas las condiciones especiales, false en caso contrario
     */
    public abstract boolean validarCondicionesEspeciales();

    // ==================== METODOS DE AUTENTICACION Y SEGURIDAD ====================
    
    /**
     * Valida si la contrasena ingresada coincide con el hash almacenado.
     * 
     * @param contrasena Contrasena en texto plano ingresada por el usuario
     * @return true si la contrasena es correcta, false en caso contrario
     */
    public boolean validarContrasena(String contrasena) {
        return this.contrasenaHash.equals(contrasena);
    }

    /**
     * Registra un acceso exitoso a la cuenta.
     * Actualiza la fecha y hora del ultimo acceso y reinicia el contador
     * de intentos fallidos a cero.
     */
    public void registrarAcceso() {
        this.fechaUltimoAcceso = LocalDateTime.now();
        this.intentosFallidos = 0;
    }

    /**
     * Registra un intento fallido de autenticacion.
     * Incrementa el contador de intentos fallidos. Si alcanza 3 intentos
     * fallidos consecutivos, la cuenta se bloquea automaticamente.
     */
    public void registrarIntentoFallido() {
        this.intentosFallidos++;
        if (this.intentosFallidos >= 3) {
            this.estaBloqueada = true;
        }
    }

    // ==================== METODOS DE VALIDACION DE OPERACIONES ====================
    
    /**
     * Verifica si se puede realizar un retiro por el monto especificado.
     * Condiciones: cuenta no bloqueada, saldo suficiente, no exceder cupo diario.
     * 
     * @param monto Monto que se desea retirar
     * @return true si el retiro es posible, false en caso contrario
     */
    public boolean puedeRetirar(BigDecimal monto) {
        if (estaBloqueada) return false;
        if (saldoDisponible.compareTo(monto) < 0) return false;
        
        BigDecimal nuevoTotalRetiros = montoRetiradoHoy.add(monto);
        return nuevoTotalRetiros.compareTo(cupoDiarioRetiro) <= 0;
    }

    /**
     * Verifica si se puede realizar una transferencia por el monto especificado.
     * Condiciones: cuenta no bloqueada, saldo suficiente, no exceder cupo diario.
     * 
     * @param monto Monto que se desea transferir
     * @return true si la transferencia es posible, false en caso contrario
     */
    public boolean puedeTransferir(BigDecimal monto) {
        if (estaBloqueada) return false;
        if (saldoDisponible.compareTo(monto) < 0) return false;
        
        BigDecimal nuevoTotalTransferencias = montoTransferidoHoy.add(monto);
        return nuevoTotalTransferencias.compareTo(cupoDiarioTransferencia) <= 0;
    }

    // ==================== METODOS DE OPERACIONES FINANCIERAS ====================
    
    /**
     * Ejecuta un retiro de dinero desde la cuenta.
     * La operacion solo se realiza si puedeRetirar retorna true.
     * Actualiza el saldo disponible y acumula el monto retirado en el contador diario.
     * 
     * @param monto Monto a retirar
     */
    public void retirar(BigDecimal monto) {
        if (puedeRetirar(monto)) {
            this.saldoDisponible = saldoDisponible.subtract(monto);
            this.montoRetiradoHoy = montoRetiradoHoy.add(monto);
        }
    }

    /**
     * Ejecuta un deposito de dinero en la cuenta.
     * Incrementa el saldo disponible en el monto especificado.
     * Esta operacion no tiene restricciones de bloqueo ni limites diarios.
     * 
     * @param monto Monto a depositar
     */
    public void depositar(BigDecimal monto) {
        this.saldoDisponible = saldoDisponible.add(monto);
    }

    /**
     * Ejecuta una transferencia de dinero desde esta cuenta hacia otra.
     * La operacion solo se realiza si puedeTransferir retorna true.
     * Actualiza el saldo disponible y acumula el monto transferido en el contador diario.
     * Nota: Este metodo solo descuenta el monto de la cuenta origen.
     * El abono a la cuenta destino debe manejarse por separado.
     * 
     * @param monto Monto a transferir
     */
    public void transferir(BigDecimal monto) {
        if (puedeTransferir(monto)) {
            this.saldoDisponible = saldoDisponible.subtract(monto);
            this.montoTransferidoHoy = montoTransferidoHoy.add(monto);
        }
    }

    // ==================== METODOS DE GESTION DE FAVORITOS ====================
    
    /**
     * Agrega una cuenta a la lista de cuentas favoritas del titular.
     * No permite duplicados: si la cuenta ya existe en la lista, no se agrega nuevamente.
     * 
     * @param numeroCuenta Numero de la cuenta que se desea marcar como favorita
     */
    public void agregarFavorita(String numeroCuenta) {
        if (!cuentasFavoritas.contains(numeroCuenta)) {
            cuentasFavoritas.add(numeroCuenta);
        }
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Numero de cuenta */
    public String getNumeroCuenta() { return numeroCuenta; }
    
    /** @param numeroCuenta Nuevo numero de cuenta */
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    /** @return Hash de la contrasena almacenado */
    public String getContrasenaHash() { return contrasenaHash; }
    
    /** @param contrasenaHash Nuevo hash de contrasena */
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }

    /** @return Nombre completo del titular */
    public String getNombreTitular() { return nombreTitular; }
    
    /** @param nombreTitular Nuevo nombre del titular */
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }

    /** @return Documento de identidad del titular */
    public String getDocumentoTitular() { return documentoTitular; }
    
    /** @param documentoTitular Nuevo documento del titular */
    public void setDocumentoTitular(String documentoTitular) { this.documentoTitular = documentoTitular; }

    /** @return Saldo actual de la cuenta */
    public BigDecimal getSaldoDisponible() { return saldoDisponible; }
    
    /** @param saldoDisponible Nuevo saldo */
    public void setSaldoDisponible(BigDecimal saldoDisponible) { this.saldoDisponible = saldoDisponible; }

    /** @return Fecha y hora de apertura */
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    
    /** @param fechaApertura Nueva fecha de apertura */
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }

    /** @return Fecha y hora del ultimo acceso */
    public LocalDateTime getFechaUltimoAcceso() { return fechaUltimoAcceso; }
    
    /** @param fechaUltimoAcceso Nueva fecha de ultimo acceso */
    public void setFechaUltimoAcceso(LocalDateTime fechaUltimoAcceso) { this.fechaUltimoAcceso = fechaUltimoAcceso; }

    /** @return Numero de intentos fallidos consecutivos */
    public int getIntentosFallidos() { return intentosFallidos; }
    
    /** @param intentosFallidos Nueva cantidad de intentos fallidos */
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    /** @return true si esta bloqueada, false si esta activa */
    public boolean isEstaBloqueada() { return estaBloqueada; }
    
    /** @param estaBloqueada true para bloquear, false para desbloquear */
    public void setEstaBloqueada(boolean estaBloqueada) { this.estaBloqueada = estaBloqueada; }

    /** @return Tasa de interes mensual (ej: 0.01 = 1%) */
    public BigDecimal getTasaInteresMensual() { return tasaInteresMensual; }
    
    /** @param tasaInteresMensual Nueva tasa de interes */
    public void setTasaInteresMensual(BigDecimal tasaInteresMensual) { this.tasaInteresMensual = tasaInteresMensual; }

    /** @return Limite maximo de retiro por dia */
    public BigDecimal getCupoDiarioRetiro() { return cupoDiarioRetiro; }
    
    /** @param cupoDiarioRetiro Nuevo limite de retiro diario */
    public void setCupoDiarioRetiro(BigDecimal cupoDiarioRetiro) { this.cupoDiarioRetiro = cupoDiarioRetiro; }

    /** @return Limite maximo de transferencia por dia */
    public BigDecimal getCupoDiarioTransferencia() { return cupoDiarioTransferencia; }
    
    /** @param cupoDiarioTransferencia Nuevo limite de transferencia diaria */
    public void setCupoDiarioTransferencia(BigDecimal cupoDiarioTransferencia) { this.cupoDiarioTransferencia = cupoDiarioTransferencia; }

    /** @return Monto total retirado en el dia actual */
    public BigDecimal getMontoRetiradoHoy() { return montoRetiradoHoy; }
    
    /** @param montoRetiradoHoy Nuevo total de retiros del dia */
    public void setMontoRetiradoHoy(BigDecimal montoRetiradoHoy) { this.montoRetiradoHoy = montoRetiradoHoy; }

    /** @return Monto total transferido en el dia actual */
    public BigDecimal getMontoTransferidoHoy() { return montoTransferidoHoy; }
    
    /** @param montoTransferidoHoy Nuevo total de transferencias del dia */
    public void setMontoTransferidoHoy(BigDecimal montoTransferidoHoy) { this.montoTransferidoHoy = montoTransferidoHoy; }

    /** @return Lista de cuentas favoritas */
    public List<String> getCuentasFavoritas() { return cuentasFavoritas; }
    
    /** @param cuentasFavoritas Nueva lista de cuentas favoritas */
    public void setCuentasFavoritas(List<String> cuentasFavoritas) { this.cuentasFavoritas = cuentasFavoritas; }

    // ==================== SOBREESCRITURAS ====================
    
    /**
     * Devuelve una representacion textual resumida de la cuenta bancaria.
     * Formato: "Cuenta: [numero] | Titular: [nombre] | Saldo: $[saldo]"
     * 
     * @return Cadena con la informacion principal de la cuenta
     */
    @Override
    public String toString() {
        return "Cuenta: " + numeroCuenta + " | Titular: " + nombreTitular + 
               " | Saldo: $" + saldoDisponible;
    }
}//fin de la clase