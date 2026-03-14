//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.config;

// S (Responsabilidad Unica): solo contiene configuracion.

public class AppConfig {

    public static final String RABBITMQ_HOST     = "localhost";
    public static final int    RABBITMQ_PORT     = 5672;
    public static final String RABBITMQ_USERNAME = "guest";
    public static final String RABBITMQ_PASSWORD = "guest";

    public static final String API_POST_URL =
        "https://7e0d9ogwzd.execute-api.us-east-1.amazonaws.com/default/guardarTransacciones";

    // Datos mios que se agregan en el Consumer 
    public static final String ESTUDIANTE_NOMBRE = "Carlos Daniel Ramos Moran";
    public static final String ESTUDIANTE_CARNET = "0905-23-14141";

    public static final String[] BANCOS = {
        "BANRURAL", "BAC", "GYT", "INDUSTRIAL", "OCCIDENTE",
        "AGROMERCANTIL", "REFORMADOR", "INMOBILIARIO", "VIVIBANCO", "PROMERICA"
    };

    public static final int MAX_REINTENTOS = 3;

    private AppConfig() {}
}
