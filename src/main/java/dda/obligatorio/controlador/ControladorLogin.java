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
    public List<Respuesta> loginPropietarios(HttpSession sesionHttp, @RequestParam String username, @RequestParam String password) throws PeajeException {
        
        //login al modelo
        Propietario p  = Fachada.getInstancia().loginPropietario(username, password);
        

        //si hay una sesion activa la cierro
        logoutPropietarios(sesionHttp);

        //guardo la sesion de la logica en la sesionHttp
        sesionHttp.setAttribute("usuarioPropietario", p);
    // devolver la URL destino para redirigir en la vista (por ejemplo, a la raíz)
    return Respuesta.lista(new Respuesta("loginExitoso", "/"));
    }

    @PostMapping("/loginAdmin")
    public List<Respuesta> loginAdministrador(HttpSession sesionHttp, @RequestParam String username, @RequestParam String password) throws PeajeException {
        //login al modelo
        Administrador admin = Fachada.getInstancia().loginAdministrador(username, password);
                
        //guardo el admin en la sesionHttp
        sesionHttp.setAttribute("usuarioAdmin", admin);
    // devolver la URL destino para redirigir en la vista (por ejemplo, a la raíz)
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

}
