import game.GameStateHandler;
import game.TicTacToeGame;
import messaging.Mark;
import models.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TicTacToeGameTest {

    TicTacToeGame game;
    GameStateHandler handler;

    @BeforeEach
    public void setup(){
        game  = new TicTacToeGame();
        handler = new GameStateHandler();
        game.clearPlayerTurn();
        handler.clearPlayedTurns();
    }

    @After
    public void reset(){
        game = new TicTacToeGame();
        handler = new GameStateHandler();
    }

    @Test
    public void connectPlayer(){
        //Arrange
        User player = new User(1, "Ali", 99);
        //Act
        game.connectPlayer(player);
        User u = game.getPlayerById(1);
        //Assert
        Assert.assertEquals("Ali", u.getName());
    }

    @Test
    public void MarkTileWhileWaiting(){
        //Arrange
        Mark mark = new Mark();
        mark.setUserId(1);
        mark.setX(2);
        mark.setY(2);

        User player = new User(1, "Ali", 99);
        player.setMarkTypeX();
        game.connectPlayer(player);
        //Act
        String status = game.markTile(mark);
        //Assert
        Assert.assertEquals("wrong state", status);
    }

    @Test
    public void MarkTileWhilePlaying(){
        //Arrange
        Mark mark = new Mark();
        mark.setUserId(1);
        mark.setX(2);
        mark.setY(2);

        User player = new User(1, "Ali", 99);
        game.connectPlayer(player);
        game.getPlayerById(1).setMarkTypeX();
        handler.setGameState("playing");
        //Act
        String status = game.markTile(mark);
        //Assert
        Assert.assertEquals("playing", status);
    }

    @Test
    public void MarkTileOnMarkedLocation(){
        //Arrange
        Mark mark = new Mark();
        mark.setUserId(1);
        mark.setX(2);
        mark.setY(2);

        User player = new User(1, "Ali", 99);
        game.connectPlayer(player);
        game.getPlayerById(1).setMarkTypeX();
        handler.setGameState("playing");
        //Act
        game.markTile(mark);
        game.getPlayerById(1).setMarkTypeO();
        String status = game.markTile(mark);
        //Assert
        Assert.assertEquals("wrong state", status);
    }

    @Test
    public void MarkTileWhenNoTurn(){
        //Arrange
        Mark mark = new Mark();
        mark.setUserId(1);
        mark.setX(2);
        mark.setY(2);

        User player = new User(1, "Ali", 99);
        game.connectPlayer(player);
        game.getPlayerById(1).setMarkTypeO();
        handler.setGameState("playing");
        //Act
        String status = game.markTile(mark);
        //Assert
        Assert.assertEquals("wrong turn", status);
    }

    @Test
    public void MarkTileDraw(){
        //Arrange
        Mark markPlayerOne = new Mark();
        Mark markPlayerTwo = new Mark();
        markPlayerOne.setUserId(1);
        markPlayerTwo.setUserId(2);
        User player = new User(1, "Ali", 99);
        User playerTwo = new User(2, "Alex", 99);
        game.connectPlayer(player);
        game.connectPlayer(playerTwo);
        game.getPlayerById(1).setMarkTypeX();
        game.getPlayerById(2).setMarkTypeO();
        handler.setGameState("playing");
        //Act
        //1
        markPlayerOne.setX(0);
        markPlayerOne.setY(0);
        game.markTile(markPlayerOne);
        markPlayerTwo.setX(1);
        markPlayerTwo.setY(0);
        game.markTile(markPlayerTwo);
        //2
        markPlayerOne.setX(0);
        markPlayerOne.setY(1);
        game.markTile(markPlayerOne);
        markPlayerTwo.setX(1);
        markPlayerTwo.setY(1);
        game.markTile(markPlayerTwo);
        //3
        markPlayerOne.setX(1);
        markPlayerOne.setY(2);
        game.markTile(markPlayerOne);
        markPlayerTwo.setX(0);
        markPlayerTwo.setY(2);
        game.markTile(markPlayerTwo);
        //4
        markPlayerOne.setX(2);
        markPlayerOne.setY(0);
        game.markTile(markPlayerOne);
        markPlayerTwo.setX(2);
        markPlayerTwo.setY(1);
        game.markTile(markPlayerTwo);
        //Draw
        markPlayerOne.setX(2);
        markPlayerOne.setY(2);
        String status = game.markTile(markPlayerOne);
        //Assert
        Assert.assertEquals("draw", status);
    }


}
