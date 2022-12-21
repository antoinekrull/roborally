package server.gui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import server.connection.Server;

import java.io.IOException;

public class ModelServer {

    private static ModelServer modelServer;

    private Server server;

    private BooleanProperty alive;
    private StringProperty serverStatus;
    int port;

    private ModelServer() {

        this.port = 3000;
        this.alive = new SimpleBooleanProperty(false);
        this.serverStatus = new SimpleStringProperty("Offline");
        this.alive.addListener(event -> {
            if (alive.get()) {
                setServerStatus("Online");
            }
            else {
                setServerStatus("Offline");
            }
        });
    }
    public void setAlive(boolean alive) {
        this.alive.set(alive);
    }

    public void getAlive() {
        setAlive(server.isAlive());
    }

    public StringProperty serverStatusProperty() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus.set(serverStatus);
    }

    public static ModelServer getInstance() {
        if (modelServer == null) {
            modelServer = new ModelServer();
        }
        return modelServer;
    }

    public void startServer() {
        server = Server.getInstance();
        server.startServer(port);
    }

    public void stopServer() {
        try {
            server.stopServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
