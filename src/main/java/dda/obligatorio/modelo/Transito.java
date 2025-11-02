package dda.obligatorio.modelo;

import java.util.Date;
import observador.Observable;

public class Transito {
    
    private Date fecha;
    private Vehiculo vehiculo;
    private Puesto puesto;
    private Tarifa tarifaCobrada;
    private double montoFinal;

    public Transito(Date fecha, Vehiculo vehiculo, Puesto puesto) {
        this.fecha = fecha;
        this.vehiculo = vehiculo;
        this.puesto = puesto;
        this.tarifaCobrada = puesto.obtenerTarifa(vehiculo.getCategoria());
        this.montoFinal = tarifaCobrada != null ? tarifaCobrada.getMonto() : 0;
    }
    
    // Getters y Setters
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public Vehiculo getVehiculo() {
        return vehiculo;
    }
    
    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
    
    public Puesto getPuesto() {
        return puesto;
    }
    
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }
    
    public Tarifa getTarifaCobrada() {
        return tarifaCobrada;
    }
    
    public void setTarifaCobrada(Tarifa tarifaCobrada) {
        this.tarifaCobrada = tarifaCobrada;
    }
    
    public double getMontoFinal() {
        return montoFinal;
    }
    
    public void setMontoFinal(double montoFinal) {
        this.montoFinal = montoFinal;
    }

    public void aplicarBonificacion(Bonificacion bonificacion) {
        if (tarifaCobrada != null && bonificacion != null) {
            this.montoFinal = bonificacion.aplicarDescuento(tarifaCobrada.getMonto(), vehiculo, puesto, null);
        }
    }
    
    @Override
    public String toString() {
        return "Tránsito [" + vehiculo.getMatricula() + " por " + puesto.getNombre() + 
               ", fecha: " + fecha + ", monto: $" + montoFinal + "]";
    }
}

