package game;

import interfaces.IGameController;
import messaging.Mark;
import models.User;

import java.util.*;

public class TicTacToeGame implements IGameController {
    private static Map<Integer, User> players = new HashMap<>();
    private static String[][] playingField;
    private GameStateHandler stateHandler;
    private static String  playerTurn = "X";
    private static String mark = null;

    public TicTacToeGame() {
        stateHandler = new GameStateHandler();
        playingField = new String[3][3];
    }

    public void connectPlayer(User player) {
        //Pick random mark for each player.
        if (stateHandler.getGameState().equals("waiting")) {
            if (players.size() == 0) {
                int max = 2;
                int min = 1;
                int range = max - min + 1;
                int rand = (int) (Math.random() * range) + min;
                if (rand == 1) {
                    player.setMarkTypeX();
                    mark = "X";
                } else {
                    player.setMarkTypeO();
                    mark = "O";
                }
            } else {
                if (mark.equals("X")) {
                    player.setMarkTypeO();
                } else {
                    player.setMarkTypeX();
                }
            }
        }
        //Add player to players hash map.
        players.put(player.getId(), player);
        stateHandler.readyCheck(players);
    }

    public String markTile(Mark mark) {
        String status = "";
        String drawnMark = players.get(mark.getUserId()).getMark();
        if (stateHandler.getGameState().equals("playing")) {
            if (!drawnMark.equals(playerTurn)) {
                status = "wrong turn";
            }
            //Check if empty.
            else if(playingField[mark.getX()][mark.getY()] == null){
                playingField[mark.getX()][mark.getY()] = drawnMark;
                stateHandler.checkForWin(playingField);
                status = stateHandler.getGameState();
                //Check if win conditions are met.
                    if (playerTurn.equals("X")) {
                        playerTurn = "O";
                    } else if (playerTurn.equals("O")) {
                        playerTurn = "X";
                    }
            }
            else if (!playingField[mark.getX()][mark.getY()].equals(null)) {
                status = "wrong state";
            }
            else if(stateHandler.getGameState().equals("draw")){
                status = "draw";
                stateHandler.setGameState("ended");
            }
        }
        else {
            status = "wrong state";
        }
        return status;
    }


    public List<User> getPlayers() {
        List<User> returnedPlayers = new ArrayList<>();
        Set<Integer> keys = players.keySet();
        if (!keys.isEmpty()) {
            for (Integer key : keys) {
                returnedPlayers.add(players.get(key));
            }
            return returnedPlayers;
        } else {
            return new ArrayList<>();
        }
    }

    public User getPlayerById(int id){
        return players.get(id);
    }

    public void clearPlayerTurn(){
        playerTurn = "X";
    }
}
