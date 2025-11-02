package dda.obligatorio.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir/redireccionar a las vistas estáticas de login.
 */
@Controller
public class VistaController {

    @GetMapping("/login-admin")
    public String loginAdmin() {
        // Redirige a la copia estática en /static/login-admin.html
        return "redirect:/login-admin.html";
    }

    @GetMapping("/login-propietarios")
    public String loginPropietarios() {
        return "redirect:/login-propietarios.html";
    }
}
