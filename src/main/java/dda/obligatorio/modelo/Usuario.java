package dda.obligatorio.modelo;

public abstract class Usuario {
    private String cedula;
    private String password;
    private String nombreCompleto;

    public Usuario(String cedula, String password, String nombreCompleto) {
        this.cedula = cedula;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
    }

    public String getCedula() {
        return cedula;
    }
    
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
    
    public String getContrasena() {
        return password;
    }
    
    public void setContrasena(String password) {
        this.password = password;
    }
    
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public abstract String getTipoUsuario();

     @Override
    public String toString() {
        return getTipoUsuario() + " [" + cedula + ", " + nombreCompleto + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof Usuario)) return false;
        Usuario usuario = (Usuario) obj;
        return cedula.equals(usuario.cedula);
    }
}
