package dda.obligatorio.modelo;

import java.util.Date;

public class Asignacion {
    private Date fechaAsignacion;
    private Bonificacion bonificacion;
    private Puesto puesto;

    public Asignacion(Date fechaAsignacion, Bonificacion bonificacion, Puesto puesto) {
        this.fechaAsignacion = fechaAsignacion;
        this.bonificacion = bonificacion;
        this.puesto = puesto;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }
    
    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    
    public Bonificacion getBonificacion() {
        return bonificacion;
    }
    
    public void setBonificacion(Bonificacion bonificacion) {
        this.bonificacion = bonificacion;
    }
    
    public Puesto getPuesto() {
        return puesto;
    }
    
    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }
    
    @Override
    public String toString() {
        return "Asignación [" + bonificacion.getNombre() + " en " + puesto.getNombre() + 
               ", fecha: " + fechaAsignacion + "]";
    }
}
