//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.service;

 //I (Interface Segregation): interfaz especifica para escuchar colas de RabbitMQ.
 
public interface ConsumerService {

    void iniciarEscucha() throws Exception;
}
