//Programacion3
//CarlosRamos
//0905-23-14141

package producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import producer.config.Configuracion;
import producer.model.Transaccion;

// Se encarga de conectarse a RabbitMQ y publicar mensajes en las colas
public class RabbitMQService {

    private Connection connection;
    private Channel channel;
    private final ObjectMapper objectMapper;

    public RabbitMQService() {
        this.objectMapper = new ObjectMapper();
    }

    // Abre la conexion con RabbitMQ
    public void conectar() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configuracion.RABBITMQ_HOST);
        factory.setPort(Configuracion.RABBITMQ_PORT);
        factory.setUsername(Configuracion.RABBITMQ_USUARIO);
        factory.setPassword(Configuracion.RABBITMQ_PASSWORD);

        this.connection = factory.newConnection();
        this.channel    = connection.createChannel();
        System.out.println("[ProducerCR] Conectado a RabbitMQ.");
    }

    public void publicar(Transaccion transaccion) throws Exception {
        String nombreCola = transaccion.getBancoDestino();

        channel.queueDeclare(nombreCola, true, false, false, null);

        String json = objectMapper.writeValueAsString(transaccion);
        channel.basicPublish("", nombreCola, null, json.getBytes("UTF-8"));

        System.out.println("[ProducerCR] -> Cola [" + nombreCola + "] | "
                + transaccion.getIdTransaccion());
    }

    public void cerrar() {
        try {
            if (channel != null && channel.isOpen())     channel.close();
            if (connection != null && connection.isOpen()) connection.close();
            System.out.println("[ProducerCR] Conexion RabbitMQ cerrada.");
        } catch (Exception e) {
            System.err.println("[ProducerCR] Error cerrando conexion: " + e.getMessage());
        }
    }
}

