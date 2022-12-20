package game.card;

import game.Game;
import game.player.Player;
import game.robot.Robot;

public class VirusCard extends Card {
    //robots within a 6 space radius of your robot immediately take 1 spam, play the top card of your programming deck this register
    public VirusCard(){setCardName("Virus");}
    //isInrange Checks if two robots are within 6 tiles of each other
    private boolean isInRange(Robot robot1, Robot robot2){
        if(robot1.getCurrentPosition().equals(robot2.getCurrentPosition())){ //if the condition is true then robot1 == robot2
            return false;
        } else {
            return Math.abs(robot1.getCurrentPosition().getValue0() - robot2.getCurrentPosition().getValue0()) <= 6
                    || Math.abs(robot1.getCurrentPosition().getValue1() - robot2.getCurrentPosition().getValue1()) <= 6;
        }
    }
    @Override
    public void applyEffect(Player player) throws Exception {
        for(int i = 0; i < Game.playerList.size(); i++){
            if(isInRange(player.getRobot(), Game.playerList.getPlayerFromList(i).getRobot())){
                Game.playerList.getPlayerFromList(i).addCard(Game.virusDeck.popCardFromDeck());
            }
        }
    }
}
