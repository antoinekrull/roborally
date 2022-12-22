package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelUser;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.MediaView;

/**
 * ViewModel for main menu
 *
 * @author Tobias, Benedikt
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
    private MediaView factoryvideo;

    private ModelUser modelUser;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelMainMenu() {
        modelUser = ModelUser.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }
    public void initialize() {
        /*
        //Background-Video
        Media factoryvideo = new Media(new File("src/main/resources/client/factoryBackground.mp4").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(factoryvideo);
        mediaPlayer.setAutoPlay(true);
         */
    }

    public void joinButtonOnAction() throws IOException {
        /*if (modelUser.getConnection()){
            RoboRallyStart.switchScene("robotselection.fxml");
        } else {
            statusLabel.setText("Connection failed. Please try again.");
            modelUser.reconnect();
            if (modelUser.getConnection()) {
                statusLabel.setText("Connection successful.");
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(1500),
                        event -> {
                            try {
                                RoboRallyStart.switchScene("robotselection.fxml");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }));
                timeline.play();
            }
        }

         */

        RoboRallyStart.switchScene("robotselection.fxml");
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
