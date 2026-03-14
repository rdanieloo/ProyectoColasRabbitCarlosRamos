//Programacion3
//CarlosRamos
//0905-23-14141

package producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import producer.config.Configuracion;
import producer.model.LoteTransacciones;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Se encarga de llamar al GET y retornar el lote de transacciones
public class ApiService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiService() {
        this.httpClient   = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public LoteTransacciones obtenerTransacciones() throws Exception {
        System.out.println("[ProducerCR] Llamando API GET...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(Configuracion.API_GET_URL))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("[ProducerCR] HTTP Status: " + response.statusCode());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error en GET. Status: " + response.statusCode());
        }

        LoteTransacciones lote = objectMapper.readValue(response.body(), LoteTransacciones.class);
        System.out.println("[ProducerCR] Lote: " + lote.getLoteId()
                + " | Transacciones: " + lote.getTransacciones().size());
        return lote;
    }
}

