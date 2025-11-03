package dda.obligatorio.dtos;

import java.util.Date;

public class DTONotificacion {

    private Date fechaHora;
    private String mensaje;
    
    public DTONotificacion(Date fechaHora, String mensaje) {
        this.fechaHora = fechaHora;
        this.mensaje = mensaje;
    }
    
    // Getters
    public Date getFechaHora() { return fechaHora; }
    public String getMensaje() { return mensaje; }
}
