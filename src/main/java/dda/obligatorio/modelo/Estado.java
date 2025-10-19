package dda.obligatorio.modelo;

public class Estado {
    private String nombre;
    
    // Constantes para los estados predefinidos
    public static final String HABILITADO = "Habilitado";
    public static final String DESHABILITADO = "Deshabilitado";
    public static final String SUSPENDIDO = "Suspendido";
    public static final String PENALIZADO = "Penalizado";

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado estado = (Estado) obj;
        return nombre.equals(estado.nombre);
    }
}
