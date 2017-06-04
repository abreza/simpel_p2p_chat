package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static List<Process> processes = new ArrayList<>();

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 300, 135));
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(135);
        primaryStage.setMinWidth(300);
        primaryStage.setMaxHeight(135);
        primaryStage.setMaxWidth(300);
        primaryStage.show();
    }
}
