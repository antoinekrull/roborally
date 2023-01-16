package server;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerStart extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException  {
        FXMLLoader loader = new FXMLLoader(ServerStart.class.getResource("servergui.fxml"));
        Scene scene = new Scene(loader.load(), 250, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
