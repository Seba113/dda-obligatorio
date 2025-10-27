package dda.obligatorio.modelo;

import java.util.Calendar;
import java.util.List;

public class BonificacionFrecuentes extends Bonificacion {

    public BonificacionFrecuentes() {
        super(  "BonificacionFrecuentes");
    }

   @Override
    public double aplicarDescuento(double montoBase, Vehiculo vehiculo, 
                                   Puesto puesto, List<Transito> transitosAnteriores) {
        return montoBase * 0.5; 
    }

    @Override
    public boolean aplica(Vehiculo vehiculo, Puesto puesto, 
                         List<Transito> transitosAnteriores) {
        // Debe ser al menos el segundo tránsito del día con el mismo vehículo en el mismo puesto
        Calendar hoy = Calendar.getInstance();
        int contadorHoy = 0;
        
        for (Transito t : transitosAnteriores) {
            Calendar fechaTransito = Calendar.getInstance();
            fechaTransito.setTime(t.getFecha());
            
            // Verificar si es del mismo día, mismo vehículo y mismo puesto
            if (t.getVehiculo().equals(vehiculo) && 
                t.getPuesto().equals(puesto) &&
                esMismoDia(fechaTransito, hoy)) {
                contadorHoy++;
            }
        }
        return contadorHoy >= 1;
    }

}
