package game;

import models.User;
import java.util.Map;

public class GameStateHandler {
    private static int playedTurns = 0;
    private static String gameState = "waiting";

    public void checkForWin(String[][] playingField){
        String line = "";
            for(int i = 0; i < 8; i++){
                switch(i){
                    //Check horizontal win conditions.
                    case 0:
                        line = playingField[0][0] + playingField[1][0] + playingField[2][0];
                        setWinState(line);
                        break;
                    case 1:
                        line = playingField[0][1] + playingField[1][1] + playingField[2][1];
                        setWinState(line);
                        break;
                    case 2:
                        line = playingField[0][2] + playingField[1][2] + playingField[2][2];
                        setWinState(line);
                        break;
                    //Check vertical win conditions.
                    case 3:
                        line = playingField[0][0] + playingField[0][1] + playingField[0][2];
                        setWinState(line);
                        break;
                    case 4:
                        line = playingField[1][0] + playingField[1][1] + playingField[1][2];
                        setWinState(line);
                        break;
                    case 5:
                        line = playingField[2][0] + playingField[2][1] + playingField[2][2];
                        setWinState(line);
                        break;
                    //Check diagonal win conditions.
                    case 6:
                        line = playingField[0][0] + playingField[1][1] + playingField[2][2];
                        setWinState(line);
                        break;
                    case 7:
                        line = playingField[0][2] + playingField[1][1] + playingField[2][0];
                        setWinState(line);
                        break;
                }
            }
            if(gameState == "playing"){
                if(playedTurns == 8){
                    gameState = "draw";
                }
                else{
                    playedTurns++;
                }
            }
    }

    private void setWinState(String line){
        if(line.equals("XXX")){
            gameState = "X";

        }
        else if(line.equals("OOO")){
            gameState =  "O";
        }
    }

    public void readyCheck(Map<Integer, User> players){
        if(players.size() == 2){
            gameState = "playing";
        }
        else if(players.size() < 2){
            gameState = "waiting";
        }
        else{
            gameState = "error";
        }
    }

    public void clearPlayedTurns(){
        playedTurns = 0;
    }

    public String getGameState(){
        return gameState;
    }

    public void setGameState(String state){
        gameState = state;
    }
}
