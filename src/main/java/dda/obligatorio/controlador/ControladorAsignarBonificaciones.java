package dda.obligatorio.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dda.obligatorio.modelo.Bonificacion;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Puesto;
import dda.obligatorio.modelo.Asignacion;

@RestController
@RequestMapping("/asignar-bonificaciones")
public class ControladorAsignarBonificaciones {

    private Fachada fachada = Fachada.getInstancia();

    @PostMapping("/obtenerBonificaciones")
    public List<Respuesta> obtenerBonificaciones() {
        List<String> nombres = new ArrayList<>();
        for (Bonificacion b : fachada.obtenerTodasBonificaciones()) {
            nombres.add(b.getNombre());
        }
        List<Respuesta> res = new ArrayList<>();
        res.add(new Respuesta("bonificaciones", nombres));
        return res;
    }

    @PostMapping("/obtenerPuestos")
    public List<Respuesta> obtenerPuestos() {
        List<String> nombres = new ArrayList<>();
        for (Puesto p : fachada.obtenerTodosPuestos()) {
            nombres.add(p.getNombre());
        }
        List<Respuesta> res = new ArrayList<>();
        res.add(new Respuesta("puestos", nombres));
        return res;
    }

    @PostMapping("/buscarPropietario")
    public List<Respuesta> buscarPropietario(@RequestParam String cedula) {
        List<Respuesta> res = new ArrayList<>();
        Propietario p = fachada.buscarPropietario(cedula);
        if (p == null) {
            res.add(new Respuesta("error", "No existe un propietario con cédula " + cedula));
            return res;
        }

        PropietarioDetalle detalle = new PropietarioDetalle(
            p.getCedula(),
            p.getNombreCompleto(),
            p.getEstadoActual().getNombre(),
            mapAsignaciones(p.getAsignaciones())
        );
        res.add(new Respuesta("propietario", detalle));
        return res;
    }

    @PostMapping("/asignar")
    public List<Respuesta> asignar(@RequestParam String cedula,
                                   @RequestParam String bonificacion,
                                   @RequestParam String puesto) {
        List<Respuesta> res = new ArrayList<>();
        Propietario p = fachada.buscarPropietario(cedula);
        if (p == null) {
            res.add(new Respuesta("error", "No existe un propietario con cédula " + cedula));
            return res;
        }

        if (!p.puedeRecibirBonificaciones()) {
            res.add(new Respuesta("error", "El propietario en estado '" + p.getEstadoActual().getNombre() + "' no permite asignar bonificaciones"));
            return res;
        }

        Bonificacion b = fachada.buscarBonificacionPorNombre(bonificacion);
        Puesto pu = fachada.buscarPuestoPorNombre(puesto);
        if (b == null || pu == null) {
            res.add(new Respuesta("error", "Datos inválidos: bonificación o puesto inexistente"));
            return res;
        }

        p.asignarBonificacionAPuesto(b, pu, new Date());

        res.add(new Respuesta("exito", "Bonificación asignada correctamente"));
        // devolver lista actualizada
        res.add(new Respuesta("asignaciones", mapAsignaciones(p.getAsignaciones())));
        return res;
    }

    private List<AsignacionDetalle> mapAsignaciones(List<Asignacion> asignaciones) {
        List<AsignacionDetalle> lista = new ArrayList<>();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Asignacion a : asignaciones) {
            lista.add(new AsignacionDetalle(
                a.getBonificacion().getNombre(),
                a.getPuesto().getNombre(),
                fmt.format(a.getFechaAsignacion())
            ));
        }
        return lista;
    }

    // DTOs simples para la vista
    private static class PropietarioDetalle {
        public String cedula;
        public String nombre;
        public String estado;
        public List<AsignacionDetalle> asignaciones;

        public PropietarioDetalle(String cedula, String nombre, String estado, List<AsignacionDetalle> asignaciones) {
            this.cedula = cedula;
            this.nombre = nombre;
            this.estado = estado;
            this.asignaciones = asignaciones;
        }
    }

    private static class AsignacionDetalle {
        public String bonificacion;
        public String puesto;
        public String fecha;

        public AsignacionDetalle(String bonificacion, String puesto, String fecha) {
            this.bonificacion = bonificacion;
            this.puesto = puesto;
            this.fecha = fecha;
        }
    }
}
