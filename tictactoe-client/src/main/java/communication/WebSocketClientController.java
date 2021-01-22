package communication;

import com.google.gson.Gson;
import interfaces.IGameController;
import messaging.Mark;
import messaging.Operation;
import messaging.WebSocketMessage;
import models.User;

import java.util.Observer;

public class WebSocketClientController {

    private static WebSocketClientController instance = null;
    private WebSocketClient client;
    private Gson gson;

    private WebSocketClientController(){
        client = WebSocketClient.getInstance();
        gson = new Gson();
    }

    private synchronized static void createInstance () {
        if (instance == null) instance = new WebSocketClientController ();
    }

    public static WebSocketClientController getInstance () {
        if (instance == null) createInstance ();
        return instance;
    }

    private void sendMessage(WebSocketMessage message){
        client.sendMessageToServer(message);
    }

    public void connectUser(User user, IGameController gameController){
        client.start();
        client.addObserver((Observer) gameController);
        WebSocketMessage message = new WebSocketMessage();
        message.setOperation(Operation.CONNECT_PLAYER);
        message.setMessage(gson.toJson(user));
        client.sendMessageToServer(message);
    }

    public void sendMarkedTile(Mark mark){
        WebSocketMessage message = new WebSocketMessage();
        message.setOperation(Operation.MARK_TILE);
        message.setMessage(gson.toJson(mark));
        client.sendMessageToServer(message);
    }
}
