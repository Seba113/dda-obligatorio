package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.List;

public class SistemaGestionPeaje {

    private ArrayList<Puesto> puestos;
    private ArrayList<Categoria> categorias;
    private ArrayList<Bonificacion> bonificaciones;
    
    public SistemaGestionPeaje() {
        this.puestos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.bonificaciones = new ArrayList<>();
        
    }

    public List<Puesto> obtenerTodosPuestos() {
        return new ArrayList<>(puestos);
    }

    public List<Categoria> obtenerTodasCategorias() {
        return new ArrayList<>(categorias);
    }

    public List<Bonificacion> obtenerTodasBonificaciones() {
        return new ArrayList<>(bonificaciones);
    }

    public double calcularMontoConBonificacion(double montoBase, Bonificacion bonificacion,
                                              Vehiculo vehiculo, Puesto puesto,
                                              List<Transito> transitosAnteriores) {
        if (bonificacion == null) {
            return montoBase;
        }
        return bonificacion.aplicarDescuento(montoBase, vehiculo, puesto, transitosAnteriores);
    }
}
