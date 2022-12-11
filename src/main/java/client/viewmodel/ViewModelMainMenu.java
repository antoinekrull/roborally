package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelUser;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;


import java.io.IOException;

/**
 * ViewModel for start window
 *
 * @author Tobias
 * @version 0.1
 */

public class ViewModelMainMenu {

    @FXML
    private Button joinButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;
    @FXML
    private Label statusLabel;

    private ModelUser modelUser;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelMainMenu() {
        modelUser = ModelUser.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }

    public void joinButtonOnAction() throws IOException {
        if (modelUser.getConnection()){
            RoboRallyStart.switchScene("lobby.fxml");
        } else {
            statusLabel.setText("Connection failed. Please try again.");
            modelUser.reconnect();
            if (modelUser.getConnection()) {
                statusLabel.setText("Connection successful.");
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1500),
                        event -> {
                            try {
                                RoboRallyStart.switchScene("lobby.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }));
                timeline.play();
            }
        }

    }

    public void helpButtonOnAction() {
        //opens help
    }

    public void exitButtonOnAction() {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }

}
