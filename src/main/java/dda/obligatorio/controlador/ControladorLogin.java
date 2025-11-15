package dda.obligatorio.controlador;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import dda.obligatorio.modelo.Administrador;
import dda.obligatorio.modelo.PeajeException;
import dda.obligatorio.modelo.Propietario;
import dda.obligatorio.modelo.Fachada;


@RestController
@RequestMapping("/acceso")

public class ControladorLogin {

    @PostMapping("/loginPropietarios")
    public List<Respuesta> loginPropietarios(HttpSession sesionHttp, @RequestParam String cedula, @RequestParam String password) throws PeajeException {
        Propietario p  = Fachada.getInstancia().loginPropietario(cedula, password);
        logoutPropietarios(sesionHttp);
        sesionHttp.setAttribute("usuarioPropietario", p);
        return Respuesta.lista(new Respuesta("loginExitoso", "panel-propietarios.html"));
    }

    @PostMapping("/loginAdmin")
    public List<Respuesta> loginAdministrador(HttpSession sesionHttp, @RequestParam String cedula, @RequestParam String password) throws PeajeException {
        Administrador admin = Fachada.getInstancia().loginAdministrador(cedula, password);
        sesionHttp.setAttribute("usuarioAdmin", admin);
        return Respuesta.lista(new Respuesta("loginExitoso", "/"));
    }

    @PostMapping("/logoutPeaje")
    public List<Respuesta> logoutPropietarios(HttpSession sesionHttp) throws PeajeException {
        Propietario p = (Propietario)sesionHttp.getAttribute("usuarioPropietario");
        if(p!=null){
            Fachada.getInstancia().logout(p);
            sesionHttp.removeAttribute("usuarioPropietario");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login-propietarios.html"));

    }

    @PostMapping("/logoutAdministrador")
    public List<Respuesta> logoutAdministrador(HttpSession sesionHttp) throws PeajeException {
        Administrador a = (Administrador)sesionHttp.getAttribute("usuarioAdmin");
        if(a!=null){
            Fachada.getInstancia().logout(a);
            sesionHttp.removeAttribute("usuarioAdmin");
        }
        return Respuesta.lista(new Respuesta("paginaLogin", "login-admin.html"));

    }

}
