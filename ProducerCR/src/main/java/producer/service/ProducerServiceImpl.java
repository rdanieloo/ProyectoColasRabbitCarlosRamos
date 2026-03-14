//Programacion3
//CarlosRamos
//0905-23-14141

package producer.service;

import producer.model.LoteTransacciones;
import producer.model.Transaccion;
import producer.repository.TransaccionRepository;

// S (Single Responsibility): solo coordina el flujo, no hace HTTP ni RabbitMQ directamente.
// D (Dependency Inversion): depende de interfaces, no de implementaciones concretas.

public class ProducerServiceImpl implements ProducerService {

    private final TransaccionRepository transaccionRepository;
    private final MensajeriaService mensajeriaService;

    // Inyeccion de dependencias por constructor (principio D de SOLID)
    public ProducerServiceImpl(TransaccionRepository transaccionRepository,
                               MensajeriaService mensajeriaService) {
        this.transaccionRepository = transaccionRepository;
        this.mensajeriaService = mensajeriaService;
    }

    @Override
    public void ejecutar() {
        System.out.println("=================================================");
        System.out.println("    ProducerCR - Iniciando procesamiento...");
        System.out.println("=================================================");

        try {

            LoteTransacciones lote = transaccionRepository.obtenerTransacciones();

            if (lote.getTransacciones() == null || lote.getTransacciones().isEmpty()) {
                System.out.println("[ProducerCR] No hay transacciones en el lote.");
                return;
            }

            int enviados = 0;
            int errores = 0;


            for (Transaccion transaccion : lote.getTransacciones()) {
                try {
                    mensajeriaService.publicarTransaccion(transaccion);
                    enviados++;
                } catch (Exception e) {
                    errores++;
                    System.err.println("[ProducerCR] Error publicando transaccion "
                            + transaccion.getIdTransaccion() + ": " + e.getMessage());
                }
            }

            System.out.println("=================================================");
            System.out.println("[ProducerCR] Proceso finalizado.");
            System.out.println("[ProducerCR] Total enviados : " + enviados);
            System.out.println("[ProducerCR] Total con error: " + errores);
            System.out.println("=================================================");

        } catch (Exception e) {
            System.err.println("[ProducerCR] Upss Error critico en el proceso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            mensajeriaService.cerrarConexion();
        }
    }
}

