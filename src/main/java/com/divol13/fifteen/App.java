package com.divol13.fifteen;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 800;

    static final int WIDTH = 4;
    static final int HEIGHT = 4;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        root.getChildren().add(new Field());
        return root;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fifteen");
        primaryStage.setScene( new Scene( createContent()));
        primaryStage.show();
    }

    // entry point of program
    public static void main(String[] args) {
        launch(args);
    }
}
