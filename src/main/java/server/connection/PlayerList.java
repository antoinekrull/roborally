package server.connection;

import game.player.Robot;
import game.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class PlayerList implements Iterator<Player> {
    private ArrayList<Player> playerList = new ArrayList<>();
    private int count = 0;

    /**
     * Checks if player is in game.
     *
     * @param robot Player who is checked.
     * @return answer Returns if player is in game.
     */
    public boolean playerIsInList(Robot robot) {
        int indexOfPlayer = -1;
        boolean answer = false;
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRobot().equals(robot)) {
                indexOfPlayer = i;
                break;
            }
        }
        if (indexOfPlayer > -1) {
            answer = true;
        }
        return answer;
    }

    public ArrayList<Player> getPlayerList(){
        return playerList;
    }
    public boolean playersAreReady() {
        int readyCount = 0;
        boolean result = false;
        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).isReady()) {
                readyCount++;
            }
            if(readyCount == playerList.size()) {
                result = true;
            }
        }
        return result;
    }

    public int getAmountOfReadyPlayers() {
        int result = 0;
        for(Player player: playerList) {
            if (player.isReady()) {
                result++;
            }
        }
        return result;
    }

    public void setPlayerReadiness(boolean setter) {
        for(int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setReady(setter);
        }
    }

    public void setPlayersPlaying(boolean setter) {
        for(int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPlaying(setter);
        }
    }

    /**
     * Returns player from the list when his username is used.
     *
     * @param robot Username of player who is addressed.
     * @return player Returns the player in the list.
     */
    public Player getPlayerFromList(Robot robot) {
        Player player;
        for(int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getRobot().equals(robot)) {
                player = playerList.get(i);
                return player;
            }
        }
        return null;
    }

    public Player getPlayerFromList(int id) {
        Player player;
        for(int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getId() == id) {
                player = playerList.get(i);
                return player;
            }
        }
        return null;
    }

    public void remove(Robot robot) {
        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).getRobot().equals(robot)) {
                playerList.remove(i);
            }
        }
    }

    public void remove(int id) {
        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).getId() == id) {
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

    public boolean allPlayerRegistersActivated() {
        boolean result = false;
        int count = 0;
        for(int i = 0; i < playerList.size(); i++) {
            if(playerList.get(i).allRegistersActivated()) {
                count++;
            }
        }
        if(count == playerList.size()) {
            result = true;
        }
        return result;
    }

    public PlayerList getUnreadyPlayers() {
        PlayerList unreadyPlayers = new PlayerList();
        for(int i = 0; i < playerList.size(); i++) {
            if(!playerList.get(i).isReady()) {
                unreadyPlayers.add(playerList.get(i));
            }
        }
        return  unreadyPlayers;
    }

    @Override
    public boolean hasNext() {
        if (count < playerList.size()){
            return true;
        }
        return false;
    }

    @Override
    public Player next() {
        if (count == playerList.size())
            throw new NoSuchElementException();

        count++;
        return playerList.get(count - 1);
    }

    public boolean robotNeedsReboot() {
        boolean result = false;
        for(Player player: playerList) {
            if(player.getRobot().getRebootStatus()) {
                result = true;
            }
        }
        return result;
    }

    public int numberOfNeededReboots() {
        int result = 0;
        for(Player player: playerList) {
            if(player.getRobot().getRebootStatus()) {
                result++;
            }
        }
        return result;
    }
}
