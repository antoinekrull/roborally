package client.player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientPlayerList {
    private ObservableList<ClientPlayer> clientPlayerList;

    public ClientPlayerList() {
        clientPlayerList = FXCollections.observableArrayList();
        clientPlayerList.add(new ClientPlayer(-1, "Group", null));
    }

    public ObservableList<ClientPlayer> getPlayerList() {
        return clientPlayerList;
    }

    public void add(ClientPlayer clientPlayer) {
        clientPlayerList.add(clientPlayer);
    }

    public void remove(int clientID) {
        for (int i = 0; i < clientPlayerList.size(); i++) {
            if (clientPlayerList.get(i).getId() == clientID) {
                clientPlayerList.remove(clientPlayerList.get(i));
            }
        }
    }
}
