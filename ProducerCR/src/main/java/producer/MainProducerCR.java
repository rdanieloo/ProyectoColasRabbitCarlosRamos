//Programacion3
//CarlosRamos
//0905-23-14141

package producer;

import producer.model.LoteTransacciones;
import producer.model.Transaccion;
import producer.service.ApiService;
import producer.service.RabbitMQService;


public class MainProducerCR {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("       ProducerCR - Iniciando...             ");
        System.out.println("==============================================");

        ApiService    apiService    = new ApiService();
        RabbitMQService rabbitService = new RabbitMQService();

        try {

            LoteTransacciones lote = apiService.obtenerTransacciones();

            if (lote.getTransacciones() == null || lote.getTransacciones().isEmpty()) {
                System.out.println("[ProducerCR] No hay transacciones en el lote.");
                return;
            }

            rabbitService.conectar();

            int enviados = 0;
            int errores  = 0;

            for (Transaccion transaccion : lote.getTransacciones()) {
                try {
                    rabbitService.publicar(transaccion);
                    enviados++;
                } catch (Exception e) {
                    errores++;
                    System.err.println("[ProducerCR] Upss Error publicando "
                            + transaccion.getIdTransaccion() + ": " + e.getMessage());
                }
            }

            System.out.println("==============================================");
            System.out.println("[ProducerCR] Proceso completado.");
            System.out.println("[ProducerCR] Enviados : " + enviados);
            System.out.println("[ProducerCR] Errores  : " + errores);
            System.out.println("==============================================");

        } catch (Exception e) {
            System.err.println("[ProducerCR] Upss :( Error: " + e.getMessage());
            System.err.println("  -> Verifica que RabbitMQ este corriendo en localhost:5672");
            e.printStackTrace();
        } finally {
            rabbitService.cerrar();
        }
    }
}
