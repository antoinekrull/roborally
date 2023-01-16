package client.ui;

import client.player.ClientPlayer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
    }

    @Override
    protected void updateItem(ClientPlayer item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);

        if (empty || item == null || item.getUsername() == null) {
            name.setText(null);
            robot.setText(null);
            setGraphic(null);
        }
        else {
            if (item.getUsername().equals("Group")) {
                name.setText("Active Player List");
                name.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: red;" + "-fx-font-size: 22px;");
                layout.setAlignment(Pos.CENTER);
                layout.setStyle("-fx-background-color: #000000;");
                robot.setText("");
            }
            else {
                name.setText(item.getUsername());
                name.setStyle("-fx-font-weight: bold;");
                robot.setText("Roboter " + (item.getRobot() != null
                        ? item.getRobot().getFigure()
                        : 0));
            }

            /*
            switch(item.getRobot().getFigure()) {
                case -1: robotImage.setImage(null);
                case 1: robotImage.setImage(new Image(""));
                case 2: robotImage.setImage(new Image(""));
                case 3: robotImage.setImage(new Image(""));
                case 4: robotImage.setImage(new Image(""));
                case 5: robotImage.setImage(new Image(""));
                case 6: robotImage.setImage(new Image(""));
            }
            if (item.isReady()) {
                readyImage.setImage(new Image(""));
            }
            else {
                readyImage.setImage(null);
            }
             */

            setGraphic(layout);
        }
    }
}