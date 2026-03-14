//Programacion3
//CarlosRamos
//0905-23-14141

package consumer;

import consumer.service.ApiPostService;
import consumer.service.RabbitMQConsumerService;

public class MainConsumerCR {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       ConsumerCR - Iniciando...             ");
        System.out.println("==============================================");

        try {
            ApiPostService         apiPostService  = new ApiPostService();
            RabbitMQConsumerService consumerService = new RabbitMQConsumerService(apiPostService);

            consumerService.iniciarEscucha();

        } catch (Exception e) {
            System.err.println("[ConsumerCR] Error al iniciar: " + e.getMessage());
            System.err.println("  -> Verifica que RabbitMQ este corriendo en localhost:5672");
            e.printStackTrace();
        }
    }
}
