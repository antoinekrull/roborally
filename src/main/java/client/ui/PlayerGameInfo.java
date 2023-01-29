package client.ui;

import client.player.ClientPlayerList;
import client.viewmodel.ViewModelGameWindow;
import game.CustomTimer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class PlayerGameInfo {

    private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);
    private ClientPlayerList clientPlayerList;
    private GridPane playerInfoGrid;

    public PlayerGameInfo(GridPane playerInfoGrid, ClientPlayerList clientPlayerList) {
        this.playerInfoGrid = playerInfoGrid;
        this.clientPlayerList = clientPlayerList;
    }

    public void loadPlayerInfo() {
        int size = clientPlayerList.getPlayerList().size();
        logger.info(size);
        for (int i = 1; i < clientPlayerList.getPlayerList().size(); i++) {
            if (clientPlayerList.getPlayerList().get(i) != null) {

                //Progressbar for the amount of energy cubes a player currently has
                ProgressBar energyCubesBar = new ProgressBar();
                energyCubesBar.setPrefSize(120, 20);

                //binds the progressbar to the players energy cubes
                energyCubesBar.progressProperty().bind(clientPlayerList.getPlayerList().get(i).energyCubesProperty().divide(22));
                //adds a little animation when value of the progessbar changes
                energyCubesBar.progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        FadeTransition transition = new FadeTransition(Duration.millis(1000), energyCubesBar);
                        transition.setFromValue(oldValue.doubleValue());
                        transition.setToValue(newValue.doubleValue());
                        transition.play();
                    }
                });
                logger.debug("Progressbar Value: " + energyCubesBar.progressProperty());

                //shows amount of energycubes as String
                Label energy = new Label();
                energy.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");
                Platform.runLater(() -> energy.textProperty().bind(energyCubesBar.progressProperty().multiply(22).asString()));




                //Label for User:
                Label user = new Label("User: ");
                user.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //username for this player
                Label name = new Label();
                String username = clientPlayerList.getPlayerList().get(i).getUsername();
                name.setText(username);
                name.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //adding user + name Label to HBox
                HBox userHBox = new HBox();
                userHBox.getChildren().addAll(user, name);




                //Label Your Score:
                Label yourScore = new Label("Score: ");
                yourScore.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //bind score of player to Label score
                Label score = new Label();
                score.textProperty().bind(clientPlayerList.getPlayerList().get(i).scoreProperty().asString());
                score.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //wrapping yourScore and score into HBox
                HBox scoreHBox = new HBox();
                scoreHBox.getChildren().addAll(yourScore, score);




                //Label Cards in Hand:
                Label cardsInHand = new Label("Cards in hand: ");
                cardsInHand.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //amount of cards the user currently has
                Label cardAmount = new Label();
                cardAmount.textProperty().bind(clientPlayerList.getPlayerList().get(i).cardsInHandProperty().asString());
                cardAmount.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //adding cardsInHand and cardAmount to HBox
                HBox cardNumberHBox = new HBox();
                cardNumberHBox.getChildren().addAll(cardsInHand, cardAmount);




                //Turn Label
                Label turnLabel = new Label();
                turnLabel.textProperty().bind(clientPlayerList.getPlayerList().get(i).activePlayerProperty());
                turnLabel.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");



                //Label Energymeter
                Label energyMeter = new Label("Energymeter");
                energyMeter.setStyle("-fx-text-fill: #9b0303;" + "-fx-font-weight: bold;");

                //adds energybar and its label to stackpane
                StackPane energyBarStackPane = new StackPane();
                energyBarStackPane.getChildren().addAll(energyCubesBar, energy);
                //places label over energybar
                StackPane.setAlignment(energyBarStackPane, Pos.CENTER);
                StackPane.setAlignment(energy, Pos.CENTER);

                //VBox containing Energymeter Label and energymeter StackPane
                VBox energyInfoVBox = new VBox();
                energyInfoVBox.setAlignment(Pos.CENTER);
                energyInfoVBox.getChildren().addAll(energyMeter, energyBarStackPane);




                //ImageView for enemy cards
                InputStream input = getClass().getResourceAsStream(
                        "/textures/cards/kartendeckGegner.png");
                Image deck = new Image(input);
                ImageView enemyCards = new ImageView(deck);
                enemyCards.setFitHeight(250);
                enemyCards.setPreserveRatio(true);



                //adding playerInfo HBoxes to a VBox
                VBox playerInfoVBox = new VBox();
                playerInfoVBox.setSpacing(15);
                //playerInfoVBox.setPadding(new Insets(0, 0, 0, 0));
                playerInfoVBox.getChildren().addAll(userHBox, scoreHBox, cardNumberHBox, turnLabel);

                //StackPane for PlayerInfomation
                StackPane playerInfoStackPane = new StackPane();

                //adding image of cards and username/card amount
                playerInfoStackPane.getChildren().addAll(enemyCards,  playerInfoVBox);
                StackPane.setAlignment(enemyCards, Pos.TOP_CENTER);
                StackPane.setAlignment(playerInfoVBox, Pos.CENTER);
                //playerInfoStackPane.getChildren().add(playerInfoVBox);

                //adding everything into parent HBox
                HBox hBox = new HBox();
                hBox.setSpacing(20);
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.getChildren().addAll(playerInfoStackPane, energyInfoVBox);

                //adding it to the GridPane cell
                playerInfoGrid.add(hBox, i-1, 0);
            }

        }
    }
}
