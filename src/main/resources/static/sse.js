/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */


//Se asume que esta incluida vistaWeb.js
//La pagina que incluya esta lib debe cargar esta variable 
//Para registrar SSE debe ejecutarse siempre antes el inicio de la vista 
var urlRegistroSSE = null;

//Esta funcion la llama vistaWeb.js al final del submit de inicio de la vista

function primerSubmitFinalizado(){
    registrarSSE();
}

function registrarSSE(){
    //LLamada al endpoint para recibir mensajes desde el servidor
    if (urlRegistroSSE !== null) {
        
        const eventSource = new EventSource(urlRegistroSSE,{withCredentials: true });
               
        //LLEGA UN MENSAJE DESDE EL SERVIDOR!
        eventSource.onmessage = function (event) {
            //Se asume que todos los mensajes llegan en formtato JSON
            json = JSON.parse(event.data); // Convertir el JSON a objeto
            procesarMensajeSSE(json); 
        };
        //ERROR EN LA CONEXION CON EL SERVIDOR
        eventSource.onerror = function (event) {
            // Cerrar la conexión actual
            try { eventSource.close(); } catch(e) {}
            // Si la página define un manejador, usarlo
            try {
                conexionSSECerrada(event);//Metodo opcional definido por la pagina
            } catch (e) {
                // Comportamiento por defecto: intentar reconectar automáticamente
                // Evitar borrar la página; reconectar en 3 segundos
                setTimeout(function(){
                    try { registrarSSE(); } catch(e) {}
                }, 3000);
            }
        };
          
    }
}
//Por defecto se asume que los mensajes se reciven via SSE tienen el mismo formato que las respuestas
//del submit. 
function procesarMensajeSSE(mensaje){
    alert("Mensaje recibido desde el servidor via SSE");
        procesarResultadosSubmit(mensaje);
}
