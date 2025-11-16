package dda.obligatorio.modelo;

import java.util.List;

public abstract class EstadoPropietario {
    protected String nombre;

    public EstadoPropietario(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    public abstract boolean puedeLoguerse();
    public abstract boolean puedeRegistrarTransito();
    public abstract boolean puedeAsignarBonificacion();
    public abstract boolean puedeAplicarDescuento();
    public abstract boolean puedeRegistrarNotificacion();
    public abstract String getMensajeLoginDenegado();
    public abstract String getMensajeTransitoDenegado();
    
    // Método estático para obtener todos los estados disponibles
    public static List<String> obtenerEstadosDisponibles() {
        return List.of("Habilitado", "Deshabilitado", "Suspendido", "Penalizado");
    }
    
    // Método estático de fábrica para crear instancias de estados
    public static EstadoPropietario crearEstado(String nombreEstado) {
        switch (nombreEstado) {
            case "Habilitado": return new Habilitado();
            case "Deshabilitado": return new Deshabilitado();
            case "Suspendido": return new Suspendido();
            case "Penalizado": return new Penalizado();
            default: return null;
        }
    }
}
