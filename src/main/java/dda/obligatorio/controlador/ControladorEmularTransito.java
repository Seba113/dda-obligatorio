package dda.obligatorio.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import dda.obligatorio.modelo.Bonificacion;
import dda.obligatorio.modelo.Fachada;
import dda.obligatorio.modelo.Notificacion;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Puesto;
import dda.obligatorio.modelo.Tarifa;
import dda.obligatorio.modelo.Transito;
import dda.obligatorio.modelo.Vehiculo;
import dda.obligatorio.modelo.EstadoPropietario;

@RestController
@RequestMapping("/emular")
public class ControladorEmularTransito {
    private Fachada fachada;
    
    public ControladorEmularTransito() {
        this.fachada = Fachada.getInstancia();
    }

    @PostMapping("/obtenerPuestos")
    public List<Respuesta> obtenerPuestos() {
        List<Respuesta> respuestas = new ArrayList<>();
        List<Puesto> puestos = fachada.obtenerTodosPuestos();
        respuestas.add(new Respuesta("puestos", puestos));
        return respuestas;
    }

    @PostMapping("/obtenerTarifasPuesto")
    public List<Respuesta> obtenerTarifasPuesto(@RequestParam String nombrePuesto) {
        List<Respuesta> respuestas = new ArrayList<>();
        
        Puesto puesto = fachada.buscarPuestoPorNombre(nombrePuesto);
        if (puesto == null) {
            respuestas.add(new Respuesta("error", "Puesto no encontrado"));
            return respuestas;
        }
        
        List<Tarifa> tarifas = puesto.getTarifas();
        respuestas.add(new Respuesta("tarifas", tarifas));
        respuestas.add(new Respuesta("puesto", puesto));
        
        return respuestas;
    }

    @PostMapping("/emularTransito")
    public List<Respuesta> emularTransito(
            @RequestParam String cedulaPropietario,
            @RequestParam String nombrePuesto, 
            @RequestParam String matricula, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaHora) {
        
        Propietario propietario = fachada.buscarPropietario(cedulaPropietario);
        if (propietario == null) {
            List<Respuesta> respuestas = new ArrayList<>();
            respuestas.add(new Respuesta("error", "No se encontró el propietario con cédula " + cedulaPropietario));
            return respuestas;
        }
        return procesarEmulacion(propietario, nombrePuesto, matricula, fechaHora);
    }

    @PostMapping("/emularTransitoSimple")
    public List<Respuesta> emularTransitoSimple(
            @RequestParam String nombrePuesto,
            @RequestParam String matricula,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date fechaHora) {
        Propietario propietario = fachada.buscarPropietarioPorMatricula(matricula);
        if (propietario == null) {
            List<Respuesta> respuestas = new ArrayList<>();
            respuestas.add(new Respuesta("error", "No existe el vehículo"));
            return respuestas;
        }
        return procesarEmulacion(propietario, nombrePuesto, matricula, fechaHora);
    }

    private List<Respuesta> procesarEmulacion(Propietario propietario, String nombrePuesto, String matricula, Date fechaHora) {
        List<Respuesta> respuestas = new ArrayList<>();
        SimpleDateFormat sdfNotif = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Verificar que el vehículo pertenezca al propietario
        Vehiculo vehiculo = propietario.buscarVehiculoPorMatricula(matricula);
        if (vehiculo == null) {
            respuestas.add(new Respuesta("error", "No existe el vehículo"));
            return respuestas;
        }

        Puesto puesto = fachada.buscarPuestoPorNombre(nombrePuesto);
        if (puesto == null) {
            respuestas.add(new Respuesta("error", "No se encontró el puesto"));
            return respuestas;
        }

        Tarifa tarifa = puesto.obtenerTarifa(vehiculo.getCategoria());
        if (tarifa == null) {
            respuestas.add(new Respuesta("error", "No hay tarifa para esta categoría de vehículo en el puesto"));
            return respuestas;
        }
        double montoBase = tarifa.getMonto();

        if (!propietario.tieneSaldoSuficiente(montoBase)) {
            respuestas.add(new Respuesta("error", "Saldo insuficiente:" + propietario.getSaldoActual()));
            return respuestas;
        }

        // Validación de estado vía State (sin instanceof)
        EstadoPropietario estadoObj = propietario.getEstadoActual();
        if (!estadoObj.puedeRegistrarTransito()) {
            respuestas.add(new Respuesta("error", estadoObj.getMensajeTransitoDenegado()));
            return respuestas;
        }

        Transito transito = new Transito(fechaHora, vehiculo, puesto);

        Bonificacion bonificacion = propietario.obtenerBonificacionParaPuesto(puesto);
        String mensajeBonif = "";
        double montoFinal = montoBase;

        if (bonificacion != null && propietario.aplicaDescuentos()) {
            if (bonificacion.aplica(vehiculo, puesto, propietario.getTransitos())) {
                montoFinal = bonificacion.aplicarDescuento(montoBase, vehiculo, puesto, propietario.getTransitos());
                transito.setMontoFinal(montoFinal);
                mensajeBonif = bonificacion.getNombre();
            }
        } else {
            transito.setMontoFinal(montoBase);
        }

        propietario.descontarSaldo(montoFinal);
        propietario.agregarTransito(transito);

        if (propietario.registraNotificaciones()) {
            String mensaje = sdfNotif.format(new Date()) + " Pasaste por el puesto " + nombrePuesto + " con el vehículo " + matricula;
            Notificacion notif = new Notificacion(new Date(), mensaje, "Tránsito");
            propietario.agregarNotificacion(notif);
        }

        if (propietario.getSaldoActual() < propietario.getSaldoMinimo() && propietario.registraNotificaciones()) {
            String mensajeAlerta = sdfNotif.format(new Date()) + " Tu saldo actual es de $" + propietario.getSaldoActual() + " Te recomendamos hacer una recarga";
            Notificacion alerta = new Notificacion(new Date(), mensajeAlerta, "Alerta de Saldo");
            propietario.agregarNotificacion(alerta);
        }

        respuestas.add(new Respuesta("exito", "Tránsito emulado exitosamente"));
        respuestas.add(new Respuesta("propietario", propietario.getNombreCompleto()));
        respuestas.add(new Respuesta("estado", propietario.getEstadoActual().getNombre()));
        respuestas.add(new Respuesta("categoria", vehiculo.getCategoria().getNombre()));
        respuestas.add(new Respuesta("bonificacion", mensajeBonif.isEmpty() ? "-" : mensajeBonif));
        respuestas.add(new Respuesta("montoFinal", montoFinal));
        respuestas.add(new Respuesta("saldoActual", propietario.getSaldoActual()));
        return respuestas;
    }

}
