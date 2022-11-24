package client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameWindow extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader(GameWindow.class.getResource("gamewindow.fxml"));

    Scene scene = new Scene(loader.load(), 900, 500);
    primaryStage.setScene(scene);
    primaryStage.show();

    }
  }

