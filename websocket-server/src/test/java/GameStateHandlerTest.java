
import game.GameStateHandler;
import models.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;


public class GameStateHandlerTest {
    GameStateHandler handler;
    String gameState;


    @BeforeEach
    public void init(){
        handler  = new GameStateHandler();
        gameState = "playing";
    }

    @Test
    public void checkWinHorizontalX(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][0] = "X";
        board[1][0] = "X";
        board[2][0] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinHorizontalX2(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][1] = "X";
        board[1][1] = "X";
        board[2][1] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinHorizontalX3(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][2] = "X";
        board[1][2] = "X";
        board[2][2] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinVerticalY(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][0] = "X";
        board[0][1] = "X";
        board[0][2] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinVerticalY2(){
        //Arrange
        String[][] board = new String[3][3];
        board[1][0] = "X";
        board[1][1] = "X";
        board[1][2] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinVerticalY3(){
        //Arrange
        String[][] board = new String[3][3];
        board[2][0] = "X";
        board[2][1] = "X";
        board[2][2] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinDiagonalY(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][0] = "X";
        board[1][1] = "X";
        board[2][2] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkWinDiagonalY2(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][2] = "X";
        board[1][1] = "X";
        board[2][0] = "X";
        //Act
        handler.checkForWin(board);
        //Assert
        Assert.assertEquals("X", handler.getGameState());
    }

    @Test
    public void checkDraw(){
        //Arrange
        String[][] board = new String[3][3];
        board[0][2] = "O";
        board[1][1] = "X";
        board[2][0] = "X";
        handler.setGameState("playing");
        //Act
        for(int i = 0; i < 9; i++){
            handler.checkForWin(board);
        }
        //Assert
        Assert.assertEquals("draw", handler.getGameState());
    }

    @Test
    public void readyCheckTestTwoPlayers(){
        //Arrange
        Map<Integer, User> players = new HashMap<>();
        players.put(1, new User("test", "testpw"));
        players.put(2, new User("test", "testpw"));
        //Act
        handler.readyCheck(players);
        //Assert
        Assert.assertEquals("playing", handler.getGameState());
    }

    @Test
    public void readyCheckTestError(){
        //Arrange
        Map<Integer, User> players = new HashMap<>();
        players.put(1, new User("test", "testpw"));
        players.put(2, new User("test", "testpw"));
        players.put(3, new User("test", "testpw"));
        //Act
        handler.readyCheck(players);
        //Assert
        Assert.assertEquals("error", handler.getGameState());
    }

    @Test
    public void readyCheckTestOnePlayer(){
        //Arrange
        Map<Integer, User> players = new HashMap<>();
        players.put(1, new User("test", "testpw"));
        //Act
        handler.readyCheck(players);
        //Assert
        Assert.assertEquals("waiting", handler.getGameState());
    }

}
