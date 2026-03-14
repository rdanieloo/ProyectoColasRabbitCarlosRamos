//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import consumer.config.Configuracion;
import consumer.model.Transaccion;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Se encarga de enviar la transaccion al endpoint POST

public class ApiPostService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiPostService() {
        this.httpClient   = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public boolean guardarTransaccion(Transaccion transaccion) {
        for (int intento = 1; intento <= Configuracion.MAX_REINTENTOS; intento++) {
            try {
                String body = objectMapper.writeValueAsString(transaccion);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(Configuracion.API_POST_URL))
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .header("Content-Type", "application/json")
                        .build();

                HttpResponse<String> response = httpClient.send(
                        request, HttpResponse.BodyHandlers.ofString()
                );

                System.out.println("[ConsumerCR] POST " + transaccion.getIdTransaccion()
                        + " | Status: " + response.statusCode()
                        + " | Respuesta: " + response.body());

                //if (response.statusCode() == 200) {  
                //aca tuve que cambiarlo porque la API repondia con 201
                if (response.statusCode() == 200 || response.statusCode() == 201) {
                    return true;
                }

                System.err.println("[ConsumerCR] Intento " + intento + " fallido. Reintentando...");
                Thread.sleep(2000);

            } catch (Exception e) {
                System.err.println("[ConsumerCR] Error en intento " + intento
                        + " para " + transaccion.getIdTransaccion()
                        + ": " + e.getMessage());
            }
        }

        System.err.println("[ConsumerCR] Se agotaron los reintentos para: "
                + transaccion.getIdTransaccion());
        return false;
    }
}
