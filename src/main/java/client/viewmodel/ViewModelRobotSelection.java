package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelGame;
import client.model.ModelUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

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

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelRobotSelection() {
        this.modelUser = ModelUser.getInstance();
        this.modelGame = ModelGame.getInstance();
        notifyChangeSupport = NotifyChangeSupport.getInstance();
    }

    public void initialize() {
        usernameButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
        usernameTextField.textProperty().bindBidirectional(modelUser.usernameProperty());
        robot1.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot1.getText()));
        robot2.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot2.getText()));
        robot3.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot3.getText()));
        robot4.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot4.getText()));
        robot5.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot5.getText()));
        robot6.setOnMouseClicked(mouseevent -> modelGame.setRobot(robot6.getText()));
    }

    public void usernameButtonOnAction() throws IOException {
        String robot = modelGame.getRobot();
        String username = modelUser.usernameProperty().get();
        int userID = modelUser.getUserID();
        if (!robot.isEmpty() && !username.isEmpty()) {
            //modelUser.sendUsername();
            //modelGame.sendRobotSelection(userID);
            notifyChangeSupport.setBoolean("ViewModelGameWindow");
            RoboRallyStart.switchScene("gamewindow.fxml");
        }
    }
}
