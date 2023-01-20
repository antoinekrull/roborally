package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import java.io.IOException;

import client.ui.PlayerListCell;
import communication.Message;
import client.player.ClientPlayer;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ViewModel for lobby including chat, ListView to show current players,
 * two ChoiceBoxes (one for selecting chatpartner, another one for selecting the map)
 *
 * @author Tobias
 * @version 1.0
 */

public class ViewModelLobby {

    @FXML
    private VBox chatVBox;
    @FXML
    private ScrollPane chatScrollPane;
    @FXML
    private TextField chatTextfield;
    @FXML
    private Button chatButton;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem helpMenuItem;
    @FXML
    private ListView<ClientPlayer> userList;
    @FXML
    private ChoiceBox<String> mapsChoiceBox;
    @FXML
    private ChoiceBox<ClientPlayer> usersChoiceBox;
    @FXML
    private Button readyButton;
    @FXML
    private Label timeLabel;
    @FXML
    private Button mapButton;

    private Boolean ready = false;
    private ModelChat modelChat;
    private ModelUser modelUser;
    private ModelGame modelGame;

    private NotifyChangeSupport notifyChangeSupport;
    private final Logger logger = LogManager.getLogger(ViewModelLobby.class);

    public ViewModelLobby() {
        modelChat = ModelChat.getInstance();
        modelUser = ModelUser.getInstance();
        modelGame = ModelGame.getInstance();
        this.notifyChangeSupport = NotifyChangeSupport.getInstance();
        notifyChangeSupport.setViewModelLobby(this);
    }

    /**
     * Initializing method for JavaFX components
     */
    public void initialize() {
        this.userList.setItems(modelGame.getPlayerList().getPlayerList());
        this.usersChoiceBox.setValue(modelGame.getPlayerList().getPlayerList().get(0));
        this.usersChoiceBox.setItems(modelGame.getPlayerList().getPlayerList());
        this.mapsChoiceBox.setItems(modelGame.getMaps());
        //this.mapsChoiceBox.setValue(modelGame.getMaps().get(0));

        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());

        /*
         * Sets focus for TextField after sending a message
         */
        Platform.runLater(() -> chatTextfield.requestFocus());

        /*
         * refreshes ListView when change happens
         */
        Platform.runLater(() -> userList.refresh());

        /*
         * Adjusts VBox height and updates ScrollPane to latest message.
         */
        chatVBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                chatScrollPane.setVvalue((Double) newValue);
            }
        });

        /*
         * Custom Cells for ListView to show specific attributes of player
         */
        userList.setCellFactory(listView -> new PlayerListCell());

        /*
         * Mouse event for player's ListView to update choice box.
         * Cell with item updates ChoiceBox with current item.
         * If cell is empty, ChoiceBox is updated to 'All'.
         */
        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (!userList.getSelectionModel().getSelectedItems().isEmpty()) {
                    if (userList.getSelectionModel().getSelectedIndex() != 0) {
                        usersChoiceBox.setValue(userList.getSelectionModel().getSelectedItem());
                        event.consume();
                    }
                    else {
                        usersChoiceBox.setValue(userList.getItems().get(0));
                        event.consume();
                    }
                }
            }
        });


        /*
         * Automatically selects item in ListView after changing item in ChoiceBox
         */
        usersChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(usersChoiceBox.getSelectionModel().getSelectedIndex() != 0) {
                    userList.getSelectionModel().select(usersChoiceBox.getSelectionModel().getSelectedIndex());
                }
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
            privateMessageToChat(privateMessage, true);
        }
        else {
            String groupMessage = message.getMessageBody().getMessage();
            groupMessageToChat(groupMessage, true);
        }
    }

    public void chatButtonOnAction() {
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
    public void mapButtonOnAction() throws IOException{

        String map = mapsChoiceBox.getSelectionModel().getSelectedItem();
        if (map != null) {
            modelUser.sendMapSelected(map);
            //TODO: IF AT LEAST TWO PLAYERS ARE READY, START TIMER AND SWITCH SCENE
            //RoboRallyStart.switchScene("gamewindow.fxml");
        }
    }

    public void readyButtonOnAction() throws IOException {
        if (!this.ready) {
            readyButton.setText("NOT READY");
            this.ready =true;
            //modelGame.setPlayerStatus(modelUser.userIDProperty().get());
            modelUser.sendSetStatus(true);
            if(modelGame.getReadyList().size() >= 2) {
                logger.info("lets start");
            }
            /*long endTime = 2000;
            DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
            final Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.millis(500),
                            event -> {
                                final long diff = endTime - System.currentTimeMillis();
                                if (diff < 0) {
                                    //  timeLabel.setText("00:00:00");
                                    timeLabel.setText(timeFormat.format(0));
                                } else {
                                    timeLabel.setText(timeFormat.format(diff));
                                }
                            }
                    )
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
             */
            //RoboRallyStart.switchScene("gamewindow.fxml");
        }
        else if (this.ready) {
            readyButton.setText("READY");
            this.ready=false;
            modelUser.sendSetStatus(false);
        }
        //resource is null
        /*
        FXMLLoader loader = FXMLLoader.load(getClass().getResource("lobby.fxml"));
        Parent root = loader.load();
        Stage currentStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        currentStage.setScene(new Scene(root, 1650, 1000));
        currentStage.show();
        */
    }
    public void enterGame() throws IOException {
        RoboRallyStart.switchScene("gamewindow.fxml");
    }

    public void exit() throws IOException {
        //send disconnect notification to server
        Platform.exit();
        System.exit(0);
    }

    public void help() {
        //show help
    }

    public void setMap() {
        //String map = mapItem1.getText();
        //map is Dizzy Highway
    }
}
