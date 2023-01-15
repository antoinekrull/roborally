package game.card;

import game.CollisionCalculator;
import game.board.Direction;
import game.player.Player;
import org.javatuples.Pair;

public class SandboxRoutineCard extends Card{
    public SandboxRoutineCard(){
        cardType = CardType.SPECIAL_PROGRAMMING_CARD;
        setCardName("Sandbox Routine");
    }

    public void applyEffect(Player player, String cardName) throws Exception {
        switch (cardName){
            case "Left Turn" -> {
                switch (player.getRobot().getDirection()){
                    case NORTH -> player.getRobot().setDirection(Direction.WEST);
                    case WEST -> player.getRobot().setDirection(Direction.SOUTH);
                    case SOUTH -> player.getRobot().setDirection(Direction.EAST);
                    case EAST -> player.getRobot().setDirection(Direction.NORTH);
                    default -> throw new Exception("Invalid direction");
                }
            }
            case "Right Turn" -> {
                switch (player.getRobot().getDirection()){
                    case NORTH -> player.getRobot().setDirection(Direction.EAST);
                    case EAST -> player.getRobot().setDirection(Direction.SOUTH);
                    case SOUTH -> player.getRobot().setDirection(Direction.WEST);
                    case WEST -> player.getRobot().setDirection(Direction.NORTH);
                    default -> throw new Exception("Invalid direction");
                }
            }
            case "U-Turn" -> {
                switch (player.getRobot().getDirection()) {
                    case NORTH -> player.getRobot().setDirection(Direction.SOUTH);
                    case EAST -> player.getRobot().setDirection(Direction.WEST);
                    case SOUTH -> player.getRobot().setDirection(Direction.NORTH);
                    case WEST -> player.getRobot().setDirection(Direction.EAST);
                    default -> throw new Exception("Invalid direction");
                }
            }
            case "Move 1" -> {
                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                        player.getRobot().getCurrentPosition().getValue1());
                for(int i = 0; i < 1; i++){
                    if(CollisionCalculator.checkRobotCollision(player)){
                        switch(player.getRobot().getDirection()){
                            case NORTH -> newPosition.setAt0(newPosition.getValue0() + 1);
                            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - 1);
                            case EAST -> newPosition.setAt1(newPosition.getValue1() + 1);
                            case WEST -> newPosition.setAt1(newPosition.getValue1() - 1);
                        }
                    }
                }
                player.getRobot().setCurrentPosition(newPosition);
            }
            case "Move 2" -> {
                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                        player.getRobot().getCurrentPosition().getValue1());
                for(int i = 0; i < 2; i++){
                    if(CollisionCalculator.checkRobotCollision(player)){
                        switch(player.getRobot().getDirection()){
                            case NORTH -> newPosition.setAt0(newPosition.getValue0() + 1);
                            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - 1);
                            case EAST -> newPosition.setAt1(newPosition.getValue1() + 1);
                            case WEST -> newPosition.setAt1(newPosition.getValue1() - 1);
                        }
                    }
                }
                player.getRobot().setCurrentPosition(newPosition);
            }
            case "Move 3" -> {
                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                        player.getRobot().getCurrentPosition().getValue1());
                for(int i = 0; i < 3; i++){
                    if(CollisionCalculator.checkRobotCollision(player)){
                        switch(player.getRobot().getDirection()){
                            case NORTH -> newPosition.setAt0(newPosition.getValue0() + 1);
                            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - 1);
                            case EAST -> newPosition.setAt1(newPosition.getValue1() + 1);
                            case WEST -> newPosition.setAt1(newPosition.getValue1() - 1);
                        }
                    }
                }
                player.getRobot().setCurrentPosition(newPosition);
            }
            case "Move Back" -> {
                Direction originalDirection = player.getRobot().getDirection(); //the original direction is saved
                //the direction is flipped to allow for collision checking, will be later restored to its original value
                switch (player.getRobot().getDirection()){
                    case NORTH -> player.getRobot().setDirection(Direction.SOUTH);
                    case SOUTH -> player.getRobot().setDirection(Direction.NORTH);
                    case EAST -> player.getRobot().setDirection(Direction.WEST);
                    case WEST -> player.getRobot().setDirection(Direction.EAST);
                }
                //code from MoveCard1
                Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                        player.getRobot().getCurrentPosition().getValue1());
                for(int i = 0; i < 1; i++){
                    if(CollisionCalculator.checkRobotCollision(player)){
                        switch(player.getRobot().getDirection()){
                            case NORTH -> newPosition.setAt0(newPosition.getValue0() + 1);
                            case SOUTH -> newPosition.setAt0(newPosition.getValue0() - 1);
                            case EAST -> newPosition.setAt1(newPosition.getValue1() + 1);
                            case WEST -> newPosition.setAt1(newPosition.getValue1() - 1);
                        }
                    }
                }
                player.getRobot().setCurrentPosition(newPosition);
                player.getRobot().setDirection(originalDirection);
            }
        }
    }
}
