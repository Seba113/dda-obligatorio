package dda.obligatorio.modelo;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Clase Fachada que implementa el patrón Facade para proporcionar una interfaz unificada
 * a los subsistemas de gestión del peaje.
 */
public class Fachada {
    private static Fachada instancia;
    private final SistemaGestionAcceso sistemaAcceso;
    private final SistemaGestionPeaje sistemaPeaje;
    private final SistemaGestionTransitos sistemaTransitos;

    // Constructor privado para el Singleton
    private Fachada() {
        this.sistemaAcceso = new SistemaGestionAcceso();
        this.sistemaPeaje = new SistemaGestionPeaje();
        this.sistemaTransitos = new SistemaGestionTransitos();
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

    public List<Bonificacion> obtenerTodasBonificaciones() {
        return sistemaPeaje.obtenerTodasBonificaciones();
    }

    public double calcularMontoConBonificacion(double montoBase, Bonificacion bonificacion,
                                             Vehiculo vehiculo, Puesto puesto,
                                             List<Transito> transitosAnteriores) {
        return sistemaPeaje.calcularMontoConBonificacion(montoBase, bonificacion, vehiculo, 
                                                       puesto, transitosAnteriores);
    }

    // Métodos delegados de SistemaGestionTransitos
    public List<Transito> obtenerTodosTransitos() {
        return sistemaTransitos.obtenerTodosTransitos();
    }

    public List<Transito> obtenerTransitosPorVehiculo(String matricula) {
        return sistemaTransitos.obtenerTransitosPorVehiculo(matricula);
    }

    public List<Transito> obtenerTransitosPorFecha(Date fecha) {
        return sistemaTransitos.obtenerTransitosPorFecha(fecha);
    }

    public int obtenerCantidadTransitos() {
        return sistemaTransitos.obtenerCantidadTransitos();
    }

    public double calcularTotalRecaudado() {
        return sistemaTransitos.calcularTotalRecaudado();
    }

    public List<Transito> listarTransitosPropietario(String cedula) {
        Propietario propietario = sistemaAcceso.buscarPropietario(cedula);
        return sistemaTransitos.obtenerTransitosPorPropietario(propietario);
    }
        
    protected SistemaGestionAcceso getSistemaAcceso() {
        return sistemaAcceso;
    }

    protected SistemaGestionPeaje getSistemaPeaje() {
        return sistemaPeaje;
    }

    protected SistemaGestionTransitos getSistemaTransitos() {
        return sistemaTransitos;
    }

    public void logout(Usuario u) {
        sistemaAcceso.logout(u);
    }
}