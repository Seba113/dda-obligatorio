package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Propietario extends Usuario {

    private double saldoActual;
    private double saldoMinimo;
    private EstadoPropietario estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Asignacion> asignaciones;
    private List<Notificacion> notificaciones;

    public Propietario(String cedula, String contrasena, String nombreCompleto, 
                      double saldoActual, double saldoMinimo) {
        super(cedula, contrasena, nombreCompleto);
        this.saldoActual = saldoActual;
        this.saldoMinimo = saldoMinimo;
        this.estadoActual = new Habilitado();
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
    
    public EstadoPropietario getEstadoActual() {
        return estadoActual;
    }
    
    public void setEstadoActual(EstadoPropietario estadoActual) {
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
        return estadoActual.puedeRegistrarTransito();
    }

    public boolean puedeRecibirBonificaciones() {
        return estadoActual.puedeAsignarBonificacion();
    }

    public boolean registraNotificaciones() {
        return estadoActual.puedeRegistrarNotificacion();
    }

    public boolean aplicaDescuentos() {
        return estadoActual.puedeAplicarDescuento();
    }

    public boolean puedeLoguerse() {
        return estadoActual.puedeLoguerse();
    }

    public void limpiarNotificaciones() {
        this.notificaciones.clear();
    }

    @Override
    public String toString() {
        return super.toString() + " [Estado: " + estadoActual + ", Saldo: $" + saldoActual + "]";
    }




}
