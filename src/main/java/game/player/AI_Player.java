package game.player;

import game.board.Board;
import game.card.Card;
import game.card.CardType;
import helper.Helper;
import helper.Node;
import org.javatuples.Pair;

import java.util.ArrayList;

public class AI_Player extends Player {
    private Board board;
    public AI_Player(int id, String username, Robot robot) {
        super(id, username, robot);
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
