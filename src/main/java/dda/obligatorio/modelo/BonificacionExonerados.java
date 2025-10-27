package dda.obligatorio.modelo;

import java.util.List;

public class BonificacionExonerados extends Bonificacion{

    public BonificacionExonerados() {
        super("Bonificación Exonerados");
    }

    @Override
    public double aplicarDescuento(double montoBase, Vehiculo vehiculo, 
                                   Puesto puesto, List<Transito> transitosAnteriores) {
        return 0; // No pagan nada (100% descuento)
    }

     @Override
    public boolean aplica(Vehiculo vehiculo, Puesto puesto, 
                         List<Transito> transitosAnteriores) {
        return true;
    }
}

