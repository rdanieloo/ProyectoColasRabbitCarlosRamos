//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import consumer.config.AppConfig;
import consumer.model.Transaccion;
import consumer.repository.GuardarTransaccionRepository;

//Deserializa el JSON, agrega nombre/carnet y llama al POST con reintentos.
// S (Single Responsibility): solo procesa el mensaje recibido de RabbitMQ.
//D (Dependency Inversion): depende de la interfaz GuardarTransaccionRepository.

public class ProcesadorMensajeServiceImpl implements ProcesadorMensajeService {

    private final GuardarTransaccionRepository guardarRepository;
    private final ObjectMapper objectMapper;

    public ProcesadorMensajeServiceImpl(GuardarTransaccionRepository guardarRepository) {
        this.guardarRepository = guardarRepository;
        this.objectMapper      = new ObjectMapper();
    }

    @Override
    public boolean procesar(String mensajeJson) {
        try {

            Transaccion transaccion = objectMapper.readValue(mensajeJson, Transaccion.class);


            transaccion.setNombre(AppConfig.ESTUDIANTE_NOMBRE);
            transaccion.setCarnet(AppConfig.ESTUDIANTE_CARNET);

            System.out.println("[ConsumerCR] Procesando: " + transaccion.getIdTransaccion()
                    + " | Banco: " + transaccion.getBancoDestino());

            for (int intento = 1; intento <= AppConfig.MAX_REINTENTOS; intento++) {
                boolean exito = guardarRepository.guardar(transaccion);

                if (exito) {
                    System.out.println("[ConsumerCR] Guardado exitoso: "
                            + transaccion.getIdTransaccion()
                            + " (intento " + intento + ")");
                    return true;
                }

                System.err.println("[ConsumerCR] Intento " + intento + " fallido para "
                        + transaccion.getIdTransaccion()
                        + ". Reintentando...");

                if (intento < AppConfig.MAX_REINTENTOS) {
                    Thread.sleep(2000); // esperar 2 segundos antes de reintentar
                }
            }

            System.err.println("[ConsumerCR] Se agotaron los reintentos para: "
                    + transaccion.getIdTransaccion());
            return false;

        } catch (Exception e) {
            System.err.println("[ConsumerCR] Error al procesar mensaje: " + e.getMessage());
            return false;
        }
    }
}
