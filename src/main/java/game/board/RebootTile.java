package game.board;

import static game.Game.spamDeck;

import game.player.Player;

/**
 * @author Firas
 * @version 1.0
 */
public class RebootTile extends Tile {
    private static int rebootTileIndex = 0;
    public Direction direction;

    //TODO: Direction needs to be added to constructor
    public RebootTile(int xCoordinate, int yCoordinate/*, Direction direction*/){
        super(xCoordinate, yCoordinate, "/textures/gameboard/reboot.png", "RebootTile");
        this.path = getClass().getResource("/textures/gameboard/reboot.png").toString();
        this.imageFXid = "RebootTile";
        setType("RestartPoint");
        isDanger = false;
        isBlocking = false;
        setRebootTileIndex(rebootTileIndex++);
    }

    public void setRebootTileIndex(int rebootTileIndex) {
        this.rebootTileIndex = rebootTileIndex;
    }
    public static int getRebootTileIndex() {
        return rebootTileIndex;
    }

    public void setDirection(Direction newDirection) {
        direction = newDirection;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
//        player.getRobot().setCurrentPosition(getPosition());
//        player.getRobot().setDirection(this.direction);
//        player.getRobot().setRebootStatus(true);
//        //take two spam cards and add them to your discard pile
//        player.getPersonalDiscardPile().addCard(spamDeck.popCardFromDeck());
//        player.getPersonalDiscardPile().addCard(spamDeck.popCardFromDeck());
//        //discard all cards in your programming deck, activation is cancelled
//        player.setStatusRegister(true);
//        for(int i = 0; i < 5; i++){
//            player.getPersonalDiscardPile().addCard(player.getCardFromRegister(i));
//        }
//        //discard all cards in hand
//        while(player.getHand().size()>0){
//            player.getPersonalDiscardPile().addCard(player.getHand().get(0));
//            player.getHand().remove(0);
//        }
    }

}
