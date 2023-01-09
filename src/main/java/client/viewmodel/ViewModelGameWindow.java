package client.viewmodel;

import client.connection.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import communication.Message;
import game.board.Board;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    //buttons for cards

    private ModelChat modelChat;
    private ModelGame modelGame;
    private ModelUser modelUser;
    private Board gameBoard;

    private NotifyChangeSupport notifyChangeSupport;

    public ViewModelGameWindow() {
        this.modelChat = ModelChat.getInstance();
        this.modelGame = ModelGame.getInstance();
        this.modelUser = ModelUser.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        notifyChangeSupport.setViewModelGameWindow(this);
    }

    public void initialize() {
        ArrayList<Tile>[][] map = modelGame.gameBoard.getBoard();
        //placeTiles(map,13,10);
        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());
        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {@Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chatScrollPane.setVvalue((Double) newValue);
            }
        });
        setOnDragDetected(programCard1, "programCard1");
        setOnDragDetected(programCard2, "programCard2");
        setOnDragDetected(programCard3, "programCard3");
        setOnDragDetected(programCard4, "programCard4");
        setOnDragDetected(programCard5, "programCard5");
        setOnDragDetected(programCard6, "programCard6");
        setOnDragDetected(programCard7, "programCard7");
        setOnDragDetected(programCard8, "programCard8");
        setOnDragDetected(programCard9, "programCard9");
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
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
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
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
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
        textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(208,167,15);" +
                "fx-background-radius: 40px; -fx-opacity: 100;");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(hBox);
            }
        });;
    }

    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }

    private void setOnDragDetected(ImageView imageView, String data) {
        EventHandler<MouseEvent> dragDetectedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = imageView.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString(data);
                db.setContent(content);
                event.consume();
            }
        };
        EventHandler<DragEvent> dragOverHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != programmingGrid &&
                    event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        };
        EventHandler<DragEvent> dragDroppedHandler = new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    String data = db.getString();
                    ImageView imageView = null;
                    switch (data) {
                        case "Image 1":
                            imageView = new ImageView(new Image("programCard1"));
                            break;
                        case "Image 2":
                            imageView = new ImageView(new Image("programCard2"));
                            break;
                        //TODO: Add cases for the other 7 image views here
                    }
                    if (imageView != null) {
                        programmingGrid.add(imageView, 0, 0);
                        success = true;
                    }
                }
                event.setDropCompleted(success);
                event.consume();
            }
        };

        imageView.setOnDragDetected(dragDetectedHandler);
        programmingGrid.setOnDragOver(dragOverHandler);
        programmingGrid.setOnDragDropped(dragDroppedHandler);
    }
/*
This code sets up three event handlers for a JavaFX ImageView and a GridPane called programmingGrid.

The first event handler is for the "drag detected" event on the ImageView. This event is triggered when the user begins a drag-and-drop gesture by pressing the mouse button on the ImageView. When this event occurs, the event handler creates a Dragboard and puts a string of data on it. The string of data is passed to the event handler as the data parameter. The Dragboard is then associated with the drag-and-drop gesture by calling startDragAndDrop() on the ImageView.

The second event handler is for the "drag over" event on the GridPane. This event is triggered when the user drags the data over the GridPane. The event handler checks that the drag-and-drop gesture is not originating from the GridPane itself and that the Dragboard has a string of data on it. If these conditions are met, the event handler calls acceptTransferModes() on the DragEvent to allow for both copying and moving of the data.

The third event handler is for the "drag dropped" event on the GridPane. This event is triggered when the user drops the data onto the GridPane. The event handler checks that the Dragboard has a string of data on it. If this is the case, the event handler creates a new ImageView with the corresponding image based on the string data, and adds it to the GridPane. The event handler then sets the "drop completed" flag on the DragEvent to indicate whether the data was successfully transferred and used.




 */
}
