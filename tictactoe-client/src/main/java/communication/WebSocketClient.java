package communication;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import messaging.Operation;
import messaging.WebSocketMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * This class communicates with the WebSocket server.
 */
@ClientEndpoint
public class WebSocketClient extends Communicator implements ICommunicator {

    private static WebSocketClient instance = null;
    private final String uri = "ws://localhost:8084/tictactoe/";
    private Session session;
    private String message;
    private Gson gson = null;

    boolean isRunning = false;

    private WebSocketClient() {
        gson = new Gson();
    }

    /**
     * Get singleton instance of this class.
     * Ensure that only one instance of this class is created.
     * @var 
     * @return instance of client web socket
     */
    public static WebSocketClient getInstance() {
        if (instance == null) {
            System.out.println("[WebSocket Client create singleton instance]");
            instance = new WebSocketClient();
        }
        return instance;
    }

    @OnOpen
    public void onWebSocketConnect(Session session){
        System.out.println("[WebSocket Client open session] " + session.getRequestURI());
        this.session = session;
    }

    @OnMessage
    public void onWebSocketText(String message, Session session){
        this.message = message;
        System.out.println("[WebSocket Client message received] " + message);
        processMessage(message);
    }

    @OnError
    public void onWebSocketError(Session session, Throwable cause) {
        System.out.println("[WebSocket Client connection error] " + cause.toString());
    }

    @OnClose
    public void onWebSocketClose(CloseReason reason){
        System.out.print("[WebSocket Client close session] " + session.getRequestURI());
        System.out.println(" for reason " + reason);
        session = null;
    }

    /**
     *  Processes the incoming message from the server.
     *
     */
    private void processMessage(String jsonMessage) {

        WebSocketMessage message;
        try {
            message = gson.fromJson(jsonMessage, WebSocketMessage.class);
        }

        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket Client ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        message.getOperation();
        if (message == null) {
            System.out.println("[WebSocket Client ERROR: update property operation expected]");
            return;
        }

        this.setChanged();
        this.notifyObservers(message);
    }

    /**
     *  Send a WebSocketMessage object.
     * @param message: WebsocketMessage object(encapsulated message)
     */
    public void sendMessageToServer(WebSocketMessage message) {
        String jsonMessage = gson.toJson(message);
        // Use asynchronous communication
        session.getAsyncRemote().sendText(jsonMessage);
    }

    /**
     *  Start the connection.
     */
    public void start() {
        System.out.println("[WebSocket Client start connection]");
        if (!isRunning) {
            startClient();
            isRunning = true;
        }
    }

    /**
     *  Close the connection.
     */
    public void stop() {
        System.out.println("[WebSocket Client stop]");
        if (isRunning) {
            stopClient();
            isRunning = false;
        }
    }

    /**
     * Start a WebSocket client.
     */
    private void startClient() {
        System.out.println("[WebSocket Client start]");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(uri));

        } catch (IOException | URISyntaxException | DeploymentException ex) {
            // do something useful eventually
            ex.printStackTrace();
        }
    }

    /**
     * Stop the client when it is running.
     */
    private void stopClient(){
        System.out.println("[WebSocket Client stop]");
        try {
            session.close();

        } catch (IOException ex){
            // do something useful eventually
            ex.printStackTrace();
        }
    }

}
