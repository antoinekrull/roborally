package game.player;

import game.Game;
import game.card.AgainCard;
import game.card.Card;
import game.card.ProgrammingDeck;

import java.util.ArrayList;
import java.util.Random;

import game.card.TrojanDeck;
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
    private boolean isDamaged;
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
    public boolean isReady() {
        return isReady;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }
    public boolean isDamaged() {
        return isDamaged;
    }
    public void setDamaged(boolean damaged) {
        isDamaged = damaged;
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
    public ProgrammingDeck getPersonalDiscardPile() {
        return personalDiscardPile;
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
    public void setStatusRegister(boolean setAll) {
        for(boolean status: statusRegister ){
            status = setAll;
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

    public void fillRegisterWithRandomCards() {
        Random random = new Random();
        for(int x = hand.size(); x > getEmptyRegisterAmount(); x-- ) {
            for(int y = 0; y < cardRegister.length; y++) {
                if(cardRegister[y] == null) {
                    cardRegister[y] = discard(random.nextInt(x));
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

}
