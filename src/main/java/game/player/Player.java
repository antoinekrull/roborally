package game.player;

import game.Game;
import game.card.*;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.connection.Server;

import static game.Game.upgradeShop;

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
    private boolean isBuying;
    private String upgradeToBuy;
    private int adminRegister;
    private boolean[][] isUsingUpgrade = {{false, false, false},{false, false, false}};
    private ArrayList<Card> hand;
    private Card[] cardRegister = new Card[5];
    //private boolean[] statusRegister = new boolean[5];
    private ArrayBlockingQueue<Card> PermanentUpgradeSlots = new ArrayBlockingQueue<>(3);
    private ArrayBlockingQueue<Card> TemporaryUpgradeSlots = new ArrayBlockingQueue<>(3);
    private boolean[] statusRegister = new boolean[5];
    private ProgrammingDeck personalDiscardPile;
    private ArrayBlockingQueue<Card> cardsToSwap = new ArrayBlockingQueue<>(3); //meant for the use with the memory swap upgrade card
    private Boolean rearLaserOn = false;
    private Boolean hasAdminPrivilege = false;

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
    public void permanentUpgradeUsed(){
        setHasAdminPrivilege(false);
        setAdminRegister(-1);
        setRearLaserOn(false);
    }

    public void temporaryUpgradeUsed(){
        ArrayList<Card> slotContent = new ArrayList<>();
            //copies the content of the slot to allow for direct access to elements
        for(int i = 0; i < 3; i++){
            slotContent.add(TemporaryUpgradeSlots.peek());
            TemporaryUpgradeSlots.remove(TemporaryUpgradeSlots.peek());
        }
            //slots are cleared and would be repopulated after removing used cards
        TemporaryUpgradeSlots.clear();
            //cards are removed from the back of the list so that the indexes of unused cards remain unchanged
        for(int i = 2; i >= 0; i--){
            if(isUsingUpgrade[0][i]){
                slotContent.remove(slotContent.get(i));
            }
        }
            //slots are repopulated with the remaining cards
        for(int i = 0; i < 3; i++){
            TemporaryUpgradeSlots.add(slotContent.get(i));
        }
    }
    public int getAdminRegister() {return adminRegister;}

    public void setAdminRegister(int adminRegister) {this.adminRegister = adminRegister;}
    public boolean[][] isUsingUpgrade() {
        return isUsingUpgrade;
    }
    public void setUsingUpgrade(boolean[][] values) {
        isUsingUpgrade = values;
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
    public boolean isBuying() {return isBuying;}
    public void setBuying(boolean buying) {isBuying = buying;}
    public String getUpgradeToBuy() {return upgradeToBuy;}
    public void setUpgradeToBuy(String upgradeToBuy) {this.upgradeToBuy = upgradeToBuy;}
    public ArrayBlockingQueue<Card> getCardsToSwap() {return cardsToSwap;}
    public Boolean getRearLaserOn() {return rearLaserOn;}
    public void setRearLaserOn(Boolean rearLaserOn) {this.rearLaserOn = rearLaserOn;}
    public Boolean getHasAdminPrivilege() {return hasAdminPrivilege;}
    public void setHasAdminPrivilege(Boolean hasAdminPrivilege) {this.hasAdminPrivilege = hasAdminPrivilege;}
    //TODO: Fix crash if reboot happened
    public Card getCardFromRegister(int index){
        if(cardRegister[index] == null) {
            return new NullCard();
        } else {
            return cardRegister[index];
        }
    }
    public int getCurrentRegister(Card currentCard){
        return ArrayUtils.indexOf(cardRegister, currentCard);
    }
    //This method is used to add a card to a specified register. It should not be used by the player
    public void setCardRegister(Card card, int index) {
        cardRegister[index] = card;
    }
    public ProgrammingDeck getPersonalDiscardPile() {
        return personalDiscardPile;
    }
//    public void setStatusRegister(boolean setAll) {
//        for(boolean status: statusRegister ){
//            status = setAll;
//        }
//    }
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
    public void addCardToHand(Card drawnCard) {
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

    public void purchaseUpgrade(int index){
        if(upgradeShop.get(index).equals(null)){
            logger.log(Level.ERROR, "No card available at the selected index");
        }
        else {
            if(this.getRobot().getEnergyCubes() >= upgradeShop.get(index).getCost()){
                this.getRobot().setEnergyCubes(this.getRobot().getEnergyCubes() - upgradeShop.get(index).getCost());
                if(upgradeShop.get(index).isPermanent()){
                    this.PermanentUpgradeSlots.add(upgradeShop.get(index));
                    upgradeShop.remove(index);
                }
                else{
                    this.TemporaryUpgradeSlots.add(upgradeShop.get(index));
                    upgradeShop.remove(index);
                }
            }
            else
            {
                logger.log(Level.INFO, "Player does not have enough energy cubes to purchase upgrade");
            }
        }
    }

    private void useUpgrades(boolean[][] parameter){
        isUsingUpgrade = parameter;
    }

    //TODO: Add GUI functionality / exceptions
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
            logger.info("You cant play this card in the first register, please try again!");
        } else if(index < 0 || index > cardRegister.length){
            logger.info("The register has not been addressed properly, please try again!");
        } else {
            cardRegister[index] = card;
        }
        if(getEmptyRegisterAmount() == 0) {
            setReady(true);
            server.sendSelectionFinished(id);
        }
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
            if(cardRegister[i] != null) {
                if(cardRegister[i].isDamageCard()) {
                    switch (cardRegister[i].getCard()) {
                        case "Trojan" -> Game.trojanDeck.addCard(cardRegister[i]);
                        case "Worm" -> Game.wormDeck.addCard(cardRegister[i]);
                        case "Spam" -> Game.spamDeck.addCard(cardRegister[i]);
                        case "Virus" -> Game.virusDeck.addCard(cardRegister[i]);
                    }
                } else {
                    personalDiscardPile.addCard(cardRegister[i]);
                    cardRegister[i] = null;
                }
            }
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

    public void activateTUpgrades() throws Exception{
        //ArrayList<Card> pSlotContent = new ArrayList<>();
        ArrayList<Card> tSlotContent = new ArrayList<>();
        //scans the slots for cards
        for(int i = 0; i < 3; i++){
            /*pSlotContent.add(PermanentUpgradeSlots.peek());
            PermanentUpgradeSlots.remove(PermanentUpgradeSlots.peek());
            PermanentUpgradeSlots.add(pSlotContent.get(i));*/
            tSlotContent.add(TemporaryUpgradeSlots.peek());
            TemporaryUpgradeSlots.remove(TemporaryUpgradeSlots.peek());
            TemporaryUpgradeSlots.add(tSlotContent.get(i));
        }
        for(int i = 0; i < 3; i++){
            /*if(isUsingUpgrade[0][i]){
                pSlotContent.get(i).applyEffect(this);
                permanentUpgradeUsed();
            }*/
            if(isUsingUpgrade[1][i]){
                tSlotContent.get(i).applyEffect(this);
                temporaryUpgradeUsed();
            }
        }
    }
}