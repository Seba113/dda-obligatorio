package dda.obligatorio.controlador;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

import dda.obligatorio.dtos.DTOBonificacion;
import dda.obligatorio.dtos.DTONotificacion;
import dda.obligatorio.dtos.DTOResumenPropietario;
import dda.obligatorio.dtos.DTOTransito;
import dda.obligatorio.dtos.DTOVehiculoRegistrado;
import dda.obligatorio.modelo.Asignacion;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Notificacion;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Transito;
import dda.obligatorio.modelo.Vehiculo;

@RestController
public class ControladorPropietario {
    private Fachada fachada = Fachada.getInstancia();

    @PostMapping("/propietario/tablero")
    public List<Respuesta> obtenerTableroPropietario(HttpSession sesionHttp, @RequestParam(required = false) String cedula) {
        List<Respuesta> respuestas = new ArrayList<>();

        if (cedula == null || cedula.isEmpty()) {
            Propietario sesionProp = (Propietario) sesionHttp.getAttribute("usuarioPropietario");
            if (sesionProp == null) {
                respuestas.add(new Respuesta("paginaLogin", "/login-propietarios"));
                return respuestas;
            }
            cedula = sesionProp.getCedula();
        }

        Propietario propietario = fachada.buscarPropietario(cedula);
        if (propietario == null) {
            respuestas.add(new Respuesta("error", "Propietario no encontrado"));
            return respuestas;
        }

        // Resumen
        DTOResumenPropietario resumen = new DTOResumenPropietario(
            propietario.getNombreCompleto(),
            propietario.getEstadoActual().getNombre(),
            propietario.getSaldoActual()
        );
        respuestas.add(new Respuesta("resumen", resumen));

        // Bonificaciones
        List<DTOBonificacion> bonificaciones = obtenerBonificaciones(propietario);
        respuestas.add(new Respuesta("bonificaciones", bonificaciones));

        // Vehículos
        List<DTOVehiculoRegistrado> vehiculos = obtenerVehiculosRegistrados(propietario, cedula);
        respuestas.add(new Respuesta("vehiculos", vehiculos));

        // Tránsitos
        List<DTOTransito> transitos = obtenerTransitosRealizados(cedula);
        respuestas.add(new Respuesta("transitos", transitos));

        // Notificaciones
        List<DTONotificacion> notificaciones = obtenerNotificacionesPropietario(propietario);
        respuestas.add(new Respuesta("notificaciones", notificaciones));

        respuestas.add(new Respuesta("exito", "Datos cargados correctamente"));
        return respuestas;
    }

    private List<DTOBonificacion> obtenerBonificaciones(Propietario propietario) {
        List<DTOBonificacion> bonificaciones = new ArrayList<>();
        for (Asignacion asig : propietario.getAsignaciones()) {
            bonificaciones.add(new DTOBonificacion(
                asig.getBonificacion().getNombre(),
                asig.getPuesto().getNombre(),
                asig.getFechaAsignacion()
            ));
        }
        return bonificaciones;
    }

    private List<DTOVehiculoRegistrado> obtenerVehiculosRegistrados(Propietario propietario, String cedula) {
        List<DTOVehiculoRegistrado> vehiculos = new ArrayList<>();
        List<Transito> todosTransitos = fachada.listarTransitosPropietario(cedula);

        Map<String, Integer> transitosPorVehiculo = new HashMap<>();
        Map<String, Double> montoPorVehiculo = new HashMap<>();

        for (Transito t : todosTransitos) {
            String matricula = t.getVehiculo().getMatricula();
            transitosPorVehiculo.put(matricula, transitosPorVehiculo.getOrDefault(matricula, 0) + 1);
            montoPorVehiculo.put(matricula, montoPorVehiculo.getOrDefault(matricula, 0.0) + t.getMontoFinal());
        }

        for (Vehiculo veh : propietario.getVehiculos()) {
            String matricula = veh.getMatricula();
            vehiculos.add(new DTOVehiculoRegistrado(
                matricula,
                veh.getModelo(),
                veh.getColor(),
                transitosPorVehiculo.getOrDefault(matricula, 0),
                montoPorVehiculo.getOrDefault(matricula, 0.0)
            ));
        }

        return vehiculos;
    }

    private List<DTOTransito> obtenerTransitosRealizados(String cedula) {
        List<DTOTransito> lista = new ArrayList<>();
        List<Transito> transitos = fachada.listarTransitosPropietario(cedula);
    SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");

        for (Transito t : transitos) {
            String puesto = t.getPuesto() != null ? t.getPuesto().getNombre() : "";
            String matricula = t.getVehiculo() != null ? t.getVehiculo().getMatricula() : "";
            String tarifa = t.getTarifaCobrada() != null ? t.getTarifaCobrada().toString() : "";
            double montoTarifa = t.getTarifaCobrada() != null ? t.getTarifaCobrada().getMonto() : 0.0;
            double montoPagado = t.getMontoFinal();
            double montoBonificacion = montoTarifa - montoPagado;
            String nombreBonificacion = montoBonificacion > 0 ? "Aplicada" : "";
            String hora = t.getFecha() != null ? dfTime.format(t.getFecha()) : null;

            lista.add(new DTOTransito(puesto, matricula, tarifa, montoTarifa, nombreBonificacion, montoBonificacion, montoPagado, t.getFecha(), hora));
        }

        return lista;
    }

    @PostMapping("/propietario/borrarNotificaciones")
    public List<Respuesta> borrarNotificaciones(HttpSession sesionHttp, @RequestParam(required = false) String cedula) {
        Propietario sesionProp = (Propietario) sesionHttp.getAttribute("usuarioPropietario");
        if (sesionProp == null) {
            return Respuesta.lista(new Respuesta("paginaLogin", "/login-propietarios"));
        }

        if (cedula == null || cedula.isEmpty()) {
            cedula = sesionProp.getCedula();
        }

        try {
            fachada.borrarNotificacionesPropietario(cedula);
            // Recargar el tablero con las notificaciones ya borradas
            return obtenerTableroPropietario(sesionHttp, cedula);
        } catch (Exception e) {
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }
    }

    private List<DTONotificacion> obtenerNotificacionesPropietario(Propietario propietario) {
        List<DTONotificacion> notificaciones = new ArrayList<>();
        List<Notificacion> notifsOriginales = new ArrayList<>(propietario.getNotificaciones());
        // Ordenar por fecha descendente
        Collections.sort(notifsOriginales, (n1, n2) -> n2.getFecha().compareTo(n1.getFecha()));

        for (Notificacion notif : notifsOriginales) {
            notificaciones.add(new DTONotificacion(
                notif.getFecha(),
                notif.getMensaje()
            ));
        }

        return notificaciones;
    }
}
