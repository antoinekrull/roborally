package client.ui;

import client.player.ClientPlayer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.InputStream;

public class PlayerListCell extends ListCell<ClientPlayer> {
    private final Label name = new Label();
    private final Label robot = new Label();
    private ImageView readyImage = new ImageView();
    private ImageView robotImage = new ImageView();
    private final VBox info = new VBox(name, robot);
    private final HBox layout = new HBox(robotImage, info, readyImage);

    public PlayerListCell() {
        super();
        name.setStyle("-fx-font-size: 16px;");
        robotImage.setFitHeight(30);
        robotImage.setFitWidth(30);
    }

    @Override
    protected void updateItem(ClientPlayer item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);

        if (empty || item == null || item.getUsername() == null) {
            name.setText(null);
            robot.setText(null);
            robotImage.setImage(null);
            setGraphic(null);
        } else {
            if (item.getUsername().equals("Group")) {
                name.setText("Active Player List");
                name.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: #d00707;" + "-fx-font-size: 22px;");
                layout.setAlignment(Pos.CENTER);
                robot.setText("");
            } else {
                System.out.println("has changed: username: " + item.getUsername() + ", id: " + item.getId());
                name.setText(item.getUsername());
                name.setStyle("-fx-font-weight: bold;");
                robot.setText("Roboter " + (item.getRobot() != null
                        ? item.getRobot().getFigure()
                        : 0));

                layout.setSpacing(15);
                if (item.getRobot() != null) {
                    int roboter = item.getRobot().getFigure();
                    if (roboter == 1) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_1_bunt.png");
                        robotImage.setImage(new Image(input));
                    }
                    if (roboter == 2) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_2_bunt.png");
                        robotImage.setImage(new Image(input));
                    }
                    if (roboter == 3) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_3_bunt.png");
                        robotImage.setImage(new Image(input));
                    }
                    if (roboter == 4) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_4_bunt.png");
                        robotImage.setImage(new Image(input));
                    }
                    if (roboter == 5) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_5_bunt.png");
                        robotImage.setImage(new Image(input));
                    }
                    if (roboter == 6) {
                        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_6_bunt.png");
                        robotImage.setImage(new Image(input));
                    }

                }
            /*
            if (item.isReady()) {
                readyImage.setImage(new Image(""));
            }
            else {
                readyImage.setImage(null);
            }

             */
            }
        }

        setGraphic(layout);
    }
}