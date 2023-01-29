package client;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RoboRallyStart extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  private static Stage stage;
  private static StackPane layout;

  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    InputStream input = getClass().getResourceAsStream("/textures/designelements/kk_cover.png");
    Image icon = new Image(input);
    FXMLLoader loader = new FXMLLoader(RoboRallyStart.class.getResource("mainmenu.fxml"));
    Scene scene = new Scene(loader.load(), 1280, 720);
    scene.getStylesheets().add(getClass().getResource( "styles.css" ).toExternalForm() );
    stage.setFullScreen(true);
    stage.setScene(scene);
    stage.getIcons().add(icon);
    stage.show();
  }

  public static void switchScene(String fxmlFile) throws IOException {
    FXMLLoader loader = new FXMLLoader(RoboRallyStart.class.getResource(fxmlFile));
    layout = loader.load();
    stage.getScene().setRoot(layout);
  }

}

