package game.player;

import game.card.Card;
import game.card.Deck;
import game.card.ProgrammingDeck;
import game.robot.Robot;
import server.HandleClient;
import server.ServerMain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import org.apache.commons.lang3.ArrayUtils;

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
    private ArrayList<Card> hand;
    //TODO: further implementation of register logic
    private Card[] cardRegister = new Card[5];
    private boolean[] statusRegister = new boolean[5];
    private ProgrammingDeck personalDiscardPile;
    private Robot robot;

    public Player(int id, String username, Robot robot) {
        this.id = id;
        this.username = username;
        this.robot = robot;
        this.hand = new ArrayList<Card>();
        this.score = 0;
        this.personalDiscardPile = new ProgrammingDeck();
        isPlaying = false;
        isReady = false;
    }

    public String getUsername() {
        return username;
    }
    public int getScore() {
        return score;
    }
    public void increaseScore() {
        score++;
    }
    public boolean isPlaying() {
        return isPlaying;
    }
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public Robot getRobot() {
        return robot;
    }
    public Card discard(int index) {
        Card discardedCard = hand.get(index);
        personalDiscardPile.addCard(discardedCard);
        hand.remove(index);
        return discardedCard;
    }
    public void refillDeck(){
        robot.setDeck(personalDiscardPile);
        robot.getDeck().shuffleDeck();
    }
    public void addCard(Card drawnCard) {
        hand.add(drawnCard);
    }
    public void drawCard() {
        if(robot.getDeck().getSize() > 0) {
            hand.add(robot.getDeck().popCardFromDeck());
        } else {
            refillDeck();
            hand.add(robot.getDeck().popCardFromDeck());
        }
    }
    public Card getCard(int index) {
        return hand.get(index);
    }
    public int getId() {
        return id;
    }
    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }
    public Card[] getCardRegister() {
        return cardRegister;
    }
    public Card getCardFromRegister(int index){
        return cardRegister[index];
    }
    public int getCurrentRegister(Card currentCard){
        return ArrayUtils.indexOf(cardRegister, currentCard);
    }
    public void setCardRegister(Card card, int index) {
        cardRegister[index] = card;
    }
    public boolean[] getStatusRegister() {
        return statusRegister;
    }
    public void setStatusRegister(boolean setter, int index) {
        statusRegister[index] = setter;
    }
    public void setStatusRegister(boolean setAll) {
        for(boolean status: statusRegister ){
            status = setAll;
        }
    }

}
