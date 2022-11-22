package client;

import client.Controller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

public class LoginScreen implements Initializable{
    @FXML
    private TextField nicknameTextfield;
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
            client.sendUsernameToServer(nicknameTextfield.getText());
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
    public void goToChat(String username)throws IOException{
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));

                Scene scene = null;
                try {
                    scene = new Scene(loader.load(), 588, 416);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                scene.getStylesheets().add(ClientMain.class.getResource("/chat/chatgui/styles.css").toExternalForm());
                stage = (Stage) nicknameTextfield.getScene().getWindow();
                Controller controller = loader.getController();
                controller.getClient(client);
                controller.setUsername(username);
                client.getChat(controller);
                client.enterChat(true);
                stage.setTitle("Chat");
                stage.setScene(scene);
                stage.show();

            }
        });
    }
}

