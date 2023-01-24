package client.viewmodel;

import client.changesupport.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import client.ui.PlayerGameInfo;
import client.ui.Tutorial;
import communication.Message;
import game.board.Tile;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    private Pane gameboardRegion;
    @FXML
    private ColumnConstraints gameboardColumn;
    public Pane programspacePane;
    @FXML
    private Button chatButton;
    @FXML
    private TextField chatTextfield;
    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private GridPane gameboard;
    @FXML
    private Pane programCard1, programCard2, programCard3, programCard4, programCard5, programCard6, programCard7, programCard8, programCard9;
    @FXML
    private GridPane programmingGrid;
    @FXML
    private Pane programmingPane1, programmingPane2, programmingPane3, programmingPane4, programmingPane5;
    @FXML
    private GridPane handGrid;
    @FXML
    private GridPane playerInfoGrid;

    @FXML
    private Pane cardDeck;
    @FXML
    private StackPane baseStackPane;
    @FXML
    private StackPane handStackPane;
    @FXML
    private StackPane programmingSpaceStackPane;
    @FXML
    private StackPane gameboardStackPane;

    private ModelChat modelChat;
    private ModelGame modelGame;
    private ModelUser modelUser;

    private int height = 150;
    private int columnIndex;
    private PlayerGameInfo playerGameInfo;
    //private ObservableList<String> handCardsUI;

    private Tutorial tutorial;

    private double gameboardTileWidth;

    private NotifyChangeSupport notifyChangeSupport;
    private final Logger logger = LogManager.getLogger(ViewModelGameWindow.class);

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
        for (Node child : children) {
            logger.debug("Initialize handGrid: " + child);
        }
        selectStarttile();

        //handCardsUI = FXCollections.observableArrayList(modelGame.getMyHandCards());

        /*
        handCardsUI.addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {

                while(c.next()) {
                    if(c.wasUpdated()) {
                        System.out.println("wasUpdated");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasAdded()) {
                        System.out.println("wasAdded");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasRemoved()) {
                        System.out.println("wasRemoved");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasPermutated()) {
                        System.out.println("wasPermutated");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                    if(c.wasReplaced()) {
                        System.out.println("wasReplaced");
                        notifyChangeSupport.updateProgrammingHandCards();
                    }
                }
            }
        });

         */

        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());

        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {@Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            chatScrollPane.setVvalue((Double) newValue);
        }
        });

        gameboardRegion.widthProperty().addListener((obs, oldValue, newValue) -> {
            double width = newValue.doubleValue() * 0.04;
            this.gameboardTileWidth = width;
            updateWidth(gameboardTileWidth);
        });

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

        placeTiles(map);

        //StartupDispenseCards("/textures/cards/kartendeckStapel.png", cardDeck, programCard1);


        /*
        this.tutorial = new Tutorial(baseStackPane, programmingSpaceStackPane, gameboardStackPane,
                handStackPane, handGrid, programmingGrid, gameboard, modelUser.usernameProperty().get());
        tutorial.loadGameWindowTutorial();
         */
    }


    private void updateWidth(double width) {
        for(Node node: gameboard.getChildren()){
            if(node instanceof ImageView){
                ImageView img = (ImageView)node;
                img.setFitWidth(width);
                img.setPreserveRatio(true);
            }
        }
    }

    public void receivedMessage() {
        Message message = null;
        try {
            message = modelChat.getMESSSAGES().take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert message != null;
        if (message.getMessageBody().isPrivate()) {
            String privateMessage = message.getMessageBody().getMessage();
            privateMessageToChat(privateMessage);
        }
        else {
            String groupMessage = message.getMessageBody().getMessage();
            groupMessageToChat(groupMessage);
        }
    }

    public void chatButtonOnAction() {
        /*String user = usersChoiceBox.getSelectionModel().getSelectedItem();
        int userID = modelUser.getUserID();

        if(user.equals("All")) {
            modelChat.sendGroupMessage(userID);
        }
        else {
            modelChat.sendPrivateMessage(userID);
        }
         */

        int userID = modelUser.userIDProperty().get();
        modelChat.sendGroupMessage();
        addToChat(chatTextfield.getText());
    }

    public void addToChat(String message) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
            "-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        chatVBox.getChildren().add(hBox);

        chatTextfield.clear();
        chatTextfield.requestFocus();
    }

    public void groupMessageToChat(String privateMessage) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(privateMessage);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
            "-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(hBox);
            }
        });
    }

    public void privateMessageToChat(String groupMessage) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(groupMessage);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle(
            "-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(208,167,15);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(hBox);
            }
        });
    }

    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
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
                    modelGame.sendStarttileCoordinates(colIndex, rowIndex);
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
        Message robotMovement;
        try {
            robotMovement = modelGame.getPLAYERMOVEMENTS().take();
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
        Platform.runLater(() ->{
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
                boolean success = false;
                if (target.getChildren().isEmpty()) {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        Image data = db.getImage();
                        ImageView card = new ImageView(data);
                        card.setFitHeight(height);
                        card.setPreserveRatio(true);
                        //add image from dragboard to slots (Pane)
                        target.getChildren().add(card);
                        success = true;
                        Pane source = (Pane) handGrid.getChildren().get(columnIndex);
                        source.getChildren().clear();
                        //Send cardtype and register of played Card:
                        String cardName = switch (data.getUrl()) {
                            case "Move1.png" -> "MoveI";
                            case "Move2.png" -> "MoveII";
                            case "Move3.png" -> "MoveIII";
                            case "leftTurn.png" -> "TurnLeft";
                            case "rightTurn.png" -> "TurnRight";
                            case "uTurn.png" -> "UTurn";
                            case "moveBack.png" -> "BackUp";
                            case "powerUp.png" -> "PowerUp";
                            case "Again.png" -> "Again";
                            default -> "";
                        };
                        int targetIndex = GridPane.getColumnIndex(target);
                        modelGame.sendSelectedCard(cardName, targetIndex);
                    }
                    event.setDropCompleted(success);
                    event.consume();
                }
                else {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        Image data = db.getImage();
                        Pane source = (Pane) handGrid.getChildren().get(columnIndex);
                        ImageView card = new ImageView(data);
                        card.setFitHeight(height);
                        card.setPreserveRatio(true);
                        source.getChildren().add(card);
                        logger.debug(columnIndex);
                    }
                }
                ObservableList<Node> children = programmingGrid.getChildren();
                logger.debug("ProgramGrid now: ");
                for (Node child : children) {
                    logger.debug(child);
                    if (child instanceof Pane) {
                        Pane pane = (Pane) child;
                        logger.debug("Pane contains: " + pane.getChildren());
                    }
                }
            }

        });
    }

    public void onRightClickRemoveProgrammingCard(Pane target) {
        target.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    Object targetNode = event.getTarget();
                    if (targetNode instanceof ImageView) {
                        ImageView card = (ImageView) targetNode;
                        Image data = card.getImage();
                        target.getChildren().remove(card);
                        //Resets register when card is taken out:
                        int targetIndex = GridPane.getColumnIndex(target);
                        modelGame.sendSelectedCard(null, targetIndex);
                        int index = getFirstFreeSlot();
                        if (index != -1) {
                            Pane emptyPane = (Pane) handGrid.getChildren().get(index);
                            ImageView newCard = new ImageView(data);
                            newCard.setFitHeight(height);
                            newCard.setPreserveRatio(true);
                            emptyPane.getChildren().add(newCard);
                        }
                    }
                }
            }
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

    private void StartupDispenseCards(String imagePath, Pane fromPane, Pane toPane) {
        InputStream inputPath = getClass().getResourceAsStream(imagePath);
        ImageView imageView = new ImageView(new Image(inputPath));
        fromPane.getChildren().add(imageView);
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(10));
        transition.setNode(imageView);
        transition.setToX(toPane.getLayoutX());
        transition.setToY(toPane.getLayoutY());
        transition.play();
    }

    public void fillHandCards() {
        ObservableList<Node> children = handGrid.getChildren();
        logger.debug("fillHandCards Start:");
        for (Node child : children) {
            logger.debug(child);
            if (child instanceof Pane) {
                Pane pane = (Pane) child;
                logger.debug("Pane contains: " + pane.getChildren());
            }
        }
        logger.debug(" ");
        logger.debug("Filling your cards");
        ArrayList<String> handCards = new ArrayList<>(modelGame.getMyHandCards());
        Platform.runLater(() -> {
            logger.debug("handGrid children size: " + handGrid.getChildren().size());
            for (int i = 0; i < 9; i++) {
                if (handGrid.getChildren().get(i) instanceof Pane) {
                    Pane pane = (Pane) handGrid.getChildren().get(i);
                    switch (handCards.get(i)) {
                        case "MoveI" -> {
                            InputStream input = getClass().getResourceAsStream(
                                "/textures/cards/Move1.png");
                            Image image = new Image(input);
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(90);
                            imageView.setFitWidth(60);
                            pane.getChildren().add(imageView);
                        }
                        case "MoveII" -> {
                            InputStream input2 = getClass().getResourceAsStream(
                                "/textures/cards/Move2.png");
                            Image image2 = new Image(input2);
                            ImageView imageView2 = new ImageView(image2);
                            imageView2.setFitHeight(90);
                            imageView2.setFitWidth(60);
                            pane.getChildren().add(imageView2);
                        }
                        case "MoveIII" -> {
                            InputStream input3 = getClass().getResourceAsStream(
                                "/textures/cards/Move3.png");
                            Image image3 = new Image(input3);
                            ImageView imageView3 = new ImageView(image3);
                            imageView3.setFitHeight(90);
                            imageView3.setFitWidth(60);
                            pane.getChildren().add(imageView3);
                        }
                        case "TurnLeft" -> {
                            InputStream input4 = getClass().getResourceAsStream(
                                "/textures/cards/leftTurn.png");
                            Image image4 = new Image(input4);
                            ImageView imageView4 = new ImageView(image4);
                            imageView4.setFitHeight(90);
                            imageView4.setFitWidth(60);
                            pane.getChildren().add(imageView4);
                        }
                        case "TurnRight" -> {
                            InputStream input5 = getClass().getResourceAsStream(
                                "/textures/cards/rightTurn.png");
                            Image image5 = new Image(input5);
                            ImageView imageView5 = new ImageView(image5);
                            imageView5.setFitHeight(90);
                            imageView5.setFitWidth(60);
                            pane.getChildren().add(imageView5);
                        }
                        case "UTurn" -> {
                            InputStream input6 = getClass().getResourceAsStream(
                                "/textures/cards/uTurn.png");
                            Image image6 = new Image(input6);
                            ImageView imageView6 = new ImageView(image6);
                            imageView6.setFitHeight(90);
                            imageView6.setFitWidth(60);
                            pane.getChildren().add(imageView6);
                        }
                        case "BackUp" -> {
                            InputStream input7 = getClass().getResourceAsStream(
                                "/textures/cards/moveBack.png");
                            Image image7 = new Image(input7);
                            ImageView imageView7 = new ImageView(image7);
                            imageView7.setFitHeight(90);
                            imageView7.setFitWidth(60);
                            pane.getChildren().add(imageView7);
                        }
                        case "PowerUp" -> {
                            InputStream input8 = getClass().getResourceAsStream(
                                "/textures/cards/powerUp.png");
                            Image image8 = new Image(input8);
                            ImageView imageView8 = new ImageView(image8);
                            imageView8.setFitHeight(90);
                            imageView8.setFitWidth(60);
                            pane.getChildren().add(imageView8);
                        }
                        case "Again" -> {
                            InputStream input9 = getClass().getResourceAsStream(
                                "/textures/cards/Again.png");
                            Image image9 = new Image(input9);
                            ImageView imageView9 = new ImageView(image9);
                            imageView9.setFitHeight(90);
                            imageView9.setFitWidth(60);
                            pane.getChildren().add(imageView9);
                        }
                    }
                } else {
                    logger.debug("Element at index " + i + " is not a Pane");
                }
            }
            logger.debug(" ");
            logger.debug("fillYourHandCards End: ");
            for (Node child : children) {
                logger.debug(child);
                if (child instanceof Pane) {
                    Pane pane = (Pane) child;
                    logger.debug("Pane contains: " + pane.getChildren());
                }
            }
        });

    }

    public void startTimer() {
    }
}

