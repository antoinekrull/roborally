package server;

import game.player.Player;

import java.util.ArrayList;

public class PlayerList {
    private ArrayList<Player> playerList = new ArrayList<>();

    /**
     * Checks if player is in game.
     *
     * @param username Player who is checked.
     * @return answer Returns if player is in game.
     */
    public boolean playerIsInList(String username) {
        int indexOfPlayer = -1;
        boolean answer = false;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getUsername().equals(username)) {
                indexOfPlayer = i;
                break;
            }
        }
        if (indexOfPlayer > -1) {
            answer = true;
        }
        return answer;
    }

    /**
     * Returns player from the list when his username is used.
     *
     * @param username Username of player who is addressed.
     * @return player Returns the player in the list.
     */
    public Player getPlayerFromList(String username) {
        Player player;
        for(int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getUsername().equals(username)) {
                player = playerList.get(i);
                return player;
            }
        }
        return null;
    }

    public void remove(String username) {
        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).getUsername().equals(username)) {
                playerList.remove(i);
            }
        }
    }

    public void add(Player player) {
        playerList.add(player);
    }

    public int size() {
        return playerList.size();
    }

    public Player get(int index) {
        return playerList.get(index);
    }
}
