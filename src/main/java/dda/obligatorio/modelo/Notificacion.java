package dda.obligatorio.modelo;

import java.util.Date;

public class Notificacion {
    
    private Date fecha;
    private String mensaje;
    private String tipo;

    public Notificacion(Date fecha, String mensaje, String tipo) {
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {
        return "Notificación [" + tipo + "]: " + mensaje + " (" + fecha + ")";
    }
}
