package game.player;

import game.Game;
import game.card.AgainCard;
import game.card.Card;
import game.card.CardType;
import game.card.ProgrammingDeck;

import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.connection.Server;

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
    private boolean[] statusRegister = new boolean[5];
    private ProgrammingDeck personalDiscardPile;
    private Robot robot;
    private final Logger logger = LogManager.getLogger(Player.class);

    private Server server;

    public Player(int id, String username, Robot robot) {
        this.id = id;
        this.username = username;
        this.robot = robot;
        robot.setId(id);
        //robot.setDirection(Direction.NORTH);
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
    public void setServerForPlayerAndRobot(Server server) {
        this.server = server;
        robot.setServer(server);
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
    public void discardEntireHand() {
        for(int i = hand.size()-1; i > 0; i--) {
            discard(i);
        }
    }
    public void refillDeck(){
        robot.setDeck(personalDiscardPile);
        robot.getDeck().shuffleDeck();
        server.sendShuffleCoding(this);
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

    public void playCard(Card card, int index) {
        if(index == 0 && card instanceof AgainCard) {
            logger.info("You cant play this card in the first register, please try again!");
        } else if(index > 0 || index < cardRegister.length){
            logger.info("The register has not been addressed properly, please try again!");
        } else {
            cardRegister[index] = card;
        }
    }

    public void playCard(String cardName, int index) {
        Card card = null;
        if(cardNameInHand(cardName)) {
            card = hand.get(getIndexOfCard(cardName));
        }
        if(index == 0 && card instanceof AgainCard) {
            System.out.println("You cant play this card in the first register, please try again!");
        } else if(index > 0 || index < cardRegister.length){
            System.out.println("The register has not been addressed properly, please try again!");
        } else {
            cardRegister[index] = card;
        }
        if(getEmptyRegisterAmount() == 0) {
            setReady(true);
            server.sendSelectionFinished(id);
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
    //TODO: Fix this
    public String[] fillRegisterWithRandomCards() {
        discardEntireHand();
        String[] cardNames = new String[getEmptyRegisterAmount()];
        int iterator = 0;
        for(int y = 0; y < cardRegister.length; y++) {
            if (cardRegister[y] == null) {
                if (hand.size() > 0) {
                    drawCard();
                    cardRegister[y] = discard(0);
                    cardNames[iterator++] = cardRegister[y].getCard();

                }
            }
        }
        return cardNames;
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
                switch (cardRegister[i].getCard()) {
                    case "Trojan Horse" -> Game.trojanDeck.addCard(cardRegister[i]);
                    case "Worm" -> Game.wormDeck.addCard(cardRegister[i]);
                    case "Spam" -> Game.spamDeck.addCard(cardRegister[i]);
                    case "Virus" -> Game.virusDeck.addCard(cardRegister[i]);
                }
            } else {
                personalDiscardPile.addCard(cardRegister[i]);
                cardRegister[i] = null;
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
            logger.debug(hand.get(i).getCard());
        }
    }

    public void printRegisters() {
        for(int i = 0; i < cardRegister.length; i++) {
            if(cardRegister[i] == null) {
                logger.debug("null");
            } else {
                logger.info(cardRegister[i].getCard());
            }
        }
    }

    public boolean cardNameInHand(String cardName) {
        boolean result = false;
        for(int i = 0; i < hand.size(); i++) {
            if(cardName.equals(hand.get(i).getCard())) {
                result = true;
            }
        }
        return result;
    }

    private int getIndexOfCard(Card card) {
        int result = -1;
        Object cardClass = card.getClass();
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getCard().equals(card.getCard())) {
                result = i;
                break;
            }
        }
        return result;
    }

    private int getIndexOfCard(String cardName) {
        int result = -1;
        Object cardClass = cardName.getClass();
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getCard().equals(cardName)) {
                result = i;
                break;
            }
        }
        return result;
    }
}