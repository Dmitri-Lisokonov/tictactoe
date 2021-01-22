package websockets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import game.TicTacToeGame;
import messaging.Mark;
import messaging.Operation;
import messaging.WebSocketMessage;
import models.User;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.*;

@ServerEndpoint(value="/tictactoe/")
public class WebSocketController {
    private static final List<Session> sessions = new ArrayList<>();
    private static final Map<Integer, List<Session>> gameSessions = new HashMap<>();
    private TicTacToeGame game = new TicTacToeGame();

    /**
     * Processes and parses the message received from client.
     * @param jsonMessage: WebSocketMessage object in JSON.
     * @param session: Session of client.
     */
    private void handleMessageFromClient(String jsonMessage, Session session) {
        Gson gson = new Gson();
        WebSocketMessage inComingMessage;
        WebSocketMessage outGoingMessage = new WebSocketMessage();
        try {
            inComingMessage = gson.fromJson(jsonMessage, WebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }
        if(inComingMessage.getLobbyId() == 0){
            inComingMessage.setLobbyId(1);
        }
        // Process message based on operation.
        Integer id = inComingMessage.getLobbyId();
        if (null != inComingMessage.getOperation() && 0 != id) {
            switch (inComingMessage.getOperation()) {
                case CONNECT_PLAYER:
                    User user = gson.fromJson(inComingMessage.getMessage(), User.class);
                    if(gameSessions.get(id) == null){
                        //add session to gameSessions and user to game.
                        gameSessions.put(id, new ArrayList<>());
                        gameSessions.get(id).add(session);
                        System.out.println(gameSessions.get(1).size());
                        game.connectPlayer(user);

                        //create outgoingmessage object.
                        outGoingMessage.setMessage(gson.toJson(game.getPlayers()));
                        outGoingMessage.setLobbyId(1);
                        outGoingMessage.setOperation(Operation.CONNECT_PLAYER);
                        outGoingMessage.setResponseMessage("GameServer: No lobby found. Created new lobby. \n");
                        session.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                    }
                    else if(gameSessions.get(id) != null){
                        //add user to existing lobby.
                        gameSessions.get(id).add(session);
                        game.connectPlayer(user);
                        outGoingMessage.setLobbyId(1);
                        System.out.println(gameSessions.get(1).size());
                        outGoingMessage.setMessage(gson.toJson(game.getPlayers()));
                        outGoingMessage.setResponseMessage("GameServer: Joining existing lobby. \n");
                        outGoingMessage.setOperation(Operation.CONNECT_PLAYER);
                        session.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                        outGoingMessage.setResponseMessage("GameServer: "+ user.getName() + " has joined the lobby. \n");

                        //send messages to both users.
                        for(Session s : gameSessions.get(1)){
                            s.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                        }
                        for(Session s : gameSessions.get(1)) {
                            for (User player : game.getPlayers()) {
                                outGoingMessage.setResponseMessage("GameServer: " + player.getName() + " is now playing as " + player.getMark() + "\n");
                                s.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                            }
                        }
                    }
                    break;
                case MARK_TILE:
                    Mark mark = gson.fromJson(inComingMessage.getMessage(), Mark.class);
                    String status = game.markTile(mark);
                    User p = game.getPlayerById(mark.getUserId());

                    if(status.equals("playing")){
                        outGoingMessage.setOperation(Operation.MARK_TILE);
                        outGoingMessage.setResponseMessage("GameServer: " + p.getName() + " has marked location " + mark.getX() +","+ mark.getY() + ".\n");
                        outGoingMessage.setMessage(gson.toJson(mark));
                        for(Session s : gameSessions.get(1)){
                            s.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                        }
                    }
                    else if(status.equals("O") || status.equals("X")){
                        outGoingMessage.setOperation(Operation.END_GAME);
                        outGoingMessage.setResponseMessage("GameServer: " + p.getName() + " has won the game.\n");
                        outGoingMessage.setMessage(gson.toJson(mark));
                        for(Session s : gameSessions.get(1)){
                            s.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                        }
                    }
                    else if(status.equals("wrong state")){
                        outGoingMessage.setOperation(Operation.FALSE_MOVE);
                        outGoingMessage.setResponseMessage("GameServer: Something went wrong.\n");
                        outGoingMessage.setMessage(null);
                        session.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                    }
                    else if(status.equals("wrong turn")){
                        outGoingMessage.setOperation(Operation.FALSE_MOVE);
                        outGoingMessage.setResponseMessage("GameServer: It is not your turn yet.\n");
                        outGoingMessage.setMessage(null);
                        session.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                    }
                    else if(status.equals("draw")){
                        outGoingMessage.setOperation(Operation.END_GAME);
                        outGoingMessage.setResponseMessage("GameServer: The game resulted in a draw.\n");
                        outGoingMessage.setMessage(gson.toJson(mark));
                        for(Session s : gameSessions.get(1)){
                            s.getAsyncRemote().sendText(gson.toJson(outGoingMessage));
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Standard open, close, onmessage and onerror
     */
    @OnOpen
    public void onConnect(Session session) {
        System.out.println("[WebSocket Connected] SessionID: " + session.getId());
        String message = String.format("[New client with client side session ID]: %s", session.getId());
        sessions.add(session);
        System.out.println("[#gamesessions]: " + sessions.size());
    }

    @OnMessage
    public void onText(String message, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Received] : " + message);
        handleMessageFromClient(message, session);
    }

    @OnClose
    public void onClose(CloseReason reason, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Socket Closed]: " + reason);
        sessions.remove(session);
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        System.out.println("[WebSocket Session ID] : " + session.getId() + "[ERROR]: ");
        cause.printStackTrace(System.err);
    }

}
