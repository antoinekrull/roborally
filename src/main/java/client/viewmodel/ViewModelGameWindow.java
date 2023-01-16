package client.viewmodel;

import client.connection.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import communication.Message;
import game.board.Tile;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * ViewModel for gamescreen
 *
 * @author Tobias, Benedikt
 * @version 0.1
 */

public class ViewModelGameWindow {

    public static ColumnConstraints gameboardTileColumn;
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
    private ImageView programCard1, programCard2, programCard3, programCard4, programCard5, programCard6, programCard7, programCard8, programCard9;
    @FXML
    private GridPane programmingGrid;
    @FXML
    private Pane programmingPane1, programmingPane2, programmingPane3, programmingPane4, programmingPane5;
    @FXML
    private GridPane handGrid;
    @FXML
    private Region region0_1;

    //buttons for cards

    private ModelChat modelChat;
    private ModelGame modelGame;
    private ModelUser modelUser;

    private int height = 150;
    private int columnIndex;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelGameWindow() {
        this.modelChat = ModelChat.getInstance();
        this.modelGame = ModelGame.getInstance();
        this.modelUser = ModelUser.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        notifyChangeSupport.setViewModelGameWindow(this);
    }

    public void initialize() {
        //TODO: Tiles resizeable
        ArrayList<ArrayList<ArrayList<Tile>>> map = modelGame.getGameMap();
        placeTiles(map);
        //TODO: Playerlist in server/viewmodel
        /* PlayerList playerList = modelGame.getUsers();
        placeRobots(playerList);
        */

        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());
        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {@Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            chatScrollPane.setVvalue((Double) newValue);
        }
        });

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

        onRightClickRemoveProgrammingcard(programmingPane1);
        onRightClickRemoveProgrammingcard(programmingPane2);
        onRightClickRemoveProgrammingcard(programmingPane3);
        onRightClickRemoveProgrammingcard(programmingPane4);
        onRightClickRemoveProgrammingcard(programmingPane5);

        region0_1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Integer colIndex = GridPane.getColumnIndex(region0_1);
                Integer rowIndex = GridPane.getRowIndex(region0_1);
                System.out.println(colIndex + " " + rowIndex);
            }
        });

        gameboard.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node target = (Node) event.getTarget();
                if (target != gameboard) {
                    Node parent;
                    while ((parent = target.getParent()) != gameboard) {
                        target = parent;
                    }
                }
                Integer colIndex = GridPane.getColumnIndex(target);
                Integer rowIndex = GridPane.getRowIndex(target);
                System.out.println(colIndex + " " + rowIndex);
            }
        });
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
        ;
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
                    System.out.println("("+x+"; "+y+"): "+map.get(x).get(y).get(i).getType()+" Tile");
                    map.get(x).get(y).get(i).makeImage(gameboard);
                }
            }
        }
    }

    /*


    private void placeRobots(PlayerList playerList) {
        for (int x = 0; x < playerList.getPlayerList().size(); x++){
            playerList.getPlayerList().get(x).getRobot().makeImage(gameboard);
        }
    }
    */
    public void setOnDragDetected(ImageView source) {

        source.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //drag was detected, start drag-and-drop gesture
                System.out.println("Drag detected");

                //Any TransferMode is allowed
                Dragboard db = source.startDragAndDrop(TransferMode.ANY);

                //put image on dragboard
                ClipboardContent content = new ClipboardContent();
                content.putImage(source.getImage());
                db.setContent(content);

                event.consume();
                columnIndex = handGrid.getChildren().indexOf(source);
                ((ImageView) handGrid.getChildren().get(columnIndex)).setImage(null);
                System.out.println(columnIndex);

            }
        });
    }

    public void setOnDragOver(Pane target) {
        target.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
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
                        handGrid.getChildren().remove(columnIndex);
                        System.out.println("Success");
                    }

                    event.setDropCompleted(success);
                    event.consume();
                } else {
                    Dragboard db = event.getDragboard();
                    if (db.hasImage()) {
                        Image data = db.getImage();
                        ((ImageView)handGrid.getChildren().get(columnIndex)).setImage(data);
                        System.out.println(columnIndex);
                    }
                }
            }
        });
    }
    public void onRightClickRemoveProgrammingcard (Pane pane) {
        pane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                for(Node child : pane.getChildren()) {
                    handGrid.add(child, 0, 0);
                }
                pane.getChildren().clear();
            }
        });
    }
}

