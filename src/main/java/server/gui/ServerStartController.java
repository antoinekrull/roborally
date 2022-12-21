package server.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ServerStartController {

    @FXML
    private Button startserver;
    @FXML
    private Button stopserver;
    @FXML
    private Label serverstatus;
    private StringProperty status;

    private ModelServer modelServer;


    public ServerStartController() {
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
        modelServer.stopServer();
        modelServer.getAlive();
    }
}
