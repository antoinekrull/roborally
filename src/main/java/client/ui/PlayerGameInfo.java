package client.ui;

import client.player.ClientPlayerList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PlayerGameInfo {

    private ClientPlayerList clientPlayerList;
    private GridPane playerInfoGrid;
    public PlayerGameInfo(GridPane playerInfoGrid, ClientPlayerList clientPlayerList) {
        this.playerInfoGrid = playerInfoGrid;
        this.clientPlayerList = clientPlayerList;
    }

    public void loadPlayerInfo() {
        int size = clientPlayerList.getPlayerList().size();
        System.out.println(size);
        for (int i = 1; i < clientPlayerList.getPlayerList().size(); i++) {
            if (clientPlayerList.getPlayerList().get(i) != null) {
                //Progressbar for the amount of energy cubes a player currently has
                ProgressBar energyCubesBar = new ProgressBar();
                energyCubesBar.setPrefSize(120, 20);
                energyCubesBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

                /*
                //animates the loss and gain of energy cubes
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), new KeyValue(energyCubesBar.progressProperty(), clientPlayerList.getPlayerList().get(i).energyCubesProperty().get())));
                timeline.play();
                 */

                //shows amount of energycubes next to the progressbar as a number
                Label energy = new Label();
                energy.setStyle("-fx-text-fill: red;");
                energy.textProperty().bind(energyCubesBar.progressProperty().asString());

                //binds the progressbar to the players energy cube
                energyCubesBar.progressProperty().bind(clientPlayerList.getPlayerList().get(i).energyCubesProperty());
                System.out.println(clientPlayerList.getPlayerList().get(0).energyCubesProperty().get() + " " + energyCubesBar.progressProperty().get());

                //username for this player
                Label name = new Label();
                String username = clientPlayerList.getPlayerList().get(i).getUsername();
                name.setText(username);
                name.setStyle("-fx-text-fill: red;");

                //adding username, hand and progressbar to the gridpane
                HBox hBox = new HBox();
                hBox.setSpacing(5);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(energyCubesBar, energy);
                HBox hBox2 = new HBox();
                hBox2.setSpacing(5);
                hBox2.setAlignment(Pos.TOP_CENTER);
                hBox2.getChildren().add(name);
                hBox2.setPadding(new Insets(20, 0, 0, 50));
                VBox vBox = new VBox();
                vBox.setAlignment(Pos.CENTER_LEFT);
                vBox.setSpacing(12);
                vBox.setPadding(new Insets(0, 0, 0, 10));
                vBox.getChildren().addAll(hBox2, hBox);

                playerInfoGrid.add(vBox, i-1, 0);
            }

        }
    }
}
