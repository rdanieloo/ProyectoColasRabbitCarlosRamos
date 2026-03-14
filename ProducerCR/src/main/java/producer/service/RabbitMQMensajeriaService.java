//Programacion3
//CarlosRamos
//0905-23-14141

package producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import producer.config.RabbitMQConfig;
import producer.model.Transaccion;

// S (Single Responsibility): solo se encarga de publicar en RabbitMQ.
// D (Dependency Inversion): implementa la interfaz MensajeriaService.

public class RabbitMQMensajeriaService implements MensajeriaService {

    private final Connection connection;
    private final Channel channel;
    private final ObjectMapper objectMapper;

    public RabbitMQMensajeriaService() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfig.HOST);
        factory.setPort(RabbitMQConfig.PORT);
        factory.setUsername(RabbitMQConfig.USERNAME);
        factory.setPassword(RabbitMQConfig.PASSWORD);

        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        this.objectMapper = new ObjectMapper();

        System.out.println("[ProducerCR] Conexion con RabbitMQ establecida.");
    }

    @Override
    public void publicarTransaccion(Transaccion transaccion) throws Exception {
        String nombreCola = transaccion.getBancoDestino();

        channel.queueDeclare(
                nombreCola,
                true,   
                false,  
                false, 
                null
        );

        String mensajeJson = objectMapper.writeValueAsString(transaccion);

        channel.basicPublish(
                "",          
                nombreCola,  
                null,
                mensajeJson.getBytes("UTF-8")
        );

        System.out.println("[ProducerCR] Enviado a cola [" + nombreCola + "]: "
                + transaccion.getIdTransaccion());
    }

    @Override
    public void cerrarConexion() {
        try {
            if (channel != null && channel.isOpen()) channel.close();
            if (connection != null && connection.isOpen()) connection.close();
            System.out.println("[ProducerCR] Conexion RabbitMQ cerrada.");
        } catch (Exception e) {
            System.err.println("[ProducerCR] Error al cerrar conexion: " + e.getMessage());
        }
    }
}
