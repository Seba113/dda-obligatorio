package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import observador.Observable;

public class Propietario extends Usuario {

    private double saldoActual;
    private double saldoMinimo;
    private EstadoPropietario estadoActual;
    private List<Vehiculo> vehiculos;
    private List<Asignacion> asignaciones;
    private List<Notificacion> notificaciones;
    private List<Transito> transitos;
    private transient Observable observable = new Observable();

    // Eventos de dominio simples para Observer
    public enum Eventos {
        CAMBIO_ESTADO,
        CAMBIO_SALDO,
        CAMBIO_NOTIFICACIONES,
        CAMBIO_ASIGNACIONES,
        CAMBIO_TRANSITO
    }

    public Propietario(String cedula, String contrasena, String nombreCompleto, 
                      double saldoActual, double saldoMinimo) {
        super(cedula, contrasena, nombreCompleto);
        this.saldoActual = saldoActual;
        this.saldoMinimo = saldoMinimo;
        this.estadoActual = new Habilitado();
        this.vehiculos = new ArrayList<>();
        this.asignaciones = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        this.transitos = new ArrayList<>();
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
        observable.avisar(Eventos.CAMBIO_ESTADO);
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

    public List<Transito> getTransitos() {
        return transitos;
    }

    public void setTransitos(List<Transito> transitos) {
        this.transitos = transitos;
    }

    public void agregarVehiculo(Vehiculo vehiculo) {
        this.vehiculos.add(vehiculo);
    }

    public void agregarAsignacion(Asignacion asignacion) {
        this.asignaciones.add(asignacion);
        observable.avisar(Eventos.CAMBIO_ASIGNACIONES);
    }

    public void asignarBonificacionAPuesto(Bonificacion bonificacion, Puesto puesto, Date fecha) {
        Asignacion existente = null;
        for (Asignacion a : asignaciones) {
            if (a.getPuesto().equals(puesto)) {
                existente = a;
                break;
            }
        }
        if (existente != null) {
            existente.setBonificacion(bonificacion);
            existente.setFechaAsignacion(fecha);
        } else {
            agregarAsignacion(new Asignacion(fecha, bonificacion, puesto));
        }
        observable.avisar(Eventos.CAMBIO_ASIGNACIONES);
    }

    public void agregarNotificacion(Notificacion notificacion) {
        this.notificaciones.add(notificacion);
        observable.avisar(Eventos.CAMBIO_NOTIFICACIONES);
    }

    public void agregarTransito(Transito transito) {
        this.transitos.add(transito);
        observable.avisar(Eventos.CAMBIO_TRANSITO);
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
            observable.avisar(Eventos.CAMBIO_SALDO);
            return true;
        }
        return false;
    }

    public void recargarSaldo(double monto) {
        this.saldoActual += monto;
        observable.avisar(Eventos.CAMBIO_SALDO);
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
        observable.avisar(Eventos.CAMBIO_NOTIFICACIONES);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v.getMatricula().equals(matricula)) {
                return v;
            }
        }
        return null;
    }

    // Observer facade methods
    public void agregarObservador(observador.Observador obs) {
         observable.agregarObservador(obs); 
    }

    public void quitarObservador(observador.Observador obs) {
         observable.quitarObservador(obs); 
    }

    @Override
    public String toString() {
        return super.toString() + " [Estado: " + estadoActual + ", Saldo: $" + saldoActual + "]";
    }




}
