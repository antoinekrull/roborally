package client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenController extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private static Stage stage;
  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("mainmenu.fxml"));

    Scene scene = new Scene(loader.load(), 1650, 900);
    stage.setScene(scene);
    stage.show();
  }

  public static void switchScene(String fxmlFile) throws IOException {
    FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource(fxmlFile));

    Scene newScene = new Scene(loader.load(), 1650, 900);
    stage.setScene(newScene);
    stage.show();
  }
}

