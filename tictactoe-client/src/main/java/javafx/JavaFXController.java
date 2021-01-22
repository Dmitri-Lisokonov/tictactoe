package javafx;

import com.google.gson.Gson;
import communication.RestfulClient;
import communication.WebSocketClientController;
import communication.WebSocketMessageHandler;
import interfaces.IGameController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messaging.Mark;
import messaging.WebSocketMessage;
import models.User;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Observer;


/**
 * This class controls all GUI elements.
 */
public class JavaFXController implements IGameController, Observer {
    private RestfulClient httpClient;
    private WebSocketClientController wsClient;
    private Gson gson;
    WebSocketMessageHandler messageHandler;
    private static Tile[][] board;
    private User player;
    private User opponent;

    /**
     * FXML objects.
     */
    @FXML
    private Pane field;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_register;
    @FXML
    private TextArea txt_chat;
    @FXML
    private TextField txt_message;
    @FXML
    private Button btn_message;
    @FXML
    private Button btn_mark;
    @FXML
    private Label label_player;
    @FXML
    private Label label_opponent;

    @FXML
    private void initialize() {
        board = new Tile[3][3];
        httpClient = new RestfulClient();
        gson = new Gson();
        wsClient = WebSocketClientController.getInstance();
        messageHandler = new WebSocketMessageHandler(this);
        txt_chat.setWrapText(true);
        btn_mark.setDisable(true);
        btn_message.setDisable(true);
    }

    @Override
    public void update(java.util.Observable observable, Object o) {
        WebSocketMessage message = (WebSocketMessage) o;
        clearSelectedTiles();
        messageHandler.handleMessage(message);
    }

    /**
     * Logic.
     */
    @Override
    public void connectPlayer(User player){
        wsClient.connectUser(player, this);
    }

    public void registerUser() throws InterruptedException, IOException {
        User user = new User(username.getText(), password.getText());
        HttpResponse response = httpClient.sendPostRequest("http://localhost:8083/user/register", gson.toJson(user));
        if(response.statusCode() == 200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Registration successful.");
            alert.setContentText("User was registered.");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Registration unsuccessful.");
            alert.setContentText("Something went wrong, please try again.");

            alert.showAndWait();
        }
    }


    public void loginUser() throws InterruptedException, IOException{
        User user = new User(username.getText(), password.getText());
        HttpResponse<String> response = httpClient.sendPostRequest("http://localhost:8083/user/login", gson.toJson(user));
        if(response.statusCode() == 200){
            User p = gson.fromJson(response.body(), User.class);
            username.setDisable(true);
            password.setDisable(true);
            btn_login.setDisable(true);
            btn_register.setDisable(true);
            btn_mark.setDisable(false);
            player = p;
            connectPlayer(player);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Login successful.");
            alert.setContentText("signed in as " + player.getName()+ ".");
            alert.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Login unsuccessful.");
            alert.setContentText("Something went wrong, please try again.");

            alert.showAndWait();
        }
    }


    public String markTile(Mark mark){
        wsClient.sendMarkedTile(mark);
        return null;
    }

    /**
     * FXML methods.
     */
    public void drawMark(Mark mark){
        if(mark.getUserId() == player.getId()){
            board[mark.getX()][mark.getY()].setText(player.getMark());
        }
        else{
            board[mark.getX()][mark.getY()].setText(opponent.getMark());
        }
    }

    public void setPlayerLabel(String text){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                label_player.setText(text);
            }
        });
    }

    public void setOpponentLabel(String text){
        Platform.runLater(new Runnable() {
            @Override public void run() {
                label_opponent.setText(text);
            }
        });
    }

    public void clearSelectedTiles(){
        for(Tile[] array: board){
            for (Tile tile: array){
                if(tile.isSelected){
                    tile.rectangle.setFill(null);
                    tile.isSelected = false;
                }
            }
        }
    }

    public void chatButton(){

    }

    public void markButton(ActionEvent event){
        int selected = 0;
        int y = 0;
        int x = 0;
        Mark mark = new Mark();
        mark.setUserId(player.getId());
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                if(board[j][i].isSelected){
                    selected++;
                    x = j;
                    y = i;
                }
            }
        }
        if(selected > 1){
            addMessageToChat("GameClient: You can only select one tile!");
        }
        else{
            mark.setX(x);
            mark.setY(y);
            markTile(mark);
        }

    }

    public void addMessageToChat(String message){
        txt_chat.appendText(message);
    }

    public void loginButton(ActionEvent event) throws InterruptedException, IOException{
        loginUser();
    }

    public void registerButton(ActionEvent event) throws InterruptedException, IOException{
        registerUser();
    }

    /**
     * Getters and setters for non-FXML objects.
     */

    public void setPlayer(User player){
        this.player = player;
    }

    public User getPlayer(){
        return this.player;
    }

    public User getOpponent(){
        return this.opponent;
    }

    public void setOpponent(User opponent){
        this.opponent = opponent;
    }


    /**
     * Draw the board.
     * @param stage: JavaFX Stage object.
     */
    @FXML
    public void drawField(Stage stage){
        field = (Pane) stage.getScene().lookup("#field");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 175);
                tile.setTranslateY(i * 175);
                field.getChildren().add(tile);
                board[j][i] = tile;
            }
        }
    }


}
