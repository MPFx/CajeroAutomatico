package model.infraestructura;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.cuentas.CuentaAhorro;
import model.cuentas.CuentaBancaria;
import model.transacciones.Transaccion;

/**
 * Clase que contiene la logica de negocio y reglas del sistema bancario.
 * Gestiona validaciones de limites diarios, bloqueo de cuentas,
 * aplicacion de intereses, notificaciones y deteccion de transacciones sospechosas.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 * @see CuentaAhorro
 * @see Transaccion
 */
public class LogicaNegocio {

    // ==================== ATRIBUTOS ====================
    
    /** Contador de transferencias realizadas en el dia actual. */
    private int contadorTransferenciasHoy;
    
    /** Lista que almacena las ultimas tres transacciones realizadas. */
    private List<Transaccion> ultimasTresTransacciones;
    
    /** Indica si es el primer inicio de sesion del usuario. */
    private boolean esPrimerInicioSesion;
    
    /** Idioma preferido por el usuario (ES, EN, etc.). */
    private String preferenciaIdioma;
    
    /** Indica si las notificaciones estan activadas. */
    private boolean notificacionesActivadas;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear la logica de negocio.
     * Inicializa el contador de transferencias en cero,
     * lista de ultimas transacciones vacia, primer inicio de sesion verdadero,
     * idioma preferido "ES" (espanol) y notificaciones activadas.
     */
    public LogicaNegocio() {
        this.contadorTransferenciasHoy = 0;
        this.ultimasTresTransacciones = new ArrayList<>();
        this.esPrimerInicioSesion = true;
        this.preferenciaIdioma = "ES";
        this.notificacionesActivadas = true;
    }

    // ==================== METODOS DE VALIDACION ====================
    
    /**
     * Valida si una cuenta puede realizar una operacion respetando sus limites diarios.
     * Verifica tanto retiros como transferencias.
     * 
     * @param cuenta Cuenta bancaria a validar
     * @param monto Monto de la operacion
     * @return true si el monto no excede los limites diarios, false en caso contrario
     */
    public boolean ValidarLimiteDiario(CuentaBancaria cuenta, BigDecimal monto) {
        if (!cuenta.puedeRetirar(monto) && !cuenta.puedeTransferir(monto)) {
            return false;
        }
        return true;
    }

    /**
     * Verifica si una cuenta bancaria esta bloqueada.
     * 
     * @param cuenta Cuenta bancaria a verificar
     * @return true si la cuenta esta bloqueada, false en caso contrario
     */
    public boolean VerificarBloqueo(CuentaBancaria cuenta) {
        return cuenta.isEstaBloqueada();
    }

    /**
     * Registra un intento fallido de autenticacion en una cuenta.
     * Si se alcanzan 3 intentos fallidos, envia una notificacion de bloqueo.
     * 
     * @param cuenta Cuenta en la que se registro el intento fallido
     */
    public void RegistrarIntentoFallido(CuentaBancaria cuenta) {
        cuenta.registrarIntentoFallido();
        if (cuenta.getIntentosFallidos() >= 3) {
            EnviarNotificacion("Cuenta bloqueada por multiples intentos fallidos");
        }
    }

    // ==================== METODOS DE OPERACIONES FINANCIERAS ====================
    
    /**
     * Aplica los intereses a una cuenta de ahorro.
     * Calcula los intereses, los deposita en la cuenta y envia una notificacion.
     * 
     * @param cuenta Cuenta de ahorro a la que se aplicaran los intereses
     */
    public void AplicarIntereses(CuentaAhorro cuenta) {
        BigDecimal intereses = cuenta.calcularIntereses();
        cuenta.depositar(intereses);
        EnviarNotificacion("Intereses aplicados: $" + intereses);
    }

    // ==================== METODOS DE NOTIFICACIONES ====================
    
    /**
     * Envia una notificacion al usuario si las notificaciones estan activadas.
     * 
     * @param mensaje Mensaje a enviar como notificacion
     */
    public void EnviarNotificacion(String mensaje) {
        if (notificacionesActivadas) {
            System.out.println("[NOTIFICACION] " + mensaje);
        }
    }

    // ==================== METODOS DE REGISTRO DE TRANSACCIONES ====================
    
    /**
     * Registra una transaccion en el historial reciente.
     * Mantiene solo las ultimas 3 transacciones y actualiza el contador
     * de transferencias del dia si la transaccion es una transferencia.
     * 
     * @param transaccion Transaccion a registrar
     */
    public void registrarTransaccion(Transaccion transaccion) {
        ultimasTresTransacciones.add(transaccion);
        if (ultimasTresTransacciones.size() > 3) {
            ultimasTresTransacciones.remove(0);
        }
        
        if (transaccion.getTipoTransaccion().equals("TRANSFERENCIA")) {
            contadorTransferenciasHoy++;
        }
    }

    /**
     * Detecta si una transaccion es potencialmente sospechosa o fraudulenta.
     * Criterios: montos mayores a $5,000,000 o mas de 10 transferencias en el dia.
     * 
     * @param transaccion Transaccion a evaluar
     * @return true si la transaccion es sospechosa, false en caso contrario
     */
    public boolean esTransaccionSospechosa(Transaccion transaccion) {
        BigDecimal monto = transaccion.getMonto();
        
        if (monto.compareTo(new BigDecimal("5000000")) > 0) {
            return true;
        }
        
        if (contadorTransferenciasHoy > 10) {
            return true;
        }
        
        return false;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Contador de transferencias realizadas hoy */
    public int getContadorTransferenciasHoy() { return contadorTransferenciasHoy; }
    
    /** @param contadorTransferenciasHoy Nuevo contador de transferencias */
    public void setContadorTransferenciasHoy(int contadorTransferenciasHoy) { this.contadorTransferenciasHoy = contadorTransferenciasHoy; }

    /** @return Lista de las ultimas tres transacciones */
    public List<Transaccion> getUltimasTresTransacciones() { return ultimasTresTransacciones; }
    
    /** @param ultimasTresTransacciones Nueva lista de ultimas transacciones */
    public void setUltimasTresTransacciones(List<Transaccion> ultimasTresTransacciones) { this.ultimasTresTransacciones = ultimasTresTransacciones; }

    /** @return true si es el primer inicio de sesion */
    public boolean isEsPrimerInicioSesion() { return esPrimerInicioSesion; }
    
    /** @param esPrimerInicioSesion Nuevo estado de primer inicio */
    public void setEsPrimerInicioSesion(boolean esPrimerInicioSesion) { this.esPrimerInicioSesion = esPrimerInicioSesion; }

    /** @return Idioma preferido del usuario */
    public String getPreferenciaIdioma() { return preferenciaIdioma; }
    
    /** @param preferenciaIdioma Nuevo idioma preferido */
    public void setPreferenciaIdioma(String preferenciaIdioma) { this.preferenciaIdioma = preferenciaIdioma; }

    /** @return true si las notificaciones estan activadas */
    public boolean isNotificacionesActivadas() { return notificacionesActivadas; }
    
    /** @param notificacionesActivadas Nuevo estado de notificaciones */
    public void setNotificacionesActivadas(boolean notificacionesActivadas) { this.notificacionesActivadas = notificacionesActivadas; }
}//fin de la clase