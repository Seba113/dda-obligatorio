package dda.obligatorio.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import dda.obligatorio.modelo.Bonificacion;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Puesto;
import dda.obligatorio.modelo.Asignacion;
import observador.Observable;
import observador.Observador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import dda.obligatorio.ConexionNavegador;

@RestController
@RequestMapping("/asignar-bonificaciones")
@Scope("session")
public class ControladorAsignarBonificaciones implements Observador {

    private Fachada fachada = Fachada.getInstancia();
    @Autowired private ConexionNavegador conexionNavegador;
    private String cedulaSesion;

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

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
        // Registrar este controlador para recibir eventos de este propietario
        p.agregarObservador(this);
        this.cedulaSesion = cedula;

        res.addAll(construirVistaAsignar(p));
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
        // devolver snapshot actualizado
        res.addAll(construirVistaAsignar(p));
        return res;
    }

    private List<Respuesta> construirVistaAsignar(Propietario p) {
        List<Respuesta> res = new ArrayList<>();
        PropietarioDetalle detalle = new PropietarioDetalle(
            p.getCedula(),
            p.getNombreCompleto(),
            p.getEstadoActual().getNombre(),
            mapAsignaciones(p.getAsignaciones())
        );
        res.add(new Respuesta("propietario", detalle));
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

    @Override
    public void actualizar(Object evento, Observable origen) {
        try {
            if (!(evento instanceof Propietario.Eventos)) return;
            if (cedulaSesion == null) return;
            Propietario p = fachada.buscarPropietario(cedulaSesion);
            if (p == null) return;
            conexionNavegador.enviarJSON(construirVistaAsignar(p));
        } catch (Exception e) {
            // Silencioso
        }
    }
}
