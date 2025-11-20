package dda.obligatorio.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import dda.obligatorio.modelo.EstadoPropietario;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Propietario;
import observador.Observable;
import observador.Observador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import dda.obligatorio.ConexionNavegador;

@RestController
@RequestMapping("/cambiar-estado")
@Scope("session")
public class ControladorCambiarEstadoPropietario implements Observador {

    private Fachada fachada = Fachada.getInstancia();
    @Autowired private ConexionNavegador conexionNavegador;
    private String cedulaSesion;

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter registrarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @PostMapping("/obtenerPropietarios")
    public List<Respuesta> obtenerPropietarios() {
        List<Propietario> propietarios = fachada.obtenerPropietarios();
        List<Respuesta> respuestas = new ArrayList<>();
        
        // Transformar a formato simple para la vista
        List<PropietarioSimple> datos = new ArrayList<>();
        for (Propietario p : propietarios) {
            datos.add(new PropietarioSimple(
                p.getCedula(),
                p.getNombreCompleto(),
                p.getEstadoActual().getNombre()
            ));
        }
        
        respuestas.add(new Respuesta("propietarios", datos));
        return respuestas;
    }

    @PostMapping("/buscarPropietario")
    public List<Respuesta> buscarPropietario(@RequestParam String cedula) {
        List<Respuesta> respuestas = new ArrayList<>();
        
        Propietario propietario = fachada.buscarPropietario(cedula);
        if (propietario == null) {
            respuestas.add(new Respuesta("error", "No existe un propietario con cédula " + cedula));
            return respuestas;
        }
        propietario.agregarObservador(this);
        this.cedulaSesion = cedula;
        
        respuestas.addAll(construirVistaEstado(propietario));
        return respuestas;
    }

    @PostMapping("/obtenerEstadosDisponibles")
    public List<Respuesta> obtenerEstadosDisponibles() {
        List<Respuesta> respuestas = new ArrayList<>();
        // Delegar a EstadoPropietario (Expert del patrón State)
        List<String> estados = EstadoPropietario.obtenerEstadosDisponibles();
        respuestas.add(new Respuesta("estados", estados));
        return respuestas;
    }

    @PostMapping("/cambiarEstado")
    public List<Respuesta> cambiarEstado(
            @RequestParam String cedula,
            @RequestParam String nuevoEstado) {
        
        List<Respuesta> respuestas = new ArrayList<>();
        
        Propietario propietario = fachada.buscarPropietario(cedula);
        if (propietario == null) {
            respuestas.add(new Respuesta("error", "No existe un propietario con cédula " + cedula));
            return respuestas;
        }
        
        // Usar factory method de EstadoPropietario (Expert del patrón State)
        EstadoPropietario estado = EstadoPropietario.crearEstado(nuevoEstado);
        if (estado == null) {
            respuestas.add(new Respuesta("error", "Estado no válido: " + nuevoEstado));
            return respuestas;
        }
        
        propietario.setEstadoActual(estado);
        respuestas.add(new Respuesta("exito", "Estado cambiado a " + nuevoEstado + " para " + propietario.getNombreCompleto()));
        respuestas.addAll(construirVistaEstado(propietario));
        return respuestas;
    }

    private List<Respuesta> construirVistaEstado(Propietario propietario) {
        List<Respuesta> respuestas = new ArrayList<>();
        PropietarioDetalle detalle = new PropietarioDetalle(
            propietario.getCedula(),
            propietario.getNombreCompleto(),
            propietario.getEstadoActual().getNombre()
        );
        respuestas.add(new Respuesta("propietarioEncontrado", detalle));

        // Refrescar lista de propietarios (estado actualizado)
        List<Propietario> propietarios = fachada.obtenerPropietarios();
        List<PropietarioSimple> datos = new ArrayList<>();
        for (Propietario p : propietarios) {
            datos.add(new PropietarioSimple(
                p.getCedula(),
                p.getNombreCompleto(),
                p.getEstadoActual().getNombre()
            ));
        }
        respuestas.add(new Respuesta("propietarios", datos));
        return respuestas;
    }

    // Clases internas para transferir datos a la vista
    private static class PropietarioSimple {
        public String cedula;
        public String nombre;
        public String estado;

        public PropietarioSimple(String cedula, String nombre, String estado) {
            this.cedula = cedula;
            this.nombre = nombre;
            this.estado = estado;
        }
    }

    private static class PropietarioDetalle {
        public String cedula;
        public String nombre;
        public String estado;

        public PropietarioDetalle(String cedula, String nombre, String estado) {
            this.cedula = cedula;
            this.nombre = nombre;
            this.estado = estado;
        }
    }

    @Override
    public void actualizar(Object evento, Observable origen) {
        try {
            if (!(evento instanceof Propietario.Eventos)) return;
            if (cedulaSesion == null) return;
            Propietario propietario = fachada.buscarPropietario(cedulaSesion);
            if (propietario == null) return;
            conexionNavegador.enviarJSON(construirVistaEstado(propietario));
        } catch (Exception e) {
            // Silencioso
        }
    }
}
