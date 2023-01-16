package client.ui;

import client.player.Player;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerListCell extends ListCell<Player> {
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
    protected void updateItem(Player item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);

        if (empty || item == null || item.getUsername() == null) {
            name.setText(null);
            robot.setText(null);
            setGraphic(null);
        }
        else {
            name.setText(item.getUsername());
            robot.setText("Roboter " + (item.getRobot() != null
                            ? item.getRobot().getFigure()
                            : 0)
            );
            int robo = item.getRobot().getFigure();
            switch(robo) {
                case 1: robotImage.setImage(new Image(""));
                case 2: robotImage.setImage(new Image(""));;
                case 3: robotImage.setImage(new Image(""));;
                case 4: robotImage.setImage(new Image(""));;
                case 5: robotImage.setImage(new Image(""));;
                case 6: robotImage.setImage(new Image(""));;
            }
            if (item.isReady()) {
                readyImage.setImage(new Image(""));
            }
            else {
                readyImage.setImage(null);
            }
            setGraphic(layout);
        }
    }
}