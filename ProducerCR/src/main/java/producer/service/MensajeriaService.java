//Programacion3
//CarlosRamos
//0905-23-14141

package producer.service;

import producer.model.Transaccion;

 // I (Interface Segregation): interfaz especifica solo para publicar mensajes.
 // O (Open/Closed): si se cambia de RabbitMQ a otro broker, solo cambia la implementacion.
 
public interface MensajeriaService {

    /**
     * Publica una transaccion en la cola correspondiente al banco destino.
     * @param transaccion la transaccion a publicar
     * @throws Exception si falla la conexion con RabbitMQ
     */
    void publicarTransaccion(Transaccion transaccion) throws Exception;

    void cerrarConexion();
}
