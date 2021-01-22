package communication;

import com.google.gson.Gson;
import javafx.JavaFXController;
import messaging.Mark;
import messaging.WebSocketMessage;
import models.User;

public class WebSocketMessageHandler {
    JavaFXController gameController;
    Gson gson;

    public WebSocketMessageHandler(JavaFXController gameController){
        this.gameController = gameController;
        gson = new Gson();
    }

    public void handleMessage(WebSocketMessage message){
        Mark mark;
        switch(message.getOperation()){
            case CONNECT_PLAYER:
                User[] players = gson.fromJson(message.getMessage(), User[].class);
                for(User player: players){
                    if(player.getId() != gameController.getPlayer().getId()){
                        gameController.setOpponent(player);
                        gameController.setOpponentLabel(player.getName());

                        if(player.getMark().equals("X")){
                            gameController.getOpponent().setMarkTypeX();
                        }
                        else{
                            gameController.getOpponent().setMarkTypeO();
                        }

                    }
                    else{
                        gameController.setPlayerLabel(player.getName());
                            if(player.getMark().equals("X")){
                                gameController.getPlayer().setMarkTypeX();
                            }
                            else{
                                gameController.getPlayer().setMarkTypeO();
                            }
                    }
                }
                gameController.addMessageToChat(message.getResponseMessage());
                break;
            case MARK_TILE:
                mark = gson.fromJson(message.getMessage(), Mark.class);
                gameController.drawMark(mark);
                gameController.addMessageToChat(message.getResponseMessage());
                break;
            case END_GAME:
                mark = gson.fromJson(message.getMessage(), Mark.class);
                gameController.drawMark(mark);
                gameController.addMessageToChat(message.getResponseMessage());
                //Todo: disable GUI
                System.out.println("fin");
                break;
            case FALSE_MOVE:
                gameController.addMessageToChat(message.getResponseMessage());
        }
    }
}
