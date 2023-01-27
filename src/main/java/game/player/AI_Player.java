package game.player;

import communication.Message;
import communication.MessageCreator;
import game.Game;
import game.board.Board;
import game.board.StartTile;
import game.card.Card;
import game.card.CardType;
import org.javatuples.Pair;

public class AI_Player extends Player {
    private Board board;
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

    public void playTurn(){
        //preparation
        drawFullHand();
        //UPGRADE PHASE
        playUpgradePhase();
        //PROGRAMMING PHASE
        playProgrammingPhase();
        //ACTIVATION PHASE
    }
    @Override
    public void drawFullHand(){
        for(int i = 0; i <= 9 - getHand().size(); i++){
            drawCard();
        }
    }
    @Override
    public void playUpgradePhase(){
    }
    @Override
    public void playProgrammingPhase(){
        //play cards in register
        for(int i = 0; i <= 5; i++){
            playCard(getHand().get(i), i);
        }
        //discard the rest of the programming cards
        for(Card card: getHand()){
            if(card.getCardType() == CardType.PROGRAMMING_CARD){
                discard(getHand().indexOf(card));
            }
        }
    }

//    public ArrayList<Node> getShortestPath(){
//        Pair<Integer, Integer> currentObjective = Helper.getPositionOfCurrentObjective(robot.getCurrentObjective(), board);
//
//    }

    /**
     * The setter for the board. This is important as KIs are generated before a board exists. But for getting the
     * shortest path, a board is needed. So after starting the game, a board is given to the AI player.
     *
     * @param board The board on wich is played
     */
    public void setBoard(Board board){ this.board = board; }

}
