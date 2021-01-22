package communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class communicates with the Restful API server.
 */
public class RestfulClient {
    HttpClient client;

    public RestfulClient(){
        client = HttpClient.newHttpClient();
    }

    public HttpResponse<String> sendPostRequest(String url, String data) throws InterruptedException, IOException {

        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        return response;
    }
}
