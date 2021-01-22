package javafx;

import interfaces.IGameController;
import javafx.geometry.Pos;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import models.User;

/**
 * Tile class to create the playing field.
 */
public class Tile extends StackPane {

    Rectangle rectangle;
    int counter = 0;
    boolean isSelected = false;
    private Text text = new Text();

    public Tile(){
        rectangle = new Rectangle(175, 175);
        rectangle.setFill(null);
        rectangle.setStroke(Color.BLACK);
        text.setFont(Font.font(75));
        setAlignment(Pos.CENTER);
        getChildren().addAll(rectangle, text);

        setOnMouseClicked(event -> {
            if(event.getButton() == MouseButton.PRIMARY){
                isSelected = !isSelected;
            }
            if(isSelected){
                rectangle.setFill(Color.LIGHTGREEN);
            }
            if(!isSelected){
                rectangle.setFill(null);
            }
        });
    }

    public void setRectangleColor(){

    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setText(String text){
        this.text.setText(text);
    }


}
