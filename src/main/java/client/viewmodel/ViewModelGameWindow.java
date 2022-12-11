package client.viewmodel;

import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * ViewModel for gamescreen
 *
 * @author Tobias
 * @version 0.1
 */

public class ViewModelGameWindow {

    @FXML
    private Button chatButton;
    @FXML
    private TextField chatTextfield;
    @FXML
    private VBox chatVBox;

    //buttons for cards

    private ModelChat modelChat;
    private ModelGame modelGame;
    private ModelUser modelUser;

    public ViewModelGameWindow() {
        this.modelChat = ModelChat.getInstance();
        this.modelGame = ModelGame.getInstance();
        this.modelUser = ModelUser.getInstance();
    }

    public void initialize() {
        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());
    }

    public void messageToChat() {
        String message = "";
        try {
            message = modelChat.getMessages().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        chatVBox.getChildren().add(hBox);
    }

    public void chatButtonOnAction() {
        int userID = modelUser.getUserID();
        modelChat.sendMessage(userID);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(chatTextfield.getText());
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        chatVBox.getChildren().add(hBox);

        chatTextfield.clear();
    }
}
