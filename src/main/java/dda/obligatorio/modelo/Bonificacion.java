package dda.obligatorio.modelo;

import java.util.Calendar;
import java.util.List;

public abstract class Bonificacion {

    private String nombre;

    public Bonificacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public abstract double aplicarDescuento(double montoBase, Vehiculo vehiculo, 
                                           Puesto puesto, List<Transito> transitosAnteriores);

    
                                           
    @Override
    public String toString() {
        return nombre;
    }

    public abstract boolean aplica(Vehiculo vehiculo, Puesto puesto, 
                                   List<Transito> transitosAnteriores);
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bonificacion bonif = (Bonificacion) obj;
        return nombre.equals(bonif.nombre);
    }

    protected boolean esMismoDia(Calendar fecha1, Calendar fecha2) {
        return fecha1.get(Calendar.YEAR) == fecha2.get(Calendar.YEAR) &&
               fecha1.get(Calendar.DAY_OF_YEAR) == fecha2.get(Calendar.DAY_OF_YEAR);
    }

    protected boolean esDiaSemana() {
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_WEEK);
        return dia >= Calendar.MONDAY && dia <= Calendar.FRIDAY;
    }
    
}
