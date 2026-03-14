//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import consumer.config.Configuracion;
import consumer.model.Transaccion;
import java.util.UUID;

public class RabbitMQConsumerService {

    private final ApiPostService apiPostService;
    private final ObjectMapper   objectMapper;

    public RabbitMQConsumerService(ApiPostService apiPostService) {
        this.apiPostService = apiPostService;
        this.objectMapper   = new ObjectMapper();
    }

    public void iniciarEscucha() throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configuracion.RABBITMQ_HOST);
        factory.setPort(Configuracion.RABBITMQ_PORT);
        factory.setUsername(Configuracion.RABBITMQ_USUARIO);
        factory.setPassword(Configuracion.RABBITMQ_PASSWORD);

        Connection connection = factory.newConnection();
        Channel    channel    = connection.createChannel();

        channel.basicQos(1);

        System.out.println("==============================================");
        System.out.println("      ConsumerCR - Escuchando colas...       ");
        System.out.println("==============================================");

        for (String banco : Configuracion.BANCOS) {

            channel.queueDeclare(banco, true, false, false, null);
            System.out.println("[ConsumerCR] Escuchando cola: " + banco);

            DeliverCallback callback = (consumerTag, delivery) -> {
                long   deliveryTag = delivery.getEnvelope().getDeliveryTag();
                String mensajeJson = new String(delivery.getBody(), "UTF-8");

                System.out.println("\n[ConsumerCR] Mensaje recibido de cola [" + banco + "]");

                try {
                    Transaccion transaccion = objectMapper.readValue(mensajeJson, Transaccion.class);

                    String idOriginal    = transaccion.getIdTransaccion();
                    String uuid          = UUID.randomUUID().toString();
                    String idModificado  = idOriginal + "-" + uuid;
                    transaccion.setIdTransaccion(idModificado);

                    transaccion.setNombre(Configuracion.ESTUDIANTE_NOMBRE);
                    transaccion.setCarnet(Configuracion.ESTUDIANTE_CARNET);
                    transaccion.setCorreo(Configuracion.ESTUDIANTE_CORREO);

                    System.out.println("[ConsumerCR] ID modificado: " + idModificado);

                    boolean exito = apiPostService.guardarTransaccion(transaccion);

                    if (exito) {

                        channel.basicAck(deliveryTag, false);
                        System.out.println("[ConsumerCR] ACK enviado. Transaccion guardada.");
                    } else {

                        channel.basicNack(deliveryTag, false, true);
                        System.err.println("[ConsumerCR] NACK - mensaje reencolado en [" + banco + "]");
                    }

                } catch (Exception e) {
                    System.err.println("[ConsumerCR] Upss Error procesando mensaje: " + e.getMessage());

                    channel.basicNack(deliveryTag, false, true);
                }
            };

            channel.basicConsume(banco, false, callback, tag -> {});
        }

        System.out.println("\n[ConsumerCR] Esperando mensajes... (Ctrl+C para detener)\n");

        Thread.currentThread().join();
    }
}
