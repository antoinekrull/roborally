package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelUser;
import java.io.IOException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

/**
 * ViewModel for main menu
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
    @FXML
    private MediaView factoryVideo;

    private ModelUser modelUser;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelMainMenu() {
        modelUser = ModelUser.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }
    public void initialize() {
        //Background-Video:

        /*
        Media factoryVideo = new Media(
            getClass().getResource("https://www.youtube.com/watch?v=7rk3b1ctttg").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(factoryVideo);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.play();
        */
    }

    public void joinButtonOnAction() throws IOException {
        /*if (modelUser.getConnection()){
            RoboRallyStart.switchScene("login.fxml");
        } else {
            statusLabel.setText("Connection failed. Please try again.");
            modelUser.reconnect();
            if (modelUser.getConnection()) {
                statusLabel.setText("Connection successful.");
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1500),
                        event -> {
                            try {
                                RoboRallyStart.switchScene("login.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }));
                timeline.play();
            }
        }

         */

        RoboRallyStart.switchScene("login.fxml");
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
