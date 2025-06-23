package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameController game = new GameController();
        StackPane root = game.createContent();
        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Descubre la Fauna");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}