package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile {

    private int count;
    public CheckpointTile(int xCoordinate, int yCoordinate, int count) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/checkpoint.png", "Checkpoint");
        if (count == 1) {
            this.path = getClass().getResource("/textures/gameboard/checkpoint1.png").toString();
            this.imageFXid = "Checkpoint1";
        } else if (count == 2) {
            this.path = getClass().getResource("/textures/gameboard/checkpoint2.png").toString();
            this.imageFXid = "Checkpoint2";
        } else if (count == 3) {
            this.path = getClass().getResource("/textures/gameboard/checkpoint3.png").toString();
            this.imageFXid = "Checkpoint3";
        } else if (count == 4) {
            this.path = getClass().getResource("/textures/gameboard/checkpoint4.png").toString();
            this.imageFXid = "Checkpoint4";
        } else if (count == 5) {
            //TODO: replace with image of 5. checkpoint
            this.path = getClass().getResource("/textures/gameboard/checkpoint4.png").toString();
            this.imageFXid = "Checkpoint4";
        } else {
            this.path = getClass().getResource("/textures/gameboard/checkpoint.png").toString();
            this.imageFXid = "Checkpoint";
        }

        setType("Checkpoint");
        isDanger = false;
        isBlocking = false;
        this.count = count;
    }

    @Override
    public void applyEffect(Player player) {
        if(player.getRobot().getCurrentObjective() == (count - 1)) {
            System.out.println("Penis");
            player.getRobot().increaseObjectiveNumber();
        }
    }
    @Override
    public int getCount() {
        return count;
    }
}


//        if (registers.size()==2){
//            this.path = getClass().getResource("/textures/gameboard/pushPanel.png").toString();
//            this.imageFXid = "PushPanelRegister2";
//        }
//        else {
//            this.path = getClass().getResource("/textures/gameboard/pushPanel_1_3_5.png").toString();
//            this.imageFXid = "PushPanel";
//        }
