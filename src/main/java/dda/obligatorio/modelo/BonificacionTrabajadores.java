package dda.obligatorio.modelo;

import java.util.List;

public class BonificacionTrabajadores extends Bonificacion {

    public BonificacionTrabajadores() {
        super("Bonificación Trabajadores");
    }

    @Override
    public double aplicarDescuento(double montoBase, Vehiculo vehiculo, 
                                   Puesto puesto, List<Transito> transitosAnteriores) {
        return montoBase * 0.2;
    }

    @Override
    public boolean aplica(Vehiculo vehiculo, Puesto puesto, 
                         List<Transito> transitosAnteriores) {
        return esDiaSemana();
    }

}
