package client.player;

import game.Game;
import game.card.AgainCard;
import game.card.Card;
import game.card.CardType;
import game.card.ProgrammingDeck;
import game.player.Robot;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Moritz, Dominic, Antoine, Firas
 * @version 1.0
 */
public class Player {

    private String username;
    private int score;
    private int id;
    private boolean isPlaying;
    private boolean isReady;
    private Robot robot;

    public Player(int id, String username, Robot robot) {
        this.id = id;
        this.username = username;
        this.robot = robot;
        this.score = 0;
        isPlaying = false;
        isReady = false;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public String toString() {
        return username;
    }

}