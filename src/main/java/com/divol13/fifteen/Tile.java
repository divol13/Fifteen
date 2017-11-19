package com.divol13.fifteen;

import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Tile extends StackPane {
    static final int TILE_SIZE = 200;

    private Text text;
    private Rectangle rectangle;
    public int grid_x, grid_y;

    public Tile(String num) {
        rectangle = new Rectangle(TILE_SIZE-1, TILE_SIZE-1);

        rectangle.setStroke(Color.BLACK);
        rectangle.setArcWidth(40);
        rectangle.setArcHeight(40);
        rectangle.setFill(Color.AQUA);
        rectangle.setStrokeWidth(2.5);

        setAlignment(Pos.CENTER);

        text = new Text();
        text.setText(num);
        text.setFont(Font.font(48));

        setOnMouseClicked(this::onClicked);

        getChildren().addAll(rectangle, text);
    }

    public void setColor(Color color) {
        this.rectangle.setFill(color);
    }

    private void onClicked(MouseEvent mouseEvent) {
        Field field = (Field) getParent();
        field.check();
        if(mouseEvent.isAltDown()){
            System.out.println("ALT");
            field.swap(this);
        }
        else {
            field.move(this);
        }
        //field.check();
    }

    public String getText(){
        return text.getText();
    }

    public void setPosition(int x, int y) {
        setTranslateX(x * TILE_SIZE);
        setTranslateY(y * TILE_SIZE);

        grid_x = x;
        grid_y = y;
    }

    @Override
    public String toString() {
        return "["+ grid_x + "," + grid_y + "][\"" + text.getText() + "\"]";
    }
}
