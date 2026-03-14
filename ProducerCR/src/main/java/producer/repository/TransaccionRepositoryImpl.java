//Programacion3
//CarlosRamos
//0905-23-14141

package producer.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import producer.model.LoteTransacciones;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

 // Implementacion concreta que llama a la API GET del docente.
 // S (Single Responsibility): solo se encarga de llamar la API y parsear JSON.
 // D (Dependency Inversion): implementa la interfaz TransaccionRepository.
 
public class TransaccionRepositoryImpl implements TransaccionRepository {

    private static final String URL_API =
        "https://hly784ig9d.execute-api.us-east-1.amazonaws.com/default/transacciones";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TransaccionRepositoryImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public LoteTransacciones obtenerTransacciones() throws Exception {
        System.out.println("[ProducerCR] Llamando a la API GET...");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_API))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("[ProducerCR] Status HTTP: " + response.statusCode());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al obtener transacciones. Status: " + response.statusCode());
        }

        LoteTransacciones lote = objectMapper.readValue(response.body(), LoteTransacciones.class);
        System.out.println("[ProducerCR] Lote obtenido: " + lote.getLoteId()
                + " | Total transacciones: " + lote.getTransacciones().size());

        return lote;
    }
}
