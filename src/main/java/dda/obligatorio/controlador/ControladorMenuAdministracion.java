package dda.obligatorio.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import dda.obligatorio.ConexionNavegador;
import jakarta.servlet.http.HttpSession;
import dda.obligatorio.modelo.Administrador;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/menu-administracion")
@Scope("session")
public class ControladorMenuAdministracion {
    private final ConexionNavegador conexionNavegador;

    public ControladorMenuAdministracion(@Autowired ConexionNavegador conexionNavegador) {
        this.conexionNavegador = conexionNavegador;
    }

    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter registrarSSE() {
        return conexionNavegador.conectarSSE(); 
    }

    @PostMapping("/vistaConectada")
    public List<Respuesta> vistaConectada(HttpSession session) {
        Administrador admin = (Administrador) session.getAttribute("usuarioAdmin");
        if (admin == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay administrador en la sesión");
        }
        return Respuesta.lista(new Respuesta("nombreCompleto", admin.getNombreCompleto()));
    }
}
