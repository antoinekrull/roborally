package client.viewmodel;

import client.RoboRallyStart;
import client.connection.NotifyChangeSupport;
import client.model.ModelChat;
import client.model.ModelGame;
import client.model.ModelUser;
import java.io.IOException;
import communication.Message;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    private ListView<String> userList;
    @FXML
    private ChoiceBox<String> mapsChoiceBox;
    @FXML
    private ChoiceBox<String> usersChoiceBox;
    @FXML
    private Button readyButton;
    @FXML
    private Label timeLabel;

    private BooleanProperty ready;
    private ModelChat modelChat;
    private ModelUser modelUser;
    private ModelGame modelGame;

    private NotifyChangeSupport notifyChangeSupport;

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
        this.ready = new SimpleBooleanProperty();
        this.userList.setItems(modelGame.getUsers());
        this.usersChoiceBox.setValue(modelGame.getUsersToSelect().get(0));
        this.mapsChoiceBox.setValue(modelGame.getMaps().get(0));
        this.usersChoiceBox.setItems(modelGame.getUsersToSelect());
        this.mapsChoiceBox.setItems(modelGame.getMaps());
        chatButton.disableProperty().bind(chatTextfield.textProperty().isEmpty());
        chatTextfield.textProperty().bindBidirectional(modelChat.textfieldProperty());
        ready.bindBidirectional(modelGame.readyToPlayProperty());

        /*
         * Sets focus for TextField after sending a message
         */
        Platform.runLater(() -> chatTextfield.requestFocus());

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
         * Mouse event for player's ListView to update choice box.
         * Cell with item updates ChoiceBox with current item.
         * If cell is empty, ChoiceBox is updated to 'All'.
         */
        userList.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(item);
                }
            };
            cell.setOnMouseClicked(e -> {
                if (!cell.isEmpty()) {
                    usersChoiceBox.setValue(userList.getSelectionModel().getSelectedItem());
                    e.consume();
                }
                else {
                    usersChoiceBox.setValue("All");
                }
            });
            return cell;
        });

        /*
         * Automatically selects item in ListView after changing item in ChoiceBox
         */
        usersChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(usersChoiceBox.getSelectionModel().getSelectedIndex() != 0) {
                    userList.getSelectionModel().select(usersChoiceBox.getSelectionModel().getSelectedIndex()-1);
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
            privateMessageToChat(privateMessage);
        }
        else {
            String groupMessage = message.getMessageBody().getMessage();
            groupMessageToChat(groupMessage);
        }
    }

    public void chatButtonOnAction() {
        String user = usersChoiceBox.getSelectionModel().getSelectedItem();
        int userID = modelUser.userIDProperty().get();

        if(user.equals("All")) {
            modelChat.sendGroupMessage();
            addToChat(chatTextfield.getText(), false);
        }
        else {
            modelChat.sendPrivateMessage(userID);
            addToChat(chatTextfield.getText(), true);
        }
    }

    public void addToChat(String message, Boolean isPrivate) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(message);
        TextFlow textFlow = new TextFlow(text);
        if(!isPrivate) {
            textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(46,119,204);" +
                    "fx-background-radius: 40px; -fx-opacity: 100;");
        }
        else {
            textFlow.setStyle("-fx-color: rgb(255,255,255);" + "-fx-background-color: rgb(208,167,15);" +
                    "fx-background-radius: 40px; -fx-opacity: 100;");
        }
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(0.934, 0.945, 0.996));

        hBox.getChildren().add(textFlow);
        chatVBox.getChildren().add(hBox);

        chatTextfield.clear();
        chatTextfield.requestFocus();
    }

    public void groupMessageToChat(String groupMessage) {
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                chatVBox.getChildren().add(hBox);
            }
        });
    }

    public void privateMessageToChat(String privateMessage) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(privateMessage);
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
        });
    }

    public void readyButtonOnAction() throws IOException {
        if (this.ready.get()==false) {
            readyButton.setText("NOT READY");
            this.ready.set(true);
            modelGame.setPlayerStatus(modelUser.userIDProperty().get());
            //modelGame.setPlayerStatus(modelUser.getUserID());
            modelUser.sendSetStatus(true);
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
            RoboRallyStart.switchScene("gamewindow.fxml");
        }
        else if (this.ready.get()== true) {
            readyButton.setText("READY");
            this.ready.set(false);
            modelGame.setPlayerStatus(modelUser.userIDProperty().get());
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
        ;
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
