package game.player;

import communication.Message;
import communication.MessageCreator;
import game.Game;
import game.board.Board;
import game.board.StartTile;
import game.card.AgainCard;
import game.card.Card;
import game.card.CardType;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AI_Player extends Player {
    private Board board;
    Random random = new Random();
    MessageCreator messageCreator;
    public AI_Player(int id, String username, Robot robot, Board board) {
        super(id, username, robot);
        this.board = board;
        messageCreator = new MessageCreator();
    }

    public void chooseStartingPoint(Game game) throws InterruptedException {
        Pair<Integer, Integer> aiStartTilePosition = new Pair<>(0,0);
        for(StartTile startTile: board.getStartTileList()) {
            if(!startTile.isTaken()) {
                aiStartTilePosition = startTile.getPosition();
                startTile.setTaken(true);
            }
        }
        robot.setCurrentPosition(aiStartTilePosition);
        logger.debug("AI choose startingpoint x: " + aiStartTilePosition.getValue0() + " y: " + aiStartTilePosition.getValue1());
        Message startingPointTakenMessage = messageCreator.generateStartingPointTakenMessage(aiStartTilePosition.getValue0(), aiStartTilePosition.getValue1(), id);
        game.setStartPoint(aiStartTilePosition.getValue0(), aiStartTilePosition.getValue1());
        server.messages.put(startingPointTakenMessage);
    }
    public void sendPlayerAddedMessage() throws InterruptedException {
        Message playerAddedMessage = messageCreator.generatePlayerAddedMessage(username, robot.getFigure(), id);
        //server.CLIENTS.get(1).write(playerAddedMessage);
        server.messages.put(playerAddedMessage);
    }

    public void sendDrawDamage(Player player, String[] drawnDamageCards){
        try {
            server.messages.put(messageCreator.generateDrawDamageMessage(player.getId(), drawnDamageCards));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void drawFullHand(){
        for(int i = 0; i <= 9 - getHand().size(); i++){
            drawCard();
        }
    }
    @Override
    public void drawCard() {
        if(robot.getDeck().getSize() > 0) {
            hand.add(robot.getDeck().popCardFromDeck());
        } else {
            refillDeck();
            hand.add(robot.getDeck().popCardFromDeck());
        }
    }
    @Override
    public void refillDeck(){
        robot.setDeck(personalDiscardPile);
        robot.getDeck().shuffleDeck();
    }
    @Override
    public void playUpgradePhase(){
    }
    @Override
    public void playProgrammingPhase() throws InterruptedException {
        logger.debug("A KI Player starts his programming phase");
        logger.debug(getHand());
        TimeUnit.SECONDS.sleep(10);
        // if the first card that would be played is an AgainCard, it is swapped to another place
        if(hand.get(0) instanceof AgainCard){
            Collections.swap(hand, 0, random.nextInt(3) + 1);
        }
        //play cards in register
        for(int i = 0; i < 5; i++){
            logger.debug("KI PLAYS: " + hand.get(i).getCard());
            playCard(hand.get(i).getCard(), i);
        }
        //discard the rest of the programming cards
        for(Iterator<Card> iterator = getHand().iterator(); iterator.hasNext();){
            Card card = iterator.next();
            if(card.getCardType() == CardType.PROGRAMMING_CARD){
                iterator.remove();
                //discard(getHand().indexOf(card));
            }
        }
    }

    /**
     * The setter for the board. This is important as KIs are generated before a board exists. But for getting the
     * shortest path, a board is needed. So after starting the game, a board is given to the AI player.
     *
     * @param board The board on wich is played
     */
    public void setBoard(Board board){ this.board = board; }

}
