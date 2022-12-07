package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Dominik
 * @version 1.0
 */

public class MainMenu implements Initializable{
    @FXML
    private TextField nicknameTextfield;
    @FXML
    private Button joinButton;
    @FXML
    private Label messageLabel;

    private Client client;
    private Stage stage;

    /**
     * Class for showing login screen
     *
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void getClient(Client client){
        this.client= client;
    }
    public void onEnter() {
        if(nicknameTextfield.getText().equals("")){
            setMessage("You have to choose a username");
        }
        else {
            //not the real method, just for testing purpose
            client.sendPlayerValuesMessage(nicknameTextfield.getText(), 0);
            nicknameTextfield.clear();
        }
    }
    public void setMessage(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messageLabel.setText(message);
            }
        });
    }

    /**
     * switch stages from login screen to chat after entering a nickname
     *
     */
    public void joinLobby()throws IOException{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));

                Scene selectRobo = null;
                try {
                    selectRobo = new Scene(loader.load(), 588, 416);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //selectRobo.getStylesheets().add(ClientMain.class.getResource("/chat/chatgui/styles.css").toExternalForm());
                stage = (Stage) joinButton.getScene().getWindow();
                RobotSelection controller = loader.getController();
                controller.getClient(client);
                stage.setTitle("Robot Selection");
                stage.setScene(selectRobo);
                stage.show();
            }
        });
    }
    public void closeButtonAction() {
        client.closeApplication();
    }
}

