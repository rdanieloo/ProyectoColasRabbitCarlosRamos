//Programacion3
//CarlosRamos
//0905-23-14141

package consumer.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import consumer.config.AppConfig;
import consumer.model.Transaccion;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// S (Single Responsibility): solo se encarga de hacer el POST.
// D (Dependency Inversion): implementa GuardarTransaccionRepository.

public class GuardarTransaccionRepositoryImpl implements GuardarTransaccionRepository {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GuardarTransaccionRepositoryImpl() {
        this.httpClient   = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean guardar(Transaccion transaccion) {
        try {
            String body = objectMapper.writeValueAsString(transaccion);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(AppConfig.API_POST_URL))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("[ConsumerCR] POST " + transaccion.getIdTransaccion()
                    + " -> Status: " + response.statusCode()
                    + " | Respuesta: " + response.body());

            return response.statusCode() == 200;

        } catch (Exception e) {
            System.err.println("[ConsumerCR] Error al llamar API POST para "
                    + transaccion.getIdTransaccion() + ": " + e.getMessage());
            return false;
        }
    }
}
