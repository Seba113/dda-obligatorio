package dda.obligatorio.modelo;

public class Bonificacion {

    private String nombre;
    
    // Constantes para los tipos de bonificación
    public static final String EXONERADOS = "Exonerados";
    public static final String FRECUENTES = "Frecuentes";
    public static final String TRABAJADORES = "Trabajadores";

    public Bonificacion(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double aplicarDescuento(double montoBase) {
        switch (this.nombre) {
            case EXONERADOS:
                return 0; // No pagan
            case FRECUENTES:
                return montoBase * 0.5; // 50% de descuento
            case TRABAJADORES:
                return montoBase * 0.2; // 80% de descuento
            default:
                return montoBase;
        }
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Bonificacion bonif = (Bonificacion) obj;
        return nombre.equals(bonif.nombre);
    }
    
}
