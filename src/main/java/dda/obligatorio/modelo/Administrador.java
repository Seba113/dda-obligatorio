package dda.obligatorio.modelo;

public class Administrador extends Usuario {

    public Administrador(String cedula, String contrasena, String nombreCompleto) {
        super(cedula, contrasena, nombreCompleto);
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }

    public boolean tienePermisosAdministrativos() {
        return true;
    }
}
