package dda.obligatorio.modelo;

import java.util.ArrayList;
import java.util.List;

public class SistemaGestionAcceso {
    private ArrayList<Administrador> administradores;
    private ArrayList<Propietario> propietarios;
    
    public SistemaGestionAcceso() {
        this.administradores = new ArrayList<>();
        this.propietarios = new ArrayList<>();
    }

    public void logout(Usuario u){
        // Implement logout logic if needed, or leave empty if not applicable
    }

     public boolean registrarAdministrador(String cedula, String contrasena, String nombreCompleto) {
        if (existeUsuario(cedula)) {
            return false;
        }
        
        Administrador nuevo = new Administrador(cedula, contrasena, nombreCompleto);
        administradores.add(nuevo);
        return true;
    }

    public Administrador loginAdministrador(String cedula, String pwd) throws PeajeException {
        Administrador admin = (Administrador) login(cedula, pwd, administradores);
         if(admin==null) throw new PeajeException("Login incorrecto");
       return admin;
    }

    private Usuario login(String cedula, String pwd, ArrayList lista){
        Usuario usuario;
        
        for(Object o:lista){
            usuario = (Usuario)o;
            if(usuario.getCedula().equals(cedula) && usuario.getPassword().equals(pwd)){
                return usuario;
            }
        }
        return null;
    }

    public Administrador buscarAdministrador(String cedula) {
        for (Administrador admin : administradores) {
            if (admin.getCedula().equals(cedula)) {
                return admin;
            }
        }
        return null;
    }

    public List<Administrador> obtenerAdministradores() {
        return new ArrayList<>(administradores);
    }

    public boolean registrarPropietario(String cedula, String contrasena, String nombreCompleto,
                                       double saldoActual, double saldoMinimo) {
        if (existeUsuario(cedula)) {
            return false;
        }
        
        Propietario nuevo = new Propietario(cedula, contrasena, nombreCompleto, 
                                             saldoActual, saldoMinimo);
        propietarios.add(nuevo);
        return true;
    }

    public boolean existeUsuario(String cedula) {
        return buscarAdministrador(cedula) != null || buscarPropietario(cedula) != null;
    }

    public Propietario loginPropietario(String cedula, String pwd) throws PeajeException {
         Propietario prop = (Propietario) login(cedula, pwd, propietarios);
         if(prop==null) throw new PeajeException("Acceso denegado");
         if(!prop.puedeLoguerse()){
             throw new PeajeException(prop.getEstadoActual().getMensajeLoginDenegado());
         }
        return prop;
    }

    public Propietario buscarPropietario(String cedula) {
        for (Propietario prop : propietarios) {
            if (prop.getCedula().equals(cedula)) {
                return prop;
            }
        }
        return null;
    }

    public List<Propietario> obtenerPropietarios() {
        return new ArrayList<>(propietarios);
    }

    

}
