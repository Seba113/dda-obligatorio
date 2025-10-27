package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Propietario extends Usuario {

    private double saldoActual;
    private double saldoMinimo;
    private Estado estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Asignacion> asignaciones;
    private List<Notificacion> notificaciones;

    public Propietario(String cedula, String contrasena, String nombreCompleto, 
                      double saldoActual, double saldoMinimo) {
        super(cedula, contrasena, nombreCompleto);
        this.saldoActual = saldoActual;
        this.saldoMinimo = saldoMinimo;
        this.estadoActual = new Estado(Estado.HABILITADO); // Estado por defecto
        this.vehiculos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }
    
    @Override
    public String getTipoUsuario() {
        return "Propietario";
    }

    public double getSaldoActual() {
        return saldoActual;
    }
    
    public void setSaldoActual(double saldoActual) {
        this.saldoActual = saldoActual;
    }
    
    public double getSaldoMinimo() {
        return saldoMinimo;
    }
    
    public void setSaldoMinimo(double saldoMinimo) {
        this.saldoMinimo = saldoMinimo;
    }
    
    public Estado getEstadoActual() {
        return estadoActual;
    }
    
    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }
    
    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }
    
    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
    
    public List<Asignacion> getAsignaciones() {
        return asignaciones;
    }
    
    public void setAsignaciones(List<Asignacion> asignaciones) {
        this.asignaciones = asignaciones;
    }
    
    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }
    
    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        this.vehiculos.add(vehiculo);
    }

    public void agregarAsignacion(Asignacion asignacion) {
        this.asignaciones.add(asignacion);
    }

    public void agregarNotificacion(Notificacion notificacion) {
        this.notificaciones.add(notificacion);
    }

    public boolean tieneSaldoSuficiente(double monto) {
        return this.saldoActual >= monto;
    }

    public boolean descontarSaldo(double monto) {
        if (tieneSaldoSuficiente(monto)) {
            this.saldoActual -= monto;
            
            if (this.saldoActual < this.saldoMinimo) {
                generarAlertaSaldo();
            }
            return true;
        }
        return false;
    }

    public void recargarSaldo(double monto) {
        this.saldoActual += monto;
    }

    private void generarAlertaSaldo() {
        Notificacion alerta = new Notificacion(
            new Date(),
            "Su saldo está por debajo del mínimo establecido. Saldo actual: $" + saldoActual,
            "Alerta de Saldo"
        );
        agregarNotificacion(alerta);
    }

     public Bonificacion obtenerBonificacionParaPuesto(Puesto puesto) {
        for (Asignacion asig : asignaciones) {
            if (asig.getPuesto().equals(puesto)) {
                return asig.getBonificacion();
            }
        }
        return null;
    }

    public boolean puedeTransitar() {
        String estado = this.estadoActual.getNombre();
        return estado.equals(Estado.HABILITADO) || estado.equals(Estado.PENALIZADO);
    }

    public boolean puedeRecibirBonificaciones() {
        String estado = this.estadoActual.getNombre();
        return estado.equals(Estado.HABILITADO);
    }

    public boolean registraNotificaciones() {
        String estado = this.estadoActual.getNombre();
        return !estado.equals(Estado.PENALIZADO);
    }

    @Override
    public String toString() {
        return super.toString() + " [Estado: " + estadoActual + ", Saldo: $" + saldoActual + "]";
    }




}
