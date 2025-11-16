package dda.obligatorio.modelo;

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
}
