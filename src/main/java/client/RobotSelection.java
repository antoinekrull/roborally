package client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RobotSelection {
    @FXML
    private TextField nicknameTextfield;
    Client client;
    public void getClient(Client client){
        this.client= client;
    }

    public void test() {
        System.out.println("this robot is clickable");
    }
    public void onEnter() {
        String messageToSend = nicknameTextfield.getText();
        if (!messageToSend.isEmpty()) {

            client.sendGroupMessage(messageToSend);

            nicknameTextfield.clear();
        }
    }
}
