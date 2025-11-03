package dda.obligatorio.dtos;

import java.util.Date;

public class DTOBonificacion {

    private String nombreBonificacion;
    private String nombrePuesto;
    private Date fechaAsignada;
    
    public DTOBonificacion(String nombreBonificacion, String nombrePuesto, Date fechaAsignada) {
        this.nombreBonificacion = nombreBonificacion;
        this.nombrePuesto = nombrePuesto;
        this.fechaAsignada = fechaAsignada;
    }
    
    public String getNombreBonificacion() { return nombreBonificacion; }
    public String getNombrePuesto() { return nombrePuesto; }
    public Date getFechaAsignada() { return fechaAsignada; }
}
