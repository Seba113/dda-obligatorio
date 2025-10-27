package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SistemaGestionTransitos {

    private ArrayList<Transito> transitos;

    public SistemaGestionTransitos() {
        this.transitos = new ArrayList<>();
    }

    public List<Transito> obtenerTodosTransitos() {
        return new ArrayList<>(transitos);
    }
    public List<Transito> obtenerTransitosPorVehiculo(String matricula) {
        List<Transito> resultado = new ArrayList<>();
        for (Transito t : transitos) {
            if (t.getVehiculo().getMatricula().equals(matricula)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    public List<Transito> obtenerTransitosPorFecha(Date fecha) {
        List<Transito> resultado = new ArrayList<>();
        Calendar fechaBuscada = Calendar.getInstance();
        fechaBuscada.setTime(fecha);
        
        for (Transito t : transitos) {
            Calendar fechaTransito = Calendar.getInstance();
            fechaTransito.setTime(t.getFecha());
            
            if (esMismoDia(fechaTransito, fechaBuscada)) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    protected boolean esMismoDia(Calendar fecha1, Calendar fecha2) {
        return fecha1.get(Calendar.YEAR) == fecha2.get(Calendar.YEAR) &&
               fecha1.get(Calendar.DAY_OF_YEAR) == fecha2.get(Calendar.DAY_OF_YEAR);
    }

    public int obtenerCantidadTransitos() {
        return transitos.size();
    }

    public double calcularTotalRecaudado() {
        double total = 0;
        for (Transito t : transitos) {
            total += t.getMontoFinal();
        }
        return total;
    }
}
