package game.player;

import game.Game;
import game.board.Direction;
import game.card.AgainCard;
import game.card.Card;
import game.card.CardType;
import game.card.ProgrammingDeck;

import java.util.ArrayList;
import java.util.Random;

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
    private Card[] cardRegister = new Card[5];
    private Card[] upgradeSlots = new Card[3];
    private boolean[] statusRegister = new boolean[5];
    private ProgrammingDeck personalDiscardPile;
    private Robot robot;

    public Player(int id, String username, Robot robot) {
        this.id = id;
        this.username = username;
        this.robot = robot;
        robot.setDirection(Direction.NORTH);
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
    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }
    public ArrayList<Card> getHand() {
        return hand;
    }
    public Robot getRobot() {
        return robot;
    }
    public Card getCard(int index) {
        return hand.get(index);
    }
    public int getId() {
        return id;
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
    //This method is used to add a card to a specified register. It should not be used by the player
    public void setCardRegister(Card card, int index) {
        cardRegister[index] = card;
    }
    public boolean[] getStatusRegister() {
        return statusRegister;
    }
    public void setStatusRegister(boolean setter, int index) {
        statusRegister[index] = setter;
    }
    public ProgrammingDeck getPersonalDiscardPile() {
        return personalDiscardPile;
    }
    public void setStatusRegister(boolean setAll) {
        for(boolean status: statusRegister ){
            status = setAll;
        }
    }
    public void drawFullHand(){
        int cardsToDraw = 9 - hand.size();
        for(int i = 0; i < cardsToDraw; i++){
            drawCard();
        }
    }
    public void playUpgradePhase(){
    }
    public void playProgrammingPhase(){
        //user decides which cards to play from his hand
        //code needs input from the UI of each user to know which cards are played
        //playCard(getHand().get(i), i);
        //discard the rest of the programming cards
        for(Card card: getHand()){
            if(card.getCardType() == CardType.PROGRAMMING_CARD){
                discard(getHand().indexOf(card));
            }
        }
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

    //TODO: Add GUI functionality / exceptions
    public void playCard(Card card, int index) {
        if(index == 0 && card instanceof AgainCard) {
            System.out.println("You cant play this card in the first register, please try again!");
        } else if(index > 0 || index < cardRegister.length){
            System.out.println("The register has not been addressed properly, please try again!");
        } else {
            cardRegister[index] = card;
        }
    }

    public boolean allRegistersActivated() {
        boolean result = false;
        int registerCount = 0;
        for(int i = 0; i < statusRegister.length; i++) {
            if(statusRegister[i] == true) {
                registerCount++;
            }
            if(registerCount == statusRegister.length) {
                result = true;
            }
        }
        return result;
    }

    //if timer runs out all unfilled registers of player get filled with random cards
    public void fillRegisterWithRandomCards() {
        Random random = new Random();
        for(int x = 0; x < getEmptyRegisterAmount(); x++) {
            for(int y = 0; y < cardRegister.length; y++) {
                if(cardRegister[y] == null) {
                    if(hand.size() > 0) {
                        cardRegister[y] = discard(random.nextInt(hand.size()));
                    }
                    System.out.println("Hand too empty to fill all registers");
                }
            }
        }
    }

    public int getEmptyRegisterAmount() {
        int count = 0;
        for(int i = 0; i < cardRegister.length; i++) {
            if(cardRegister[i] == null) {
                count++;
            }
        }
        return count;
    }

    public void emptyAllCardRegisters() {
        for(int i = 0; i < cardRegister.length; i++) {
            if(cardRegister[i].isDamageCard()) {
                switch (cardRegister[i].getCardName()) {
                    case "Trojan Horse" -> Game.trojanDeck.addCard(cardRegister[i]);
                    case "Worm" -> Game.wormDeck.addCard(cardRegister[i]);
                    case "Spam" -> Game.spamDeck.addCard(cardRegister[i]);
                    case "Virus" -> Game.virusDeck.addCard(cardRegister[i]);
                }
            } else {
                personalDiscardPile.addCard(cardRegister[i]);
            }
            setCardRegister(null, i);
        }
    }
    @Override
    public String toString() {
        return username;
    }

    //for testing purposes
    public void printHand() {
        for(int i = 0; i < hand.size(); i++) {
            System.out.println(hand.get(i).getCardName());
        }
    }

    public void printRegisters() {
        for(int i = 0; i < cardRegister.length; i++) {
            if(cardRegister[i] == null) {
                System.out.println("null");
            } else {
                System.out.println(cardRegister[i].getCardName());
            }
        }
    }
}