package dda.obligatorio.dtos;
public class DTOResumenPropietario {

    private String nombreCompleto;
    private String estado;
    private double saldoActual;
    
    public DTOResumenPropietario(String nombreCompleto, String estado, double saldoActual) {
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.saldoActual = saldoActual;
    }
    
    public String getNombreCompleto() { return nombreCompleto; }
    public String getEstado() { return estado; }
    public double getSaldoActual() { return saldoActual; }
}
