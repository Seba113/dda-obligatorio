/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/SpringFramework/RestController.java to edit this template
 */
package dda.obligatorio;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 *
 * @author PC
 */

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConexionNavegador  {

    private SseEmitter conexionSSE;

    public void conectarSSE() {
        if (conexionSSE != null) { //si hay hay una conexion la cierro
            cerrarConexion();
        }
        conexionSSE = new SseEmitter();
    }
    public void cerrarConexion(){
        try{
            if(conexionSSE!=null){
                conexionSSE.complete();
                conexionSSE = null;
            }
        }catch(Exception e){}
    }

    public SseEmitter getConexionSSE() {
        return conexionSSE;
    }
     
    public void enviarJSON(Object informacion) {
        try {
            String json = new ObjectMapper().writeValueAsString(informacion);
            enviarMensaje(json);
   
        } catch (JsonProcessingException e) {
            System.out.println("Error al convertir a JSON:" + e.getMessage());
           
        }
   
    }
    public void enviarMensaje(String mensaje) {
       
	if(conexionSSE==null) return;
        try {	
	     	conexionSSE.send(mensaje);
						
	} catch (Throwable e) {
            System.out.println("Error al enviar mensaje:" + e.getMessage());
            cerrarConexion();
	}
    }
    
  

}
