package dda.obligatorio.modelo;

import java.util.List;
import java.util.ArrayList;

/**
 * Clase Fachada que implementa el patrón Facade para proporcionar una interfaz unificada
 * a los subsistemas de gestión del peaje.
 */
public class Fachada {
    private static Fachada instancia;
    private final SistemaGestionAcceso sistemaAcceso;
    private final SistemaGestionPeaje sistemaPeaje;

    // Constructor privado para el Singleton
    private Fachada() {
        this.sistemaAcceso = new SistemaGestionAcceso();
        this.sistemaPeaje = new SistemaGestionPeaje();
    }

    // Método para obtener la instancia única (Singleton)
    public static Fachada getInstancia() {
        if (instancia == null) {
            instancia = new Fachada();
        }
        return instancia;
    }

    // Métodos delegados de SistemaGestionAcceso
    public Administrador loginAdministrador(String nombre, String password) throws PeajeException {
        return sistemaAcceso.loginAdministrador(nombre, password);
    }

    public Propietario loginPropietario(String nombre, String password) throws PeajeException {
        return sistemaAcceso.loginPropietario(nombre, password);
    }

    public boolean registrarAdministrador(String cedula, String contrasena, String nombreCompleto) {
        return sistemaAcceso.registrarAdministrador(cedula, contrasena, nombreCompleto);
    }

    public boolean registrarPropietario(String cedula, String contrasena, String nombreCompleto, 
                                      double saldoActual, double saldoMinimo) {
        return sistemaAcceso.registrarPropietario(cedula, contrasena, nombreCompleto, 
                                                saldoActual, saldoMinimo);
    }

    public List<Administrador> obtenerAdministradores() {
        return sistemaAcceso.obtenerAdministradores();
    }

    public List<Propietario> obtenerPropietarios() {
        return sistemaAcceso.obtenerPropietarios();
    }

    public Propietario buscarPropietario(String cedula) {
        return sistemaAcceso.buscarPropietario(cedula);
    }

    public boolean existeUsuario(String cedula) {
        return sistemaAcceso.existeUsuario(cedula);
    }

    // Métodos delegados de SistemaGestionPeaje
    public List<Puesto> obtenerTodosPuestos() {
        return sistemaPeaje.obtenerTodosPuestos();
    }

    public List<Categoria> obtenerTodasCategorias() {
        return sistemaPeaje.obtenerTodasCategorias();
    }

    public void agregarCategoria(Categoria categoria) {
        sistemaPeaje.agregarCategoria(categoria);
    }

    public List<Bonificacion> obtenerTodasBonificaciones() {
        return sistemaPeaje.obtenerTodasBonificaciones();
    }

    public void agregarBonificacion(Bonificacion bonificacion) {
        sistemaPeaje.agregarBonificacion(bonificacion);
    }

    public double calcularMontoConBonificacion(double montoBase, Bonificacion bonificacion,
                                             Vehiculo vehiculo, Puesto puesto,
                                             List<Transito> transitosAnteriores) {
        return sistemaPeaje.calcularMontoConBonificacion(montoBase, bonificacion, vehiculo, 
                                                       puesto, transitosAnteriores);
    }

    public List<Transito> listarTransitosPropietario(String cedula) {
        Propietario propietario = sistemaAcceso.buscarPropietario(cedula);
        return propietario != null ? propietario.getTransitos() : new ArrayList<>();
    }

    public void agregarPuesto(Puesto puesto) {
        sistemaPeaje.agregarPuesto(puesto);
    }

    public Puesto buscarPuestoPorNombre(String nombrePuesto) {
        return sistemaPeaje.buscarPuestoPorNombre(nombrePuesto);
    }
    
    public Bonificacion buscarBonificacionPorNombre(String nombreBonificacion) {
        return sistemaPeaje.buscarBonificacionPorNombre(nombreBonificacion);
    }
        

    public void logout(Usuario u) {
        sistemaAcceso.logout(u);
    }

    public void borrarNotificacionesPropietario(String cedula) throws PeajeException {
        Propietario propietario = buscarPropietario(cedula);
        if (propietario == null) {
            throw new PeajeException("Propietario no encontrado");
        }
        propietario.limpiarNotificaciones();
    }

    public Vehiculo buscarVehiculoPorMatricula(String cedula, String matricula) {
        Propietario propietario = buscarPropietario(cedula);
        return propietario != null ? propietario.buscarVehiculoPorMatricula(matricula) : null;
    }

    // Buscar propietario por matrícula (delegado a acceso)
    public Propietario buscarPropietarioPorMatricula(String matricula) {
        return sistemaAcceso.buscarPropietarioPorMatricula(matricula);
    }
}