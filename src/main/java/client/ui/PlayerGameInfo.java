package client.ui;

import client.player.ClientPlayerList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.InputStream;

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

                //shows amount of energycubes as String
                Label energy = new Label();
                energy.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");
                energy.textProperty().bind(energyCubesBar.progressProperty().asString());

                //binds the progressbar to the players energy cubes
                energyCubesBar.progressProperty().bind(clientPlayerList.getPlayerList().get(i).energyCubesProperty());
                System.out.println(clientPlayerList.getPlayerList().get(0).energyCubesProperty().get() + " " + energyCubesBar.progressProperty().get());

                //username for this player
                Label name = new Label();
                String username = clientPlayerList.getPlayerList().get(i).getUsername();
                name.setText(username);
                name.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");

                Label score = new Label();
                int playerScore = clientPlayerList.getPlayerList().get(i).getScore();
                score.setText(String.valueOf(playerScore));
                score.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");

                //amount of cards the user currently has
                Label cardAmount = new Label();
                cardAmount.textProperty().bind(clientPlayerList.getPlayerList().get(i).cardsInHandProperty().asString());
                cardAmount.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");

                /*
                //ImageView for cards
                //TODO: input stream must not be null (checking this out)
                InputStream input = getClass().getResourceAsStream(
                        "textures/cards/kartendeckGegner.png");
                Image deck = new Image(input);
                ImageView enemyCards = new ImageView(deck);
                enemyCards.setFitHeight(200);
                enemyCards.setPreserveRatio(true);

                 */
                //adds energybar and its label to stackpane
                StackPane energyBarStackPane = new StackPane();
                energyBarStackPane.getChildren().addAll(energyCubesBar, energy);
                //places label over energybar
                StackPane.setAlignment(energyBarStackPane, Pos.CENTER);
                StackPane.setAlignment(energy, Pos.CENTER);
                /*

                //adding username, hand and progressbar to the gridpane
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(energyCubesBar, energy);
                 */

                VBox vBox = new VBox();
                vBox.setSpacing(12);
                vBox.setPadding(new Insets(0, 0, 0, 10));
                vBox.getChildren().addAll(name, score,  cardAmount);

                //StackPane for PlayerInfomation
                StackPane playerInfoStackPane = new StackPane();

                //adding image of cards and username/card amount
                //playerInfoStackPane.getChildren().addAll(enemyCards, vBox);
                playerInfoStackPane.getChildren().add(vBox);

                HBox hBox2 = new HBox();
                hBox2.setSpacing(20);
                hBox2.setAlignment(Pos.CENTER_LEFT);
                hBox2.getChildren().addAll(playerInfoStackPane, energyBarStackPane);

                playerInfoGrid.add(hBox2, i-1, 0);
            }

        }
    }
}
