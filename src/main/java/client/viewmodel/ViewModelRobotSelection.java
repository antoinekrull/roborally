package client.viewmodel;

import client.RoboRallyStart;
import client.model.ModelGame;
import client.model.ModelUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * ViewModel for selecting one of eight robots
 *
 * @author Tobias
 * @version 1.0
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

    public ViewModelRobotSelection() {
        this.modelUser = ModelUser.getInstance();
        this.modelGame = ModelGame.getInstance();
    }

    public void initialize() {
        //usernameButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
        //usernameTextField.textProperty().bindBidirectional(modelUser.usernameProperty());
        robot1.setOnMouseClicked(mouseEvent -> robot1.textProperty().bindBidirectional(modelGame.robotProperty()));
        robot2.setOnMouseClicked(mouseEvent -> robot2.textProperty().bindBidirectional(modelGame.robotProperty()));
        robot3.setOnMouseClicked(mouseEvent -> robot3.textProperty().bindBidirectional(modelGame.robotProperty()));
        robot4.setOnMouseClicked(mouseEvent -> robot4.textProperty().bindBidirectional(modelGame.robotProperty()));
        robot5.setOnMouseClicked(mouseEvent -> robot5.textProperty().bindBidirectional(modelGame.robotProperty()));
        robot6.setOnMouseClicked(mouseEvent -> robot6.textProperty().bindBidirectional(modelGame.robotProperty()));
    }

    public void usernameButtonOnAction() throws IOException {
        String robot = modelGame.getRobot();
        String username = modelUser.getUsername();
        int userID = modelUser.getUserID();
        if (!robot.isEmpty() && !username.isEmpty()) {
            //modelUser.sendUsername();
            //modelGame.sendRobotSelection(userID);
        }

        RoboRallyStart.switchScene("gamewindow.fxml");
    }

    //method for clicking on label
    /*
    public void robot1LabelOnMouseAction(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == robot1) {
            robot1.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot2) {
            robot2.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot3) {
            robot3.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot4) {
            robot4.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot5) {
            robot5.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot6) {
            robot6.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot7) {
            robot7.textProperty().bindBidirectional(modelGame.robotProperty());
        }
        if (mouseEvent.getSource() == robot8) {
            robot6.textProperty().bindBidirectional(modelGame.robotProperty());
        }
    }
    */

}
