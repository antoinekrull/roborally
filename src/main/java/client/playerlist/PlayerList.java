package client.playerlist;

import game.player.Player;
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
}
