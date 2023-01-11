package server;

import client.RoboRallyStart;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerStart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException  {
        FXMLLoader loader = new FXMLLoader(ServerStart.class.getResource("servergui.fxml"));
        Scene scene = new Scene(loader.load(), 250, 250);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
