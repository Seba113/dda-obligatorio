package dda.obligatorio.controlador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dda.obligatorio.modelo.Deshabilitado;
import dda.obligatorio.modelo.EstadoPropietario;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Habilitado;
import dda.obligatorio.modelo.Penalizado;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Suspendido;

@RestController
@RequestMapping("/cambiar-estado")
public class ControladorCambiarEstadoPropietario {

    private Fachada fachada = Fachada.getInstancia();

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
        
        PropietarioDetalle detalle = new PropietarioDetalle(
            propietario.getCedula(),
            propietario.getNombreCompleto(),
            propietario.getEstadoActual().getNombre()
        );
        
        respuestas.add(new Respuesta("propietarioEncontrado", detalle));
        return respuestas;
    }

    @PostMapping("/obtenerEstadosDisponibles")
    public List<Respuesta> obtenerEstadosDisponibles() {
        List<Respuesta> respuestas = new ArrayList<>();
        List<String> estados = List.of("Habilitado", "Deshabilitado", "Suspendido", "Penalizado");
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
        
        // Crear nueva instancia del estado según el nombre (respeta Expert: Propietario es dueño de su estado)
        EstadoPropietario estado = crearEstado(nuevoEstado);
        if (estado == null) {
            respuestas.add(new Respuesta("error", "Estado no válido: " + nuevoEstado));
            return respuestas;
        }
        
        propietario.setEstadoActual(estado);
        
        respuestas.add(new Respuesta("exito", "Estado cambiado a " + nuevoEstado + " para " + propietario.getNombreCompleto()));
        return respuestas;
    }

    private EstadoPropietario crearEstado(String nombreEstado) {
        switch (nombreEstado) {
            case "Habilitado": return new Habilitado();
            case "Deshabilitado": return new Deshabilitado();
            case "Suspendido": return new Suspendido();
            case "Penalizado": return new Penalizado();
            default: return null;
        }
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
}
