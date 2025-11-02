/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dda.obligatorio.modelo;

import java.util.Date;

/**
 *
 * @author PC
 */
public class Sesion {
    
    private Date fechaIngreso = new Date();
    private Propietario usuario;

    public Sesion(Propietario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Propietario getUsuario() {
        return usuario;
    }
    
    
    
}
