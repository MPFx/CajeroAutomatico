package ui;

import java.util.Scanner;
import service.CajeroService;
import service.CuentaService;
import service.TransaccionService;

/**
 * Clase que implementa la interfaz de usuario por consola para el cajero automatico.
 * Gestiona la interaccion con el usuario, muestra los menus y procesa las opciones
 * seleccionadas. Coordina los servicios de cuentas, transacciones y cajero.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see CuentaService
 * @see TransaccionService
 * @see CajeroService
 */
public class MenuConsola {

    // ==================== ATRIBUTOS ====================
    
    /** Scanner para leer la entrada del usuario. */
    private Scanner scanner;
    
    /** Servicio para operaciones de cuentas bancarias. */
    private CuentaService cuentaService;
    
    /** Servicio para operaciones de transacciones. */
    private TransaccionService transaccionService;
    
    /** Servicio para operaciones del cajero. */
    private CajeroService cajeroService;
    
    /** Indica si hay una sesion de usuario activa. */
    private boolean sesionActiva;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor del menu de consola.
     * Inicializa el scanner y crea las instancias de los servicios necesarios.
     * La sesion comienza como inactiva.
     */
    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        this.cuentaService = new CuentaService();
        this.transaccionService = new TransaccionService();
        this.cajeroService = new CajeroService();
        this.sesionActiva = false;
    }

    // ==================== METODO PRINCIPAL ====================
    
    /**
     * Inicia el bucle principal del menu.
     * Muestra diferentes menus segun si hay una sesion activa o no.
     * Permite navegar entre todas las opciones del sistema hasta que el usuario decida salir.
     */
    public void iniciar() {
        boolean salir = false;
        
        while (!salir) {
            if (!sesionActiva) {
                mostrarMenuPrincipal();
                int opcion = leerOpcion();
                
                switch (opcion) {
                    case 1:
                        iniciarSesion();
                        break;
                    case 2:
                        cajeroService.consultarEstadoCajero();
                        pausa();
                        break;
                    case 3:
                        System.out.println("Gracias por usar el cajero automatico");
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
            } else {
                mostrarMenuCliente();
                int opcion = leerOpcion();
                
                switch (opcion) {
                    case 1:
                        cuentaService.consultarSaldo();
                        pausa();
                        break;
                    case 2:
                        cuentaService.realizarRetiro(scanner, cajeroService);
                        pausa();
                        break;
                    case 3:
                        cuentaService.realizarDeposito(scanner);
                        pausa();
                        break;
                    case 4:
                        transaccionService.transferir(scanner, cuentaService);
                        pausa();
                        break;
                    case 5:
                        transaccionService.verHistorial();
                        pausa();
                        break;
                    case 6:
                        cuentaService.cambiarContrasena(scanner);
                        pausa();
                        break;
                    case 7:
                        cuentaService.cerrarSesion();
                        sesionActiva = false;
                        System.out.println("Sesion cerrada");
                        pausa();
                        break;
                    default:
                        System.out.println("Opcion no valida");
                }
            }
        }
        
        scanner.close();
    }

    // ==================== METODOS DE MENU ====================
    
    /**
     * Muestra el menu principal cuando no hay una sesion activa.
     * Opciones: Iniciar sesion, Estado del cajero, Salir.
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n=== CAJERO AUTOMATICO ===");
        System.out.println("1. Iniciar sesion");
        System.out.println("2. Estado del cajero");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    /**
     * Muestra el menu de cliente cuando hay una sesion activa.
     * Opciones: Consultar saldo, Retirar, Depositar, Transferir,
     * Ver historial, Cambiar contrasena, Cerrar sesion.
     */
    private void mostrarMenuCliente() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Retirar efectivo");
        System.out.println("3. Depositar");
        System.out.println("4. Transferir");
        System.out.println("5. Ver historial");
        System.out.println("6. Cambiar contrasena");
        System.out.println("7. Cerrar sesion");
        System.out.print("Seleccione una opcion: ");
    }

    // ==================== METODOS AUXILIARES ====================
    
    /**
     * Lee y valida la opcion ingresada por el usuario.
     * 
     * @return Numero entero de la opcion seleccionada, o 0 si no es valida
     */
    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Inicia el proceso de autenticacion de un usuario.
     * Solicita numero de cuenta y contrasena, y activa la sesion si las credenciales son correctas.
     */
    private void iniciarSesion() {
        System.out.println("\n=== INICIO DE SESION ===");
        System.out.print("Numero de cuenta: ");
        String numero = scanner.nextLine();
        System.out.print("Contrasena: ");
        String contrasena = scanner.nextLine();

        if (cuentaService.autenticar(numero, contrasena)) {
            sesionActiva = true;
        }
        pausa();
    }

    /**
     * Pausa la ejecucion hasta que el usuario presione Enter.
     * Util para que el usuario pueda leer mensajes antes de continuar.
     */
    private void pausa() {
        System.out.println("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}//fin de la clase