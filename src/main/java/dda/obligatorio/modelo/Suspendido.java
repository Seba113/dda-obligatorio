package dda.obligatorio.modelo;

public class Suspendido extends EstadoPropietario {

    public Suspendido() {
        super("Suspendido");
    }

    @Override
    public boolean puedeLoguerse() {
        return true;
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
        return true;
    }

    @Override
    public String getMensajeLoginDenegado() {
        return "Acceso denegado.";
    }

}
