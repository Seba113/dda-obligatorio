package dda.obligatorio.modelo;

public class Penalizado extends EstadoPropietario {

    public Penalizado() {
        super("Penalizado");
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
        return "Acceso denegado.";
    }
    @Override
    public String getMensajeTransitoDenegado() {
        return ""; // Puede registrar tránsito; no aplica descuentos/avisos según banderas
    }

}
