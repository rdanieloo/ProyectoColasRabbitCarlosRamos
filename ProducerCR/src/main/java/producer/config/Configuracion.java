//Programacion3
//CarlosRamos
//0905-23-14141

package producer.config;

// Configuracion de conexion a RabbitMQ y URL de la API GET

public class Configuracion {

    public static final String RABBITMQ_HOST     = "localhost";
    public static final int    RABBITMQ_PORT     = 5672;
    public static final String RABBITMQ_USUARIO  = "guest";
    public static final String RABBITMQ_PASSWORD = "guest";

    public static final String API_GET_URL =
        "https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones";

    private Configuracion() {}
}
