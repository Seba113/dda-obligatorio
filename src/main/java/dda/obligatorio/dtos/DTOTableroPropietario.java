package dda.obligatorio.dtos;

import java.util.List;

public class DTOTableroPropietario {

    private DTOResumenPropietario resumen;
    private List<DTOBonificacion> bonificaciones;
    private List<DTOVehiculoRegistrado> vehiculos;
    private List<DTOTransito> transitos;
    private List<DTONotificacion> notificaciones;
    
    public DTOTableroPropietario(DTOResumenPropietario resumen,
                                List<DTOBonificacion> bonificaciones,
                                List<DTOVehiculoRegistrado> vehiculos,
                                List<DTOTransito> transitos,
                                List<DTONotificacion> notificaciones) {
        this.resumen = resumen;
        this.bonificaciones = bonificaciones;
        this.vehiculos = vehiculos;
        this.transitos = transitos;
        this.notificaciones = notificaciones;
    }
    
    // Getters
    public DTOResumenPropietario getResumen() { return resumen; }
    public List<DTOBonificacion> getBonificaciones() { return bonificaciones; }
    public List<DTOVehiculoRegistrado> getVehiculos() { return vehiculos; }
    public List<DTOTransito> getTransitos() { return transitos; }
    public List<DTONotificacion> getNotificaciones() { return notificaciones; }
}
