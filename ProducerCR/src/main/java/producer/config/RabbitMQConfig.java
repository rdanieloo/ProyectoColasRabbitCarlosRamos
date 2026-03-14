//Programacion3
//CarlosRamos
//0905-23-14141

package producer.config;


 //Clase de configuracion con los datos de conexion a RabbitMQ.
 //S (Single Responsibility): solo contiene configuracion, nada mas.

public class RabbitMQConfig {

    public static final String HOST     = "localhost";
    public static final int    PORT     = 5672;
    public static final String USERNAME = "guest";
    public static final String PASSWORD = "guest";

    // Constructor privado: esta clase no debe instanciarse
    private RabbitMQConfig() {}
}
