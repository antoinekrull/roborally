package client.player;

import client.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PlayerList {
    private ObservableList<Player> playerList;

    public PlayerList() {
        playerList = FXCollections.observableArrayList();
        playerList.add(new Player(-1, "Group", null));
    }

    public ObservableList<Player> getPlayerList() {
        return playerList;
    }

    public void add(Player player) {
        playerList.add(player);
    }

    public void remove(int clientID) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == clientID) {
                playerList.remove(playerList.get(i));
            }
        }
    }

    public void changePlayerStatus(int clientID, boolean ready) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == clientID) {
                playerList.get(i).setIsReady(ready);
            }
        }
    }

    public Player getPlayer(int clientID) {
        Player player = null;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == clientID) {
                player = playerList.get(i);
            }
        }
        return player;
    }

    public int getPlayerIndex(int clientID) {
        int index = 0;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == clientID) {
                index = i;
            }
        }
        return index;
    }

    public boolean containsPlayer(int clientID) {
        boolean contains = false;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == clientID) {
                contains = true;
            }
        }
        return contains;
    }
}
