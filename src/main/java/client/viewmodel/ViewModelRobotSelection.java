package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelGame;
import client.model.ModelUser;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * ViewModel for selecting one of eight robots
 *
 * @author Tobias
 * @version 0.1
 */

public class ViewModelRobotSelection {

    @FXML
    private Button usernameButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label robot1, robot2, robot3, robot4, robot5, robot6;
    private ModelUser modelUser;
    private ModelGame modelGame;
    @FXML
    private Button exitButton;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelRobotSelection() {
        this.modelUser = ModelUser.getInstance();
        this.modelGame = ModelGame.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }

    public void initialize() {
        usernameButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
        usernameTextField.textProperty().bindBidirectional(modelUser.usernameProperty());
        robot1.setOnMouseClicked(mouseevent -> modelGame.setRobot(1));
        robot2.setOnMouseClicked(mouseevent -> modelGame.setRobot(2));
        robot3.setOnMouseClicked(mouseevent -> modelGame.setRobot(3));
        robot4.setOnMouseClicked(mouseevent -> modelGame.setRobot(4));
        robot5.setOnMouseClicked(mouseevent -> modelGame.setRobot(5));
        robot6.setOnMouseClicked(mouseevent -> modelGame.setRobot(6));
    }

    public void usernameButtonOnAction() throws IOException {
        int robot = modelGame.getRobot();
        String username = modelUser.usernameProperty().get();
        modelGame.addUser(username);
        int userID = modelUser.getUserID();
        if (robot!=-1 && !username.isEmpty()) {
            modelUser.sendPlayerValues(username, robot);
            //modelGame.sendRobotSelection(userID);
            RoboRallyStart.switchScene("lobby.fxml");
        }
    }
    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }
}
