package dda.obligatorio.modelo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Componente que inicializa datos de prueba usando la Fachada.
 * Crea 4 registros de cada entidad principal para facilitar pruebas locales.
 */
@Component
public class DatosPrueba {

    @PostConstruct
    public void inicializar() {
        Fachada fachada = Fachada.getInstancia();

        // 4 administradores
        fachada.registrarAdministrador("12345678", "admin.123", "Usuario Administrador");
        fachada.registrarAdministrador("10000002", "admin2", "Admin Dos");
        fachada.registrarAdministrador("10000003", "admin3", "Admin Tres");
        fachada.registrarAdministrador("10000004", "admin4", "Admin Cuatro");

        // 4 propietarios
        fachada.registrarPropietario("23456789", "prop.123", "Usuario Propietario", 2000.0, 500.0);
        fachada.registrarPropietario("20000002", "prop2", "Propietario Dos", 1500.0, 200.0);
        fachada.registrarPropietario("20000003", "prop3", "Propietario Tres", 800.0, 100.0);
        fachada.registrarPropietario("20000004", "prop4", "Propietario Cuatro", 4000.0, 500.0);
        
        // Obtener referencias a propietarios
        Propietario prop1 = fachada.buscarPropietario("23456789");
        Propietario prop2 = fachada.buscarPropietario("20000002");

        // Configurar propietario 2 como deshabilitado
        if (prop2 != null) {
            prop2.setEstadoActual(new Deshabilitado());
        }

        // 4 categorías
        Categoria cat1 = new Categoria("Auto");
        Categoria cat2 = new Categoria("Camioneta");
        Categoria cat3 = new Categoria("Camión");
        Categoria cat4 = new Categoria("Moto");

        // Añadir categorías al sistema de peaje
        fachada.agregarCategoria(cat1);
        fachada.agregarCategoria(cat2);
        fachada.agregarCategoria(cat3);
        fachada.agregarCategoria(cat4);

        // 4 puestos
        Puesto p1 = new Puesto("Peaje Norte", "Av. Norte 123");
        Puesto p2 = new Puesto("Peaje Sur", "Av. Sur 456");
        Puesto p3 = new Puesto("Peaje Este", "Av. Este 789");
        Puesto p4 = new Puesto("Peaje Oeste", "Av. Oeste 321");

        fachada.agregarPuesto(p1);
        fachada.agregarPuesto(p2);
        fachada.agregarPuesto(p3);
        fachada.agregarPuesto(p4);

        // 4 tarifas (asignadas a puestos para distintas categorías)
        Tarifa t1 = new Tarifa(120.0, cat1);
        Tarifa t2 = new Tarifa(150.0, cat2);
        Tarifa t3 = new Tarifa(300.0, cat3);
        Tarifa t4 = new Tarifa(40.0, cat4);

        p1.agregarTarifa(t1); p1.agregarTarifa(t2);
        p2.agregarTarifa(t1); p2.agregarTarifa(t3);
        p3.agregarTarifa(t2); p3.agregarTarifa(t4);
        p4.agregarTarifa(t1); p4.agregarTarifa(t4);

        // 4 bonificaciones
        Bonificacion b1 = new BonificacionFrecuentes();
        Bonificacion b2 = new BonificacionExonerados();
        Bonificacion b3 = new BonificacionTrabajadores();
        Bonificacion b4 = new Bonificacion("Bonificación Promo") {
            @Override
            public double aplicarDescuento(double montoBase, Vehiculo vehiculo, Puesto puesto, java.util.List<Transito> transitosAnteriores) {
                return montoBase * 0.1; // 10% promocional
            }

            @Override
            public boolean aplica(Vehiculo vehiculo, Puesto puesto, java.util.List<Transito> transitosAnteriores) {
                return true; // siempre aplica (solo para pruebas)
            }
        };

        fachada.agregarBonificacion(b1);
        fachada.agregarBonificacion(b2);
        fachada.agregarBonificacion(b3);
        fachada.agregarBonificacion(b4);

        // Asignar bonificaciones al propietario 1 ANTES de crear vehículos
        if (prop1 != null) {
            Calendar cal = Calendar.getInstance();
            Date fechaAsignacion = cal.getTime();
            
            prop1.agregarAsignacion(new Asignacion(fechaAsignacion, b1, p1));
            prop1.agregarAsignacion(new Asignacion(fechaAsignacion, b2, p2));
            prop1.agregarAsignacion(new Asignacion(fechaAsignacion, b3, p3));
            prop1.agregarAsignacion(new Asignacion(fechaAsignacion, b4, p4));
        }

        // Asignar vehículos a propietarios (4 vehículos)

        Vehiculo v1 = new Vehiculo("AAA111", "Sedan", "Azul", cat1);
        Vehiculo v2 = new Vehiculo("BBB222", "SUV", "Gris", cat2);
        Vehiculo v3 = new Vehiculo("CCC333", "Camion", "Rojo", cat3);
        Vehiculo v4 = new Vehiculo("DDD444", "Moto", "Negra", cat4);

        if (prop1 != null) {
            prop1.agregarVehiculo(v1);
            prop1.agregarVehiculo(v2);
            prop1.agregarVehiculo(v3);
            prop1.agregarVehiculo(v4);
        }

        // Crear 4 tránsitos y registrarlos
        Date ahora = new Date();
        Transito tr1 = new Transito(ahora, v1, p1);
        Transito tr2 = new Transito(ahora, v2, p2);
        Transito tr3 = new Transito(ahora, v3, p3);
        Transito tr4 = new Transito(ahora, v4, p4);

        // Aplicar bonificaciones a los tránsitos
        tr1.aplicarBonificacion(b1); 
        tr2.aplicarBonificacion(b2);
        tr3.aplicarBonificacion(b3); 
        tr4.aplicarBonificacion(b4); 

        if (prop1 != null) {
            prop1.agregarTransito(tr1);
            prop1.agregarTransito(tr2);
            prop1.agregarTransito(tr3);
            prop1.agregarTransito(tr4);
        }

        // Agregar notificaciones de prueba
        if (prop1 != null) {
            Calendar cal = Calendar.getInstance();
            Date hoy = cal.getTime();

            prop1.agregarNotificacion(new Notificacion(hoy, "Bienvenido al sistema de peaje", "Info"));
            prop1.agregarNotificacion(new Notificacion(hoy, "Su saldo actual es: $2000.00", "Info"));
            prop1.agregarNotificacion(new Notificacion(hoy, "Tiene 4 bonificaciones asignadas", "Info"));
            prop1.agregarNotificacion(new Notificacion(hoy, "Recuerde mantener su saldo por encima del mínimo", "Alerta"));
        }
    }
}
