package client.viewmodel;

import client.RoboRallyStart;
import client.changesupport.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import client.ui.PlayerGameInfo;
import client.ui.Tutorial;
import communication.Message;
import communication.MessageType;
import game.board.Tile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ViewModel for gamescreen
 *
 * @author Tobias, Benedikt
 * @version 0.1
 */
public class ViewModelGameWindow {
    @FXML
    private ColumnConstraints gameboardColumn;
    @FXML
    private Button chatButton;
    @FXML
    private TextField chatTextfield;
    @FXML
    private VBox chatVBox, logVBox;
    @FXML
    private ScrollPane chatScrollPane, logScrollPane;
    @FXML
    private GridPane deckGrid, gameboard, programmingGrid;
    @FXML
    private Pane gameboardRegion;
    @FXML
    private Pane programCard1, programCard2, programCard3, programCard4, programCard5, programCard6, programCard7, programCard8, programCard9;
    @FXML
    private Pane programmingPane1, programmingPane2, programmingPane3, programmingPane4, programmingPane5;
    @FXML
    private GridPane handGrid, playerInfoGrid;
    @FXML
    private Pane upgradeDeck, damageDeck;
    @FXML
    private Label timerLabel, currentPhaseLabel, currentActivePlayerLabeL, myEnergyLabel;
    @FXML
    private HBox myPlayerInfoHBox;
    @FXML
    private VBox myPlayerInfoVBox;
    @FXML
    private StackPane myEnergyStackPane, baseStackPane, handStackPane, ProgrammingSpaceStackPane, gameboardStackPane;
    @FXML
    private ProgressBar myEnergyBar;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    public Pane programspacePane;
    @FXML
    private Pane toprightRow;

    private ModelChat modelChat;
    private ModelGame modelGame;
    private ModelUser modelUser;
    private NotifyChangeSupport notifyChangeSupport;
    private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);
    private PlayerGameInfo playerGameInfo;
    //private ObservableList<String> handCardsUI;
    private Tutorial tutorial;
    private double gameboardTileWidth;
    private double programcardsWidth;
    private int columnIndex;
    private boolean isClickable;
    private boolean isDroppedSuccessfully = false;
    private int registerCounter = 0;

    public ViewModelGameWindow() {
        this.modelChat = ModelChat.getInstance();
        this.modelGame = ModelGame.getInstance();
        this.modelUser = ModelUser.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        notifyChangeSupport.setViewModelGameWindow(this);
    }

    public void initialize() {
        ArrayList<ArrayList<ArrayList<Tile>>> map = modelGame.getGameMap();
        ObservableList<Node> children = handGrid.getChildren();

        selectStarttile();

        loadDecks();

        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());

        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            chatScrollPane.setVvalue((Double) newValue);
        }
        });

        logVBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                logScrollPane.setVvalue((Double) newValue);
            }
        });

        gameboardRegion.widthProperty().addListener((obs, oldValue, newValue) -> {
            double width = newValue.doubleValue();
            this.gameboardTileWidth = width * 0.04;
            this.programcardsWidth = width * 0.07;
            updateWidth(width);
        });

        //TODO: set phase
        //currentPhaseLabel.textProperty().bind("");

        //TODO: setActivePlayer
        //currentActivePlayerLabeL.textProperty().bind("");

        myEnergyBar.progressProperty().bind(modelGame.energyProperty());
        myEnergyLabel.textProperty().bind(myEnergyBar.progressProperty().asString());
        myEnergyLabel.setStyle("-fx-text-fill: red;" + "-fx-font-weight: bold;");
        StackPane.setAlignment(myEnergyBar, Pos.CENTER);
        StackPane.setAlignment(myEnergyLabel, Pos.CENTER);

        playerGameInfo = new PlayerGameInfo(playerInfoGrid, modelGame.getPlayerList());
        playerGameInfo.loadPlayerInfo();

        setOnDragDetected(programCard1);
        setOnDragDetected(programCard2);
        setOnDragDetected(programCard3);
        setOnDragDetected(programCard4);
        setOnDragDetected(programCard5);
        setOnDragDetected(programCard6);
        setOnDragDetected(programCard7);
        setOnDragDetected(programCard8);
        setOnDragDetected(programCard9);

        setOnDragOver(programmingPane1);
        setOnDragOver(programmingPane2);
        setOnDragOver(programmingPane3);
        setOnDragOver(programmingPane4);
        setOnDragOver(programmingPane5);

        setOnDragEntered(programmingPane1);
        setOnDragEntered(programmingPane2);
        setOnDragEntered(programmingPane3);
        setOnDragEntered(programmingPane4);
        setOnDragEntered(programmingPane5);

        setOnDragExited(programmingPane1);
        setOnDragExited(programmingPane2);
        setOnDragExited(programmingPane3);
        setOnDragExited(programmingPane4);
        setOnDragExited(programmingPane5);

        setOnDragDropped(programmingPane1);
        setOnDragDropped(programmingPane2);
        setOnDragDropped(programmingPane3);
        setOnDragDropped(programmingPane4);
        setOnDragDropped(programmingPane5);

        onRightClickRemoveProgrammingCard(programmingPane1);
        onRightClickRemoveProgrammingCard(programmingPane2);
        onRightClickRemoveProgrammingCard(programmingPane3);
        onRightClickRemoveProgrammingCard(programmingPane4);
        onRightClickRemoveProgrammingCard(programmingPane5);

        setOnDragDone(programCard1);
        setOnDragDone(programCard2);
        setOnDragDone(programCard3);
        setOnDragDone(programCard4);
        setOnDragDone(programCard5);
        setOnDragDone(programCard6);
        setOnDragDone(programCard7);
        setOnDragDone(programCard8);
        setOnDragDone(programCard9);

        placeTiles(map);


        /*
        this.tutorial = new Tutorial(baseStackPane, programmingSpaceStackPane, gameboardStackPane,
                handStackPane, handGrid, programmingGrid, gameboard, modelUser.usernameProperty().get());
        tutorial.loadGameWindowTutorial();
         */
    }

    private void scaleImages(Parent parent, double width, double scaleFactor) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof ImageView) {
                ImageView img = (ImageView) node;
                img.setFitWidth(width * scaleFactor);
                img.setPreserveRatio(true);
            } else if (node instanceof Parent) {
                scaleImages((Parent) node, width, scaleFactor);
            }
        }
    }

    private void updateWidth(double width) {
        scaleImages(gameboard, width, 0.04);
        scaleImages(handGrid, width, 0.07);
        scaleImages(programmingGrid, width, 0.07);
        scaleImages(playerInfoGrid, width, 0.05);
        scaleImages(deckGrid, width, 0.07);
    }

    public void chatButtonOnAction() {
        /*
        String user = usersChoiceBox.getSelectionModel().getSelectedItem().getUsername();

        if(user.equals("Group")) {
            modelChat.sendGroupMessage();
            groupMessageToChat(chatTextfield.getText(), false);
        }
        else {
            int userNumber = usersChoiceBox.getSelectionModel().getSelectedIndex();
            int userID = modelGame.getPlayerList().getPlayerList().get(userNumber).getId();
            modelChat.sendPrivateMessage(userID);
            privateMessageToChat(chatTextfield.getText(), false);
        }

         */
    }

    public void receivedChatMessage() {
        Message message = null;
        try {
            message = modelChat.getCHAT_MESSSAGES().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert message != null;
        if (message.getMessageBody().isPrivate()) {
            String privateMessage = message.getMessageBody().getMessage();
            privateMessageToChat(privateMessage, true);
        }
        else {
            String groupMessage = message.getMessageBody().getMessage();
            groupMessageToChat(groupMessage, true);
        }
    }

    public void groupMessageToChat(String groupMessage, boolean received) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(groupMessage);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        if (!received) {
            chatVBox.getChildren().add(hBox);

            chatTextfield.clear();
            chatTextfield.requestFocus();
        }
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatVBox.getChildren().add(hBox);
                }
            });
        }
    }

    public void privateMessageToChat(String privateMessage, boolean received) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(privateMessage);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(213,170,16);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        if (!received) {
            chatVBox.getChildren().add(hBox);

            chatTextfield.clear();
            chatTextfield.requestFocus();
        }
        else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chatVBox.getChildren().add(hBox);
                }
            });
        }
    }

    public void receivedGameLogMessage() {
        Message logmessage = null;
        try {
            logmessage = modelChat.getGAME_LOG_MESSAGES().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert logmessage != null;
        logMessageToLogger(logmessage);
    }

    public void logMessageToLogger(Message logMessage) {
        if (logMessage.getMessageType().equals(MessageType.SelectionFinished)) {
            int clientID = logMessage.getMessageBody().getClientID();
            String username = modelGame.getPlayerList().getPlayer(clientID).getUsername();

            logMessageStyling(MessageType.SelectionFinished, username, null, null, null);
        }
        if (logMessage.getMessageType().equals(MessageType.CardPlayed)) {
            int clientID = logMessage.getMessageBody().getClientID();
            String username = modelGame.getPlayerList().getPlayer(clientID).getUsername();
            String card = logMessage.getMessageBody().getCard();

            logMessageStyling(MessageType.CardPlayed, username, card, null, null);
        }
        if (logMessage.getMessageType().equals(MessageType.TimerEnded)) {
            int[] clientIDs = Arrays.copyOf(logMessage.getMessageBody().getClientIDs(),logMessage.getMessageBody().getClientIDs().length);
            String[] players = new String[clientIDs.length];
            for (int i = 0; i < clientIDs.length; i++) {
                for (int j = 0; j < modelGame.getPlayerList().getPlayerList().size(); j++) {
                    if (clientIDs[i] == modelGame.getPlayerList().getPlayerList().get(j).getId()) {
                        String username = modelGame.getPlayerList().getPlayer(clientIDs[i]).getUsername();
                        players[i] = username;
                    }
                }
            }

            logMessageStyling(MessageType.TimerEnded, null, null, null, players);
        }
        if (logMessage.getMessageType().equals(MessageType.DrawDamage)) {
            String[] damageCards = Arrays.copyOf(logMessage.getMessageBody().getCards(), logMessage.getMessageBody().getCards().length);
            int clientID = logMessage.getMessageBody().getClientID();
            String username = "";
            for (int i = 0; i < modelGame.getPlayerList().getPlayerList().size(); i++) {
                if (modelGame.getPlayerList().getPlayer(clientID).getId() == clientID) {
                    username = modelGame.getPlayerList().getPlayer(clientID).getUsername();
                }
            }

            logMessageStyling(MessageType.DrawDamage, username, null, damageCards, null);
        }
        if (logMessage.getMessageType().equals(MessageType.GameFinished)) {
            int clientID = logMessage.getMessageBody().getClientID();
            String username = "";
            if (modelUser.userIDProperty().get() == clientID) {
                username = modelUser.getUsername();
            }
            else {
                username = modelGame.getPlayerList().getPlayer(clientID).getUsername();
            }
            Label win = new Label("WINNER");
            win.setStyle("-fx-text-fill: gold;");
            Label winnerLabel = new Label(username);
            winnerLabel.setStyle("-fx-text-fill: red;");
            VBox winnerVBox = new VBox();
            winnerVBox.setSpacing(20);
            StackPane winnerStackPane = new StackPane();
            winnerStackPane.setStyle("-fx-opacity: 0.84;");
            winnerStackPane.setAlignment(Pos.CENTER);
            Button winnerButton = new Button("Easy Win");
            winnerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        RoboRallyStart.switchScene("lobby.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            winnerVBox.getChildren().addAll(win, winnerLabel, winnerButton);
            winnerStackPane.getChildren().add(winnerVBox);

            baseStackPane.getChildren().add(winnerStackPane);
        }
        if (logMessage.getMessageType().equals(MessageType.CheckPointReached)) {
            int clientID = logMessage.getMessageBody().getClientID();
            //add message to logger
        }
    }

    public void logMessageStyling(MessageType messageType, String username, String card,
                                  String[] cards, String[] players) {

        TextFlow logTextFlow = new TextFlow();
        Text timeText = new Text();
        Text typeLogText = new Text();
        Text usernameText = new Text();
        Text logText = new Text();

        timeText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
        timeText.setText('(' + System.currentTimeMillis() + ')' + "  ");

        usernameText.setStyle("-fx-font-weight: bold;" + "-fx-text-fill: gray;");
        usernameText.setText(username);

        if (messageType.equals(MessageType.SelectionFinished)) {
            typeLogText.setText("[SelectionFinished] ");
            typeLogText.setStyle("-fx-text-fill: purple;" + "-fx-font-size: 8pt;");

            logText.setText(" finished his selection.");
            logText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
        }
        else if (messageType.equals(MessageType.CardPlayed)) {
            typeLogText.setText("[CardPlayed] ");
            typeLogText.setStyle("-fx-text-fill: orange;" + "-fx-font-size: 8pt;");

            logText.setText(" played following card: " + card);
            logText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
        }
        else if (messageType.equals(MessageType.TimerEnded)) {
            if (players.length == 0) {
                typeLogText.setText("[TimerEnded] ");
                typeLogText.setStyle("-fx-text-fill: blue;" + "-fx-font-size: 8pt;");

                logText.setText("Nobody was too slow.");
                logText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
            }
            else {
                typeLogText.setText("[TimerEnded] ");
                typeLogText.setStyle("-fx-text-fill: blue;" + "-fx-font-size: 8pt;");

                logText.setText(Arrays.toString(players) + " missed timing.");
                logText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
            }
        }
        else if (messageType.equals(MessageType.DrawDamage)) {
            typeLogText.setText("[DrawDamage] ");
            typeLogText.setStyle("-fx-text-fill: black;" + "-fx-font-size: 8pt;");

            logText.setText(Arrays.toString(cards));
            logText.setStyle("-fx-text-fill: gray;" + "-fx-font-size: 8pt;");
        }

        logTextFlow.getChildren().addAll(timeText, typeLogText, usernameText, logText);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logVBox.getChildren().add(logTextFlow);
            }
        });

    }
    public void reveivedGameEventMessage() {
        Message gamemessage = null;
        try {
            gamemessage = modelGame.getGAME_EVENT_MESSAGES().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert gamemessage != null;
        handleGameEvent(gamemessage);
    }

    private void handleGameEvent(Message gamemessage) {
        if (gamemessage.getMessageType().equals(MessageType.SelectionFinished)) {
            int clientID = gamemessage.getMessageBody().getClientID();
            if (clientID == modelUser.userIDProperty().get()){
                setProgramcardsUnmovable();
            }
        }
        if (gamemessage.getMessageType().equals(MessageType.CurrentCards)) {
            int clientID = gamemessage.getMessageBody().getClientID();
            if(clientID == gamemessage.getMessageBody().getClientID()){
                registerCounter++;
                if (registerCounter > 5) {
                    registerCounter = 1;
                }
                logger.debug("Current register: " + registerCounter);
                setShadowOnImage(registerCounter - 1);
            }
        }
        if (gamemessage.getMessageType().equals(MessageType.ReplaceCard)) {

        }
        if (gamemessage.getMessageType().equals(MessageType.DrawDamage)) {

        }
    }

    private void placeTiles(ArrayList<ArrayList<ArrayList<Tile>>> map) {
        for (int x = 0; x < map.size(); x++){
            for (int y = 0; y < map.get(x).size(); y++) {
                for (int i = 0; i < map.get(x).get(y).size(); i++){
                    map.get(x).get(y).get(i).makeImage(gameboard);
                }
            }
        }
    }

    public void selectStarttile() {
        gameboard.setOnMouseClicked(event -> {
            if (modelGame.activePlayerProperty().get()){
                Node target = event.getPickResult().getIntersectedNode();
                Integer colIndex = GridPane.getColumnIndex(target);
                Integer rowIndex = GridPane.getRowIndex(target);
                //check imageviews for type of tile
                if (target.getId() != null && target.getId().equals("StartTile")) {
                    //when starttile, insert img of robot and send coordinates
                    modelGame.sendStartTileCoordinates(colIndex, rowIndex);
                } else {
                    logger.debug("Is not a starttile");
                }
            }
            else {
                logger.debug("Not your turn");
            }
        });
    }

    public void robotSetPosition() {
        Platform.runLater(() ->{
        Message robotMovement;
        try {
            robotMovement = modelGame.getPLAYER_MOVEMENTS().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int x = robotMovement.getMessageBody().getX();
        int y = robotMovement.getMessageBody().getY();
        logger.debug("In viewModel - X: " + x + " | Y: " + y);
        int clientID = robotMovement.getMessageBody().getClientID();
        int figure;
        if (modelUser.userIDProperty().get() == clientID) {
            figure = modelGame.robotProperty().get();
        }
        else {
            figure = modelGame.getPlayerList().getPlayer(clientID).getRobot().getFigure();
        }

            logger.debug("RobotID in viewModel: " + figure);
        InputStream input = getClass().getResourceAsStream("/textures/robots/Robot_" + figure + "_bunt.png");
        Image im = new Image(input);
        ImageView img = new ImageView(im);
        img.setFitWidth(gameboardTileWidth);
        img.setPreserveRatio(true);
        img.setId("Robot_" + figure);
        //Current robot image gets searched and then removed
        for (Node node : gameboard.getChildren()) {
            if (node instanceof ImageView imageView) {
                if (imageView.getId() != null && imageView.getId().equals("Robot_" + figure)) {
                    gameboard.getChildren().remove(imageView);
                    break;
                }
            }
        }
        //adds new Image
        gameboard.add(img, x, y);
    });
    }

    public int getFirstFreeSlot() {
        for (int i = 0; i < handGrid.getChildren().size(); i++) {
            if(handGrid.getChildren().get(i) instanceof Pane) {
                Pane child = (Pane) handGrid.getChildren().get(i);
                if (child.getChildren().isEmpty()) {
                    logger.debug("Slot " + i + " is empty");
                    return i;
                }
            }
        }
        return -1;
    }

    public void fillHandCards() {
        //clear the handcards
        for (Node child : programmingGrid.getChildren()) {
            if (child instanceof Pane) {
                Pane pane = (Pane) child;
                pane.getChildren().clear();
            }
        }
        this.isClickable = true;
        ObservableList<Node> children = handGrid.getChildren();
        logger.debug("VM - fillHandCards Start:");
        ArrayList<String> handCards = new ArrayList<>(modelGame.getMyHandCards());
        Platform.runLater(() -> {
            logger.debug("VM - handGrid children size: " + handGrid.getChildren().size());
            for (int i = 0; i < 9; i++) {
                if (handGrid.getChildren().get(i) instanceof Pane pane) {
                    switch (handCards.get(i)) {
                        case "MoveI" -> {
                            InputStream input = getClass().getResourceAsStream(
                                "/textures/cards/Move1.png");
                            Image image = new Image(input);
                            ImageView imageView = new ImageView(image);
                            imageView.setId("MoveI");
                            imageView.setFitWidth(programcardsWidth);
                            imageView.setPreserveRatio(true);
                            pane.getChildren().add(imageView);
                        }
                        case "MoveII" -> {
                            InputStream input2 = getClass().getResourceAsStream(
                                "/textures/cards/Move2.png");
                            Image image2 = new Image(input2);
                            ImageView imageView2 = new ImageView(image2);
                            imageView2.setId("MoveII");
                            imageView2.setFitWidth(programcardsWidth);
                            imageView2.setPreserveRatio(true);
                            pane.getChildren().add(imageView2);
                        }
                        case "MoveIII" -> {
                            InputStream input3 = getClass().getResourceAsStream(
                                "/textures/cards/Move3.png");
                            Image image3 = new Image(input3);
                            ImageView imageView3 = new ImageView(image3);
                            imageView3.setId("MoveIII");
                            imageView3.setFitWidth(programcardsWidth);
                            imageView3.setPreserveRatio(true);
                            pane.getChildren().add(imageView3);
                        }
                        case "TurnLeft" -> {
                            InputStream input4 = getClass().getResourceAsStream(
                                "/textures/cards/leftTurn.png");
                            Image image4 = new Image(input4);
                            ImageView imageView4 = new ImageView(image4);
                            imageView4.setId("TurnLeft");
                            imageView4.setFitWidth(programcardsWidth);
                            imageView4.setPreserveRatio(true);
                            pane.getChildren().add(imageView4);
                        }
                        case "TurnRight" -> {
                            InputStream input5 = getClass().getResourceAsStream(
                                "/textures/cards/rightTurn.png");
                            Image image5 = new Image(input5);
                            ImageView imageView5 = new ImageView(image5);
                            imageView5.setId("TurnRight");
                            imageView5.setFitWidth(programcardsWidth);
                            imageView5.setPreserveRatio(true);
                            pane.getChildren().add(imageView5);
                        }
                        case "UTurn" -> {
                            InputStream input6 = getClass().getResourceAsStream(
                                "/textures/cards/uTurn.png");
                            Image image6 = new Image(input6);
                            ImageView imageView6 = new ImageView(image6);
                            imageView6.setId("UTurn");
                            imageView6.setFitWidth(programcardsWidth);
                            imageView6.setPreserveRatio(true);
                            pane.getChildren().add(imageView6);
                        }
                        case "BackUp" -> {
                            InputStream input7 = getClass().getResourceAsStream(
                                "/textures/cards/moveBack.png");
                            Image image7 = new Image(input7);
                            ImageView imageView7 = new ImageView(image7);
                            imageView7.setId("BackUp");
                            imageView7.setFitWidth(programcardsWidth);
                            imageView7.setPreserveRatio(true);
                            pane.getChildren().add(imageView7);
                        }
                        case "PowerUp" -> {
                            InputStream input8 = getClass().getResourceAsStream(
                                "/textures/cards/powerUp.png");
                            Image image8 = new Image(input8);
                            ImageView imageView8 = new ImageView(image8);
                            imageView8.setId("PowerUp");
                            imageView8.setFitWidth(programcardsWidth);
                            imageView8.setPreserveRatio(true);
                            pane.getChildren().add(imageView8);
                        }
                        case "Again" -> {
                            InputStream input9 = getClass().getResourceAsStream(
                                "/textures/cards/Again.png");
                            Image image9 = new Image(input9);
                            ImageView imageView9 = new ImageView(image9);
                            imageView9.setId("Again");
                            imageView9.setFitWidth(programcardsWidth);
                            imageView9.setPreserveRatio(true);
                            pane.getChildren().add(imageView9);
                        }
                        case "Spam" -> {
                            InputStream input10 = getClass().getResourceAsStream(
                                "/textures/cards/SPAM.png");
                            Image image10 = new Image(input10);
                            ImageView imageView10 = new ImageView(image10);
                            imageView10.setId("Spam");
                            imageView10.setFitWidth(programcardsWidth);
                            imageView10.setPreserveRatio(true);
                            pane.getChildren().add(imageView10);
                        }
                        case "Worm" -> {
                            InputStream input11 = getClass().getResourceAsStream(
                                "/textures/cards/WORM.png");
                            Image image11 = new Image(input11);
                            ImageView imageView11 = new ImageView(image11);
                            imageView11.setId("Worm");
                            imageView11.setFitWidth(programcardsWidth);
                            imageView11.setPreserveRatio(true);
                            pane.getChildren().add(imageView11);
                        }
                        case "Virus" -> {
                            InputStream input12 = getClass().getResourceAsStream(
                                "/textures/cards/VIRUS.png");
                            Image image12 = new Image(input12);
                            ImageView imageView12 = new ImageView(image12);
                            imageView12.setId("Virus");
                            imageView12.setFitWidth(programcardsWidth);
                            imageView12.setPreserveRatio(true);
                            pane.getChildren().add(imageView12);
                        }
                        case "Trojan" -> {
                            InputStream input13 = getClass().getResourceAsStream(
                                "/textures/cards/TROJAN_HORSE.png");
                            Image image13 = new Image(input13);
                            ImageView imageView13 = new ImageView(image13);
                            imageView13.setId("Trojan");
                            imageView13.setFitWidth(programcardsWidth);
                            imageView13.setPreserveRatio(true);
                            pane.getChildren().add(imageView13);
                        }
                    }
                } else {
                    logger.debug("Element at index " + i + " is not a Pane");
                }
            }
            logger.debug(" ");
            for (Node child : children) {
                logger.debug(child);
                if (child instanceof Pane) {
                    Pane pane = (Pane) child;
                    logger.debug("VM - handpane now contains: " + pane.getChildren());
                }
            }
        });

    }

    private void loadDecks (){
        InputStream upgradeImg = getClass().getResourceAsStream("/textures/cards/upgradeDeck.png");
        Image imUpgrade = new Image(upgradeImg);
        ImageView imgUpgrade = new ImageView(imUpgrade);
        imgUpgrade.setFitWidth(programcardsWidth);
        imgUpgrade.setPreserveRatio(true);
        upgradeDeck.getChildren().add(imgUpgrade);

        InputStream damageImg = getClass().getResourceAsStream("/textures/cards/damageDeck.png");
        Image imDamage = new Image(damageImg);
        ImageView imgDamage = new ImageView(imDamage);
        imgDamage.setFitWidth(programcardsWidth);
        imgDamage.setPreserveRatio(true);
        damageDeck.getChildren().add(imgDamage);
    }


    public void setOnDragDetected(Pane source) {

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //drag was detected, start drag-and-drop gesture
                logger.debug("Drag detected");

                //Any TransferMode is allowed
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);
                //put image on dragboard
                ClipboardContent content = new ClipboardContent();
                ImageView card = (ImageView) source.getChildren().get(0);
                content.putImage(card.getImage());
                content.putString(card.getId());
                db.setContent(content);
                event.consume();
                columnIndex = handGrid.getChildren().indexOf(source);
                source.getChildren().clear();
                logger.debug("Element from Column " + columnIndex);
            }
        });
    }

    public void setOnDragOver(Pane target) {
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                //logger.debug("setOnDragOver");

                //data is dragged over target
                //accept it only if it is not dragged from the same node and if it hasImage
                if (event.getGestureSource() != target && event.getDragboard().hasImage()) {
                    //allow for copying and moving
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
    }

    public void setOnDragEntered(Pane target) {
        target.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("setOnDragEntered");
                //drag-and-drop gesture entered target
                //Show entering visually
                if (event.getGestureSource() != target && event.getDragboard().hasImage()) {
                    target.setStyle("-fx-border-color: green;");
                }
                event.consume();
            }
        });
    }

    public void setOnDragExited(Pane target) {
        target.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("setOnDragExited");
                //mouse moves out of enntered area
                //remove visuals
                target.setStyle("-fx-border-color: transparent;");
                event.consume();
            }
        });

    }

    public void setOnDragDropped(Pane target) {
        target.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("setOnDragDropped");
                //data dropped
                isDroppedSuccessfully = true;
                boolean success = false;
                logger.debug("VM - target Children: " + target.getChildren());
                if (target.getChildren().isEmpty()) {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        logger.debug("VM - ColumnIndex of Target: " + GridPane.getColumnIndex(target));
                        String cardName = db.getString();
                        Image data = db.getImage();
                        ImageView card = new ImageView(data);
                        card.setId(cardName);
                        card.setFitWidth(programcardsWidth);
                        card.setPreserveRatio(true);
                        target.getChildren().add(card);
                        int targetIndex = GridPane.getColumnIndex(target) + 1;
                        logger.debug("VM - 1 Cardname: " + cardName);
                        logger.debug("VM - 3 target for message: " + targetIndex);
                        logger.debug("VM - 4 SendSelectedCard sent: " + cardName);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        modelGame.sendSelectedCard(cardName, targetIndex);
                        success = true;
                    }

                }
                else {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        Image data = db.getImage();
                        Pane source = (Pane) handGrid.getChildren().get(columnIndex);
                        ImageView card = new ImageView(data);
                        card.setFitWidth(programcardsWidth);
                        card.setPreserveRatio(true);
                        source.getChildren().add(card);
                        logger.debug("VM - Card returned to Column: " + columnIndex);
                        success = true;
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }

        });

    }

    public void setOnDragDone(Pane source) {
        source.setOnDragDone(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                logger.debug("Drag done - completed: " + event.isDropCompleted());
                logger.debug("Drag done - completed in variable: " + event.isDropCompleted());
                if (!isDroppedSuccessfully) {
                    // drag and drop failed, add the card back to the source pane
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        Image data = db.getImage();
                        String cardName = db.getString();
                        ImageView card = new ImageView(data);
                        card.setId(cardName);
                        card.setFitWidth(programcardsWidth);
                        card.setPreserveRatio(true);
                        source.getChildren().add(card);
                    }
                }
                event.consume();
                isDroppedSuccessfully = false;
            }
        });
    }


    public void onRightClickRemoveProgrammingCard(Pane target) {
        target.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    Object targetNode = event.getTarget();
                    if (isClickable){
                        if (targetNode instanceof ImageView) {
                            ImageView card = (ImageView) targetNode;
                            String cardName = card.getId();
                            Image data = card.getImage();
                            target.getChildren().remove(card);
                            //Resets register when card is taken out:
                            int targetIndex = GridPane.getColumnIndex(target);
                            modelGame.sendSelectedCard(null, targetIndex);
                            int index = getFirstFreeSlot();
                            if (index != -1) {
                                Pane emptyPane = (Pane) handGrid.getChildren().get(index);
                                ImageView newCard = new ImageView(data);
                                newCard.setFitWidth(programcardsWidth);
                                newCard.setPreserveRatio(true);
                                newCard.setId(cardName);
                                emptyPane.getChildren().add(newCard);
                            }
                        }
                    }
                    else {
                        logger.debug("VM - Rightclick not possible in current phase");
                    }
                }
            }
        });
    }

    public void startTimer() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<>() {
            int timer = 30;
            @Override
            public void handle(ActionEvent event) {
                timerLabel.setText(String.valueOf(timer));
                if (timer <= 10) {
                    timerLabel.setStyle("-fx-text-fill: red;");
                }
                timer--;
                if (timer <= 0) {
                    timeline.stop();
                    //TODO: something could happen after timer ended
                }
            }
        }));
        timeline.setCycleCount(30);
        timeline.play();
    }

    public void setRobotAlignment() {
        //set Robot Alignment
    }

    public void setProgramcardsUnmovable () {
        this.isClickable = false;
    }

    private void setShadowOnImage(int currentRegister) {
        Pane targetPane = (Pane) programmingGrid.getChildren().get(currentRegister);
        //Going through the children of the pane for selecting the ImageView
        for (Node node : targetPane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView image = (ImageView) node;
                image.setEffect(new DropShadow(20, Color.WHITE));
                Timer timer = new Timer();
                //effect will be deleted after 3 sek
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> image.setEffect(null));
                    }
                }, 3000);
                break;
            }
        }
    }


    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }

}

