package server.viewmodel;

import game.CustomTimer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.model.ModelServer;

public class ViewModelServer {

    @FXML
    private Button startserver;
    @FXML
    private Button stopserver;
    @FXML
    private Label serverstatus;
    private StringProperty status;

    private ModelServer modelServer;
    private final Logger logger = LogManager.getLogger(ViewModelServer.class);


    public ViewModelServer() {
        this.modelServer = ModelServer.getInstance();
    }

    public void initialize() {
        status = new SimpleStringProperty("Offline");
        serverstatus.textProperty().bind(statusProperty());
        status.bind(modelServer.serverStatusProperty());
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void startServerOnAction() {
        modelServer.startServer();
        modelServer.getAlive();
    }

    public void stopServerOnAction()  {
        try {
            modelServer.stopServer();
            modelServer.getAlive();
        } catch (NullPointerException e) {
            logger.warn("No Server to stop.");
        }
    }

    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }
}
