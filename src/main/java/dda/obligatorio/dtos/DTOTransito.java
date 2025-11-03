package dda.obligatorio.dtos;

import java.util.Date;

public class DTOTransito {

    private String puesto;
    private String matricula;
    private String tarifa;
    private double montoTarifa;
    private String bonificacion;
    private double montoBonificacion;
    private double montoPagado;
    private Date fecha;
    private String hora;
    
    public DTOTransito(String puesto, String matricula, String tarifa,
                               double montoTarifa, String bonificacion, 
                               double montoBonificacion, double montoPagado,
                               Date fecha, String hora) {
        this.puesto = puesto;
        this.matricula = matricula;
        this.tarifa = tarifa;
        this.montoTarifa = montoTarifa;
        this.bonificacion = bonificacion;
        this.montoBonificacion = montoBonificacion;
        this.montoPagado = montoPagado;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getPuesto() { return puesto; }
    public String getMatricula() { return matricula; }
    public String getTarifa() { return tarifa; }
    public double getMontoTarifa() { return montoTarifa; }
    public String getBonificacion() { return bonificacion; }
    public double getMontoBonificacion() { return montoBonificacion; }
    public double getMontoPagado() { return montoPagado; }
    public Date getFecha() { return fecha; }
    public String getHora() { return hora; }
}
