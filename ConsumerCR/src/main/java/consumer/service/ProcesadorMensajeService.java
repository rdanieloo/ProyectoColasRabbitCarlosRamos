//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.service;

 // I (Interface Segregation): interfaz especifica para procesar un mensaje de RabbitMQ.

public interface ProcesadorMensajeService {


     //Procesa el JSON de una transaccion recibido desde RabbitMQ.
     //@param mensajeJson el mensaje en formato JSON
     //@return true si el mensaje fue procesado y guardado correctamente

    boolean procesar(String mensajeJson);
}
