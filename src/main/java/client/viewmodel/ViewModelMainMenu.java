package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

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

    private ModelUser modelUser;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelMainMenu() {
        modelUser = ModelUser.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }

    public void joinButtonOnAction() throws IOException {
        if (modelUser.getConnection()){
            RoboRallyStart.switchScene("lobby.fxml");
        }else {
            //TODO: show in menu that user is not connected or is no connected if reconnect works
           modelUser.reconnect();
        }
    }

    public void helpButtonOnAction() {
        //opens help
    }

    public void exitButtonOnAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        //TODO: close application not only window
        stage.close();
    }

}
