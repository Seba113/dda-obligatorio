package dda.obligatorio.modelo;

public class Habilitado extends EstadoPropietario {

    public Habilitado() {
        super("Habilitado");
    }

    @Override
    public boolean puedeLoguerse() {
        return true;
    }

    @Override
    public boolean puedeRegistrarTransito() {
        return true;
    }

    @Override
    public boolean puedeAsignarBonificacion() {
        return true;
    }

    @Override
    public boolean puedeAplicarDescuento() {
        return true;
    }

    @Override
    public boolean puedeRegistrarNotificacion() {
        return true;
    }

    @Override
    public String getMensajeLoginDenegado() {
        return "Ah ocurrido un problema, vuelva a intentarlo.";
    }

}
