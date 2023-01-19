package server;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerStart extends Application {

    private static final Logger logger = LogManager.getLogger(ServerStart.class);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException  {
        logger.debug("Server started");
        FXMLLoader loader = new FXMLLoader(ServerStart.class.getResource("servergui.fxml"));
        Scene scene = new Scene(loader.load(), 250, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
