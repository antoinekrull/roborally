package client.player;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientPlayerListenerManager {
    private ObservableList<ClientPlayer> clientPlayerList;
    private ObservableList<ChangeListener<Number>> changeListeners;

    public ClientPlayerListenerManager(ObservableList<ClientPlayer> clientPlayerList) {
        this.changeListeners = FXCollections.observableArrayList();
        this.clientPlayerList = clientPlayerList;
    }
}
