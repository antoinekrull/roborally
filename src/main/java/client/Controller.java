package client;

import client.Client;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Tobias, Dominik
 * @version 1.0
 */

public class Controller implements Initializable {

    @FXML
    private Button buttonSend;
    @FXML
    private Label chatLabel;
    @FXML
    private TextField chatTextfield;
    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private AnchorPane scenePane;

    private Client client;
    private Stage stage;
    private String username;


    /**
     * This is the controller for the chat.
     * Initialize method for making scrolling in chat possible.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chatScrollPane.setVvalue((Double) newValue);
            }
        });
    }

    /**
     * Will sent the message entered in the chat window.
     */
    public void onEnter() {
        String messageToSend = chatTextfield.getText();
        if (!messageToSend.isEmpty()) {

            client.sendGroupMessage(messageToSend);

            chatTextfield.clear();
        }
    }

    /**
     * Adds message to the chat in client window.
     *
     * @param message Entered message
     */
    public void addMessageToChat(String message) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(3,3,3);" +
                "-fx-background-radius: 20px;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.68, 0.58, 0.3));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(hBox);
            }
        });
    }
    public void getClient(Client client) {
        this.client = client;
    }
    public void exit(){
        client.closeApplication();
        System.out.println("tschüss");
    }
    public void setUsername(String username) {
        this.username = username;
        chatLabel.setText("Chat von: "+username);
    }
}

