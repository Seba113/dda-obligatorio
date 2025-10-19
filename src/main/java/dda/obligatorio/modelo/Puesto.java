package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.List;

public class Puesto {

    private String nombre;
    private String direccion;
    private List<Tarifa> tarifas;

    public Puesto(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.tarifas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public List<Tarifa> getTarifas() {
        return tarifas;
    }
    
    public void setTarifas(List<Tarifa> tarifas) {
        this.tarifas = tarifas;
    }

    public void agregarTarifa(Tarifa tarifa) {
        this.tarifas.add(tarifa);
    }

    public Tarifa obtenerTarifa(Categoria categoria) {
        for (Tarifa tarifa : tarifas) {
            if (tarifa.getCategoria().equals(categoria)) {
                return tarifa;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "Puesto [" + nombre + ", " + direccion + "]";
    }
}
