package client.viewmodel;

import client.ScreenController;
import client.model.ModelUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * ViewModel for start window
 *
 * @author Tobias
 * @version 1.0
 */

public class ViewModelMainMenu {

    @FXML
    private Button joinButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button exitButton;

    private ModelUser modelUser;

    public ViewModelMainMenu() {

    }

    public void joinButtonOnAction() throws IOException {
        //start client
        //modelUser = ModelUser.getInstance();

        //establish connection

        //if connected
        ScreenController.switchScene("lobby.fxml");
    }

    public void helpButtonOnAction() {
        //opens help
    }

    public void exitButtonOnAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}
