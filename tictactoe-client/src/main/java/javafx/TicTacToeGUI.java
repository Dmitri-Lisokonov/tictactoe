package javafx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX application class.
 */
public class TicTacToeGUI extends Application {


    JavaFXController controller = new JavaFXController();

    /**
     * Start JavaFX Application
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("tictactoeGUI.fxml"));
        stage.setTitle("Tic Tac Toe");
        stage.setScene(new Scene(root));
        controller.drawField(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
