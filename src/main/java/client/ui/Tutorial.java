package client.ui;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;


public class Tutorial {

    private StackPane baseStackPane;
    private StackPane topStackpane;
    private StackPane programmingSpaceStackPane;
    private StackPane gameboardStackPane;
    private StackPane handStackPane;
    private GridPane handGrid;
    private GridPane programmingGrid;
    private GridPane gameboard;
    private Label description;
    String username;
    int counter;

    public Tutorial(StackPane baseStackPane, StackPane programmingSpaceStackPane, StackPane gameboardStackPane,
                    StackPane handStackPane, GridPane handGrid, GridPane programmingGrid, GridPane gameboard, String username) {
        this.baseStackPane = baseStackPane;
        this.topStackpane = new StackPane();
        this.programmingSpaceStackPane = programmingSpaceStackPane;
        this.gameboardStackPane = gameboardStackPane;
        this.handStackPane = handStackPane;
        this.handGrid = handGrid;
        this.programmingGrid = programmingGrid;
        this.gameboard = gameboard;
        this.description = new Label();
        this.username = username;
        this.counter = 0;
    }

    public void loadGameWindowTutorial() {
        //Pane basePane = new Pane();
        StackPane handStackPane = new StackPane();
        StackPane programmingStackPane = new StackPane();
        StackPane gameboardStackPane = new StackPane();
        Label description = new Label();

        //basePane.setStyle("-fx-background-color: black;" + "-fx-opacity: 0.54;");
        topStackpane.setAlignment(Pos.CENTER);
        topStackpane.setStyle("-fx-background-color: black;" + "-fx-opacity: 0.84;");

        handStackPane.setAlignment(Pos.CENTER);
        handStackPane.setOpacity(1);

        programmingStackPane.setAlignment(Pos.CENTER);
        programmingStackPane.setOpacity(1);

        gameboardStackPane.setAlignment(Pos.CENTER);
        gameboardStackPane.setOpacity(1);

        description.setText("Hello " + username);
        description.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
        description.setOpacity(1);

        topStackpane.getChildren().add(description);
        baseStackPane.getChildren().add(topStackpane);

        topStackpane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                counter++;
                if (counter == 1) {
                    description.setText("Welcome to RoboRally");
                }
                if (counter == 2) {
                    description.setText("At first I would like to introduce you to this game\n" + "" +
                            "So let's get started !");
                }
                if (counter == 3) {
                    description.setText("What you can see here is the game screen.");
                }
                if (counter == 4) {
                    description.setText("RoboRally consists of setting your robot and 3 phases:\n"
                            + "1. Upgrade Phase\n" + "2. Programming Phase\n"
                                        + "3. Activation Phase");
                }

                if (counter == 5) {
                    description.setText("In the Upgrade Phase ....\n\n" +
                            "In the Programming Phase ...\n\n"
                            + "In the Activation Phase ...");
                }
                if (counter == 6) {
                    description.setText("Now let's look at your hand.\n" +
                            "Here you can see all of your cards\nyou have currently in your hand.\n" +
                            "There are different kind of cards:");
                    Pane pane = new Pane();
                    pane.setStyle("-fx-background-color: transparent;");
                    handStackPane.getChildren().addAll(handGrid, pane, description);
                    topStackpane.getChildren().addAll(handStackPane);
                    /*
                    VBox vbox1 = new VBox(8);
                    VBox vbox2 = new VBox(8);
                    HBox hBox1 = new HBox(5);
                    HBox hBox2 = new HBox(5);
                    HBox hBox3 = new HBox(5);

                    Label description2 = new Label("Upgrade Phase");
                    description2.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description2.setOpacity(1);
                    Label description3 = new Label("lel");
                    description3.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description3.setOpacity(1);
                    Label description4 = new Label("Programming Phase");
                    description4.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description4.setOpacity(1);
                    Label description5 = new Label("sheesh");
                    description5.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description5.setOpacity(1);
                    Label description6 = new Label("Activation Phase");
                    description6.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description6.setOpacity(1);
                    Label description7 = new Label("nub");
                    description7.setStyle("-fx-text-fill: #ffffff;" + "-fx-font-weight: bold;" + "-fx-font-size: 43;");
                    description7.setOpacity(1);

                    hBox1.getChildren().addAll(description2, description3);
                    hBox2.getChildren().addAll(description4, description5);
                    hBox3.getChildren().addAll(description6, description7);
                    vbox2.getChildren().addAll(hBox1, hBox2, hBox3);
                    vbox1.getChildren().addAll(description, vbox2);

                    topStackpane.getChildren().add(vbox1);
                    topStackpane.setAlignment(Pos.CENTER);

                     */

                }
                if (counter == 7) {
                    topStackpane.getChildren().remove(handStackPane);
                    description.setText("Now let's have a look at the game board.");

                    Pane pane = new Pane();
                    pane.setStyle("-fx-background-color: transparent;");
                    gameboardStackPane.getChildren().addAll(gameboard, pane, description);
                    topStackpane.getChildren().addAll(gameboardStackPane);
                }
                if (counter == 8) {
                    description.setText("You can place your robot in the starting field");
                }
                if (counter == 9) {
                    description.setText("There are also different kind of fields.\n" +
                            "So be careful where you lead your robot.");
                }
                if (counter == 10) {
                    topStackpane.getChildren().remove(gameboardStackPane);
                    description.setText("On the right side is your programming field.\n" +
                            "Use it for your moving cards in your hand.\n" +
                            "Choose wisely. Other players might have malicious intent");
                    Pane pane = new Pane();
                    pane.setStyle("-fx-background-color: transparent;");
                    programmingSpaceStackPane.getChildren().add(pane);
                    programmingStackPane.getChildren().addAll(programmingSpaceStackPane, description);
                    topStackpane.getChildren().add(programmingStackPane);
                }
                if (counter == 11) {
                    topStackpane.getChildren().remove(programmingStackPane);
                    description.setText("Now get ready and bet on the victory");
                    topStackpane.getChildren().add(description);
                }
                if (counter == 12) {
                    baseStackPane.getChildren().remove(topStackpane);
                }
            }
        });
    }

}
