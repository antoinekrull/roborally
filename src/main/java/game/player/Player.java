package game.player;

import game.card.Card;
import game.robot.Robot;
import server.HandleClient;
import server.ServerMain;

import java.util.ArrayList;
import java.util.LinkedList;

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
    private boolean isOutOfRound;
    private ArrayList<Card> hand;
    public ServerMain.Server server;
    private LinkedList<Card> personalDiscardPile;
    private Robot robot;
    private int energyCubes;

    public Player(String username, Robot robot) {
        this.username = username;
        this.robot = robot;
    }

    public int getEnergyCubes() {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }

    public Player(String username, ServerMain.Server server) {
        this.username = username;
        this.hand = new ArrayList<Card>();
        this.isOutOfRound = false;
        this.isPlaying = false;
        this.server = server;
        this.personalDiscardPile = new LinkedList<Card>();
        this.score = 0;
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
    public boolean isPlaying() {
        return isPlaying;
    }
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public boolean isOutOfRound() {
        return isOutOfRound;
    }
    public void setOutOfRound(boolean outOfRound) {
        isOutOfRound = outOfRound;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }
    public int getPersonalDiscardPileSize() {
        return personalDiscardPile.size();
    }
    public void increaseScore() {
        score++;
    }
    public void setRobot(Robot robot) {
        this.robot = robot;
    }
    public Robot getRobot() {
        return robot;
    }
    public Card getTopCardFromPersonalDiscardPile() {
        Card topCard = personalDiscardPile.get(personalDiscardPile.size());
        return topCard;
    }
    public Card discard(int index) {
        Card discardedCard = hand.get(index);
        personalDiscardPile.add(discardedCard);
        hand.remove(index);
        return discardedCard;
    }
    public void addCardToHand(Card addedCard) {
        hand.add(addedCard);
    }
    public void drawCard(Card drawnCard) {
        hand.add(drawnCard);
    }
    public Card getCard() {
        return hand.get(0);
    }
    public Card getCard(int index) {
        return hand.get(index);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }
}
