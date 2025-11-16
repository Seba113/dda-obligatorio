package dda.obligatorio.modelo;

public class Deshabilitado extends EstadoPropietario {

    public Deshabilitado() {
        super("Deshabilitado");
    }

    @Override
    public boolean puedeLoguerse() {
        return false;
    }

    @Override
    public boolean puedeRegistrarTransito() {
        return false;
    }

    @Override
    public boolean puedeAsignarBonificacion() {
        return false;
    }

    @Override
    public boolean puedeAplicarDescuento() {
        return false;
    }

    @Override
    public boolean puedeRegistrarNotificacion() {
        return false;
    }

    @Override
    public String getMensajeLoginDenegado() {
        return "Usuario deshabilitado. No puede ingresar al sistema.";
    }
    @Override
    public String getMensajeTransitoDenegado() {
        return "El propietario del vehículo está deshabilitado, no puede realizar tránsitos";
    }

}
