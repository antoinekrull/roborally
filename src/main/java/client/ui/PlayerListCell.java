package client.ui;

import client.player.ClientPlayer;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class PlayerListCell extends ListCell<ClientPlayer> {
    private final Label title = new Label();
    private final Label detail = new Label();
    private final VBox layout = new VBox(title, detail);

    public PlayerListCell() {
        super();
        title.setStyle("-fx-font-size: 16px;");
    }

    @Override
    protected void updateItem(ClientPlayer item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);

        if (empty || item == null || item.getUsername() == null) {
            title.setText(null);
            detail.setText(null);
            setGraphic(null);
        }
        else {
            title.setText(item.getUsername());
            detail.setText("Roboter " + (item.getRobot() != null
                            ? item.getRobot().getFigure()
                            : 0)
            );
            setGraphic(layout);
        }
    }
}