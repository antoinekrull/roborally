package client;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RoboRallyStart extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private static Stage stage;
  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    FXMLLoader loader = new FXMLLoader(RoboRallyStart.class.getResource("mainmenu.fxml"));
    Scene scene = new Scene(loader.load(), 1650, 900);
    //scene.getStylesheets().add(getClass().getResource( "src/main/resources/stylesheets/styles.css" ).toExternalForm() );
    //scene.getStylesheets().add("src/main/resources/stylesheets/styles.css");
    stage.setFullScreen(true);
    stage.setScene(scene);
    stage.show();
  }

  public static void switchScene(String fxmlFile) throws IOException {
    FXMLLoader loader = new FXMLLoader(RoboRallyStart.class.getResource(fxmlFile));
    Scene newScene = new Scene(loader.load(), 1650, 900);
    stage.setFullScreen(true);
    stage.setScene(newScene);
    stage.show();
  }
}

