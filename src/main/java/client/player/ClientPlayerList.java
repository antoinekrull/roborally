package client.player;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientPlayerList {
    private ObservableList<ClientPlayer> clientPlayerList;
    private ClientPlayerListenerManager clientPlayerListenerManager;


    public ClientPlayerList() {
        clientPlayerList = FXCollections.observableArrayList();
        clientPlayerList.add(new ClientPlayer(-1, "Group", null));
        clientPlayerListenerManager = new ClientPlayerListenerManager(clientPlayerList);
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

    public void changePlayerStatus(int clientID, boolean ready) {
        for (int i = 0; i < clientPlayerList.size(); i++) {
            if (clientPlayerList.get(i).getId() == clientID) {
                clientPlayerList.get(i).setIsReady(ready);
            }
        }
    }

    public ClientPlayer getPlayer(int clientID) {
        ClientPlayer player = null;
        for (int i = 0; i < clientPlayerList.size(); i++) {
            if (clientPlayerList.get(i).getId() == clientID) {
                player = clientPlayerList.get(i);
            }
        }
        return player;
    }

    public int getPlayerIndex(int clientID) {
        int index = 0;
        for (int i = 0; i < clientPlayerList.size(); i++) {
            if (clientPlayerList.get(i).getId() == clientID) {
                index = i;
            }
        }
        return index;
    }

    public boolean containsPlayer(int clientID) {
        boolean contains = false;
        for (int i = 0; i < clientPlayerList.size(); i++) {
            if (clientPlayerList.get(i).getId() == clientID) {
                contains = true;
            }
        }
        return contains;
    }
}
