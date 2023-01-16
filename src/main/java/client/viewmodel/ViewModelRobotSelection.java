package client.viewmodel;

import client.RoboRallyStart;
import client.model.ModelGame;
import client.model.ModelUser;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import client.player.PlayerList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * ViewModel for selecting a robot and choosing a name
 *
 * @author Tobias, Benedikt
 * @version 0.1
 */

public class ViewModelRobotSelection {

    @FXML
    private Button usernameButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label robot1, robot2, robot3, robot4, robot5, robot6;
    @FXML
    private Button exitButton;
    @FXML
    private Label errorLabelRobotSelection;
    @FXML
    private ImageView robotImage;

    private List<Node> robots;
    private PlayerList playerList;

    private ModelUser modelUser;
    private ModelGame modelGame;

    public ViewModelRobotSelection() {
        this.modelUser = ModelUser.getInstance();
        this.modelGame = ModelGame.getInstance();
        playerList = modelGame.getPlayerList();
    }

    /**
     * Binds selected robot to model
     */
    public void initialize() {
        usernameButton.disableProperty().bind(usernameTextField.textProperty().isEmpty());
        usernameTextField.textProperty().bindBidirectional(modelUser.usernameProperty());
        errorLabelRobotSelection.textProperty().bind(modelGame.errorMessageProperty());
        robots = Arrays.asList(robot1, robot2, robot3, robot4, robot5, robot6);
        robot1.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(1);
            applyStyle(robot1);
            addRobotImageToView(robot1);
        });
        robot2.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(2);
            applyStyle(robot2);
            addRobotImageToView(robot2);
        });
        robot3.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(3);
            applyStyle(robot3);
            addRobotImageToView(robot3);
        });
        robot4.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(4);
            applyStyle(robot4);
            addRobotImageToView(robot4);
        });
        robot5.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(5);
            applyStyle(robot5);
            addRobotImageToView(robot5);
        });
        robot6.setOnMouseClicked(mouseevent -> {
            modelGame.setRobotProperty(6);
            applyStyle(robot6);
            addRobotImageToView(robot6);
        });
    }

    private void addRobotImageToView(Label robot) {
        if (robot.getText().equals("Roboter 1")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }
        if (robot.getText().equals("Roboter 2")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }
        if (robot.getText().equals("Roboter 3")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }
        if (robot.getText().equals("Roboter 4")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }
        if (robot.getText().equals("Roboter 5")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }
        if (robot.getText().equals("Roboter 6")) {
            robotImage.setImage(new Image("src/main/resources/textures/robots/Robot_1_bunt.png"));
        }

    }

    //When selecting a robot, the robot gets highlighted, but only the selected one
    private void applyStyle(Node node) {
        for (Node robot : robots) {
            robot.setStyle("");
        }
        node.setStyle("-fx-effect: dropshadow(three-pass-box, white, 15, 0.0, 0, 0);");
     }


    /**
     * Entering lobby after selecting a robot and choosing a nickname
     */
    public void usernameButtonOnAction() throws IOException {
        int robot = modelGame.robotProperty().get();
        String username = modelUser.usernameProperty().get();
        if (robot != 0 && !username.isEmpty()) {
            modelGame.sendPlayerInformation(username);

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(500),
                    event -> {
                        try {
                            if (modelUser.getVerification()) {
                                RoboRallyStart.switchScene("lobby.fxml");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
            timeline.play();

        }
    }

    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }
}
