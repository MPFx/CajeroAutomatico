package model.transacciones;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que representa el historial de transacciones de una cuenta bancaria.
 * Almacena todas las transacciones realizadas y permite filtrarlas por fecha,
 * tipo, obtener las ultimas N transacciones, y calcular totales depositados y retirados.
 * 
 * @author ISC Israel de Jesus Mar Parada
 * @version 1.0
 * @see Transaccion
 */
public class Historial {

    // ==================== ATRIBUTOS ====================
    
    /** Lista de todas las transacciones registradas en el historial. */
    private List<Transaccion> listaTransacciones;
    
    /** Fecha de inicio del periodo de consulta del historial. */
    private LocalDateTime periodoDesde;
    
    /** Fecha de fin del periodo de consulta del historial. */
    private LocalDateTime periodoHasta;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor para crear un nuevo historial de transacciones.
     * Inicializa la lista de transacciones como vacia y establece el periodo
     * por defecto desde hace un mes hasta la fecha actual.
     */
    public Historial() {
        this.listaTransacciones = new ArrayList<>();
        this.periodoDesde = LocalDateTime.now().minusMonths(1);
        this.periodoHasta = LocalDateTime.now();
    }

    // ==================== METODOS DE GESTION DE TRANSACCIONES ====================
    
    /**
     * Agrega una transaccion al historial.
     * 
     * @param transaccion Transaccion a agregar al historial
     */
    public void AgregarTransaccion(Transaccion transaccion) {
        listaTransacciones.add(transaccion);
    }

    /**
     * Filtra las transacciones por un rango de fechas.
     * 
     * @param desde Fecha y hora de inicio del filtro
     * @param hasta Fecha y hora de fin del filtro
     * @return Lista de transacciones dentro del rango de fechas especificado
     */
    public List<Transaccion> FiltrarPorFecha(LocalDateTime desde, LocalDateTime hasta) {
        return listaTransacciones.stream()
                .filter(t -> !t.getFechaHora().isBefore(desde) && !t.getFechaHora().isAfter(hasta))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ultimas N transacciones del historial.
     * 
     * @param n Cantidad de transacciones a obtener (desde la mas reciente)
     * @return Lista con las ultimas N transacciones
     */
    public List<Transaccion> ObtenerUltimasTransacciones(int n) {
        int tamaño = listaTransacciones.size();
        if (tamaño <= n) {
            return new ArrayList<>(listaTransacciones);
        }
        return listaTransacciones.subList(tamaño - n, tamaño);
    }

    /**
     * Filtra las transacciones por un tipo especifico.
     * 
     * @param tipo Tipo de transaccion a filtrar (DEPOSITO, RETIRO, TRANSFERENCIA)
     * @return Lista de transacciones del tipo especificado
     */
    public List<Transaccion> filtrarPorTipo(String tipo) {
        return listaTransacciones.stream()
                .filter(t -> t.getTipoTransaccion().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    // ==================== METODOS DE CALCULO ====================
    
    /**
     * Calcula el monto total depositado en todas las transacciones.
     * Suma los montos de todas las transacciones de tipo DEPOSITO.
     * 
     * @return Suma total de los depositos
     */
    public BigDecimal calcularTotalDepositado() {
        return listaTransacciones.stream()
                .filter(t -> t.getTipoTransaccion().equals("DEPOSITO"))
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula el monto total retirado en todas las transacciones.
     * Suma los montos de todas las transacciones de tipo RETIRO.
     * 
     * @return Suma total de los retiros
     */
    public BigDecimal calcularTotalRetirado() {
        return listaTransacciones.stream()
                .filter(t -> t.getTipoTransaccion().equals("RETIRO"))
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==================== GETTERS Y SETTERS ====================
    
    /** @return Lista completa de transacciones del historial */
    public List<Transaccion> getListaTransacciones() { return listaTransacciones; }
    
    /** @param listaTransacciones Nueva lista de transacciones */
    public void setListaTransacciones(List<Transaccion> listaTransacciones) { this.listaTransacciones = listaTransacciones; }

    /** @return Fecha de inicio del periodo de consulta */
    public LocalDateTime getPeriodoDesde() { return periodoDesde; }
    
    /** @param periodoDesde Nueva fecha de inicio del periodo */
    public void setPeriodoDesde(LocalDateTime periodoDesde) { this.periodoDesde = periodoDesde; }

    /** @return Fecha de fin del periodo de consulta */
    public LocalDateTime getPeriodoHasta() { return periodoHasta; }
    
    /** @param periodoHasta Nueva fecha de fin del periodo */
    public void setPeriodoHasta(LocalDateTime periodoHasta) { this.periodoHasta = periodoHasta; }
}