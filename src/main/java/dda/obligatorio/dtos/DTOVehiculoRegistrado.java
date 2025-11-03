package dda.obligatorio.dtos;

public class DTOVehiculoRegistrado {

    private String matricula;
    private String modelo;
    private String color;
    private int cantidadTransitos;
    private double montoTotal;
    
    public DTOVehiculoRegistrado(String matricula, String modelo, String color, 
                                 int cantidadTransitos, double montoTotal) {
        this.matricula = matricula;
        this.modelo = modelo;
        this.color = color;
        this.cantidadTransitos = cantidadTransitos;
        this.montoTotal = montoTotal;
    }
    
    public String getMatricula() { return matricula; }
    public String getModelo() { return modelo; }
    public String getColor() { return color; }
    public int getCantidadTransitos() { return cantidadTransitos; }
    public double getMontoTotal() { return montoTotal; }

}
