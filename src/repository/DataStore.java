package repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import model.cuentas.CuentaAhorro;
import model.cuentas.CuentaBancaria;
import model.cuentas.CuentaCorriente;
import model.cuentas.CuentaJuventud;
import model.cuentas.CuentaTerceraEdad;
import model.infraestructura.Cajero;
import model.infraestructura.LogicaNegocio;
import model.transacciones.Historial;
import model.transacciones.Transaccion;

/**
 * Clase singleton que actua como almacenamiento central de datos del sistema.
 * Simula una base de datos en memoria gestionando cuentas bancarias, cajeros,
 * historial global de transacciones y la logica de negocio.
 * Contiene datos de prueba precargados para demostracion.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaBancaria
 * @see Cajero
 * @see Historial
 * @see LogicaNegocio
 */
public class DataStore {

    // ==================== ATRIBUTOS ====================
    
    /** Instancia unica del singleton DataStore. */
    private static DataStore instance;
    
    /** Mapa de cuentas bancarias indexadas por numero de cuenta. */
    private Map<String, CuentaBancaria> cuentas;
    
    /** Mapa de cajeros indexados por identificador. */
    private Map<String, Cajero> cajeros;
    
    /** Historial global que registra todas las transacciones del sistema. */
    private Historial historialGlobal;
    
    /** Instancia de la logica de negocio para validaciones y reglas. */
    private LogicaNegocio logicaNegocio;
    
    /** Cajero actualmente activo en el sistema. */
    private Cajero cajeroActivo;

    // ==================== CONSTRUCTOR PRIVADO ====================
    
    /**
     * Constructor privado para implementar el patron singleton.
     * Inicializa las estructuras de datos y carga los datos de prueba.
     */
    private DataStore() {
        this.cuentas = new HashMap<>();
        this.cajeros = new HashMap<>();
        this.historialGlobal = new Historial();
        this.logicaNegocio = new LogicaNegocio();
        inicializarDatosPrueba();
    }

    // ==================== METODOS SINGLETON ====================
    
    /**
     * Obtiene la instancia unica del DataStore.
     * Si no existe, la crea por primera vez.
     * 
     * @return Instancia unica del DataStore
     */
    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    // ==================== METODOS PRIVADOS ====================
    
    /**
     * Inicializa datos de prueba para demostracion.
     * Crea un cajero principal y cuatro cuentas de diferentes tipos:
     * - CuentaAhorro: Juan Perez, saldo $1,500,000
     * - CuentaCorriente: Maria Gomez, saldo $2,500,000
     * - CuentaJuventud: Carlos Lopez, saldo $500,000
     * - CuentaTerceraEdad: Ana Rodriguez, saldo $3,000,000
     * Todas las cuentas tienen contrasena "1234".
     */
    private void inicializarDatosPrueba() {
        // Crear cajero
        Cajero cajeroPrincipal = new Cajero("C001", "Centro Comercial Unico");
        cajeros.put(cajeroPrincipal.getIdCajero(), cajeroPrincipal);
        cajeroActivo = cajeroPrincipal;

        // Crear cuentas de prueba
        CuentaAhorro cuentaAhorro = new CuentaAhorro("1001", "Juan Perez", "12345678");
        cuentaAhorro.setContrasenaHash("1234");
        cuentaAhorro.depositar(new BigDecimal("1500000"));
        cuentas.put(cuentaAhorro.getNumeroCuenta(), cuentaAhorro);

        CuentaCorriente cuentaCorriente = new CuentaCorriente("1002", "Maria Gomez", "87654321");
        cuentaCorriente.setContrasenaHash("1234");
        cuentaCorriente.depositar(new BigDecimal("2500000"));
        cuentas.put(cuentaCorriente.getNumeroCuenta(), cuentaCorriente);

        CuentaJuventud cuentaJoven = new CuentaJuventud("1003", "Carlos Lopez", "11223344", 
                LocalDate.of(2000, 5, 15));
        cuentaJoven.setContrasenaHash("1234");
        cuentaJoven.depositar(new BigDecimal("500000"));
        cuentas.put(cuentaJoven.getNumeroCuenta(), cuentaJoven);

        CuentaTerceraEdad cuentaMayor = new CuentaTerceraEdad("1004", "Ana Rodriguez", "99887766",
                LocalDate.of(1950, 3, 10));
        cuentaMayor.setContrasenaHash("1234");
        cuentaMayor.depositar(new BigDecimal("3000000"));
        cuentas.put(cuentaMayor.getNumeroCuenta(), cuentaMayor);
    }

    // ==================== METODOS CRUD PARA CUENTAS ====================
    
    /**
     * Obtiene una cuenta bancaria por su numero de cuenta.
     * 
     * @param numeroCuenta Numero de cuenta a buscar
     * @return Cuenta bancaria encontrada o null si no existe
     */
    public CuentaBancaria getCuenta(String numeroCuenta) {
        return cuentas.get(numeroCuenta);
    }

    /**
     * Obtiene todas las cuentas bancarias del sistema.
     * 
     * @return Mapa de todas las cuentas indexadas por numero de cuenta
     */
    public Map<String, CuentaBancaria> getAllCuentas() {
        return cuentas;
    }

    /**
     * Agrega una nueva cuenta bancaria al sistema.
     * 
     * @param cuenta Cuenta bancaria a agregar
     */
    public void addCuenta(CuentaBancaria cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
    }

    /**
     * Actualiza una cuenta bancaria existente en el sistema.
     * 
     * @param cuenta Cuenta bancaria con los datos actualizados
     */
    public void updateCuenta(CuentaBancaria cuenta) {
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
    }

    // ==================== METODOS PARA TRANSACCIONES ====================
    
    /**
     * Registra una transaccion en el sistema.
     * La agrega al historial global y la registra en la logica de negocio.
     * 
     * @param transaccion Transaccion a registrar
     */
    public void registrarTransaccion(Transaccion transaccion) {
        historialGlobal.AgregarTransaccion(transaccion);
        logicaNegocio.registrarTransaccion(transaccion);
    }

    /**
     * Obtiene el historial global de transacciones del sistema.
     * 
     * @return Historial con todas las transacciones registradas
     */
    public Historial getHistorialGlobal() {
        return historialGlobal;
    }

    // ==================== METODOS PARA CAJERO ====================
    
    /**
     * Obtiene el cajero actualmente activo en el sistema.
     * 
     * @return Cajero activo
     */
    public Cajero getCajeroActivo() {
        return cajeroActivo;
    }

    /**
     * Obtiene la instancia de la logica de negocio del sistema.
     * 
     * @return Logica de negocio para validaciones y reglas
     */
    public LogicaNegocio getLogicaNegocio() {
        return logicaNegocio;
    }
}//fin de la clase