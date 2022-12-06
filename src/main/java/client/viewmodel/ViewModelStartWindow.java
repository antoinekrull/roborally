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

public class ViewModelStartWindow{

    @FXML
    private Button joinButton;
    @FXML
    private Button helpButton;
    @FXML
    private Button closeButton;

    private ModelUser modelUser;

    public ViewModelStartWindow() {
        ;
    }

    public void joinButtonOnAction() throws IOException {
        //start client
        //modelUser = ModelUser.getInstance();

        //establish connection

        //if connected
        ScreenController.switchScene("robotselection.fxml");
    }

    public void setHelpButtonOnAction() {
        //opens help
    }

    public void closeButtonOnAction() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

}
