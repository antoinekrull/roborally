package game.board;

import game.player.Player;

/**
 * @author Antoine, Firas
 * @version 1.0
 */
public class CheckpointTile extends Tile {
    private String file;
    private int count;
    public CheckpointTile(int xCoordinate, int yCoordinate, int count, boolean single) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/checkpoint.png", "Checkpoint");
        if (count == 1) {
            file ="/textures/gameboard/checkpoint1";
            this.imageFXid = "Checkpoint1";
        } else if (count == 2) {
            file = "/textures/gameboard/checkpoint2";
            this.imageFXid = "Checkpoint2";
        } else if (count == 3) {
            file = "/textures/gameboard/checkpoint3";
            this.imageFXid = "Checkpoint3";
        } else if (count == 4) {
            file = "/textures/gameboard/checkpoint4";

            this.imageFXid = "Checkpoint4";
        } else if (count == 5) {
            file = "/textures/gameboard/checkpoint5";

            this.imageFXid = "Checkpoint5";
        } else {
            file = "/textures/gameboard/checkpoint";
            this.imageFXid = "Checkpoint";
        }
        if(!single){
            file = file + "_noBackground.png";
            System.out.println(file);
        }
        else{
            file = file + ".png";
        }
        this.path = getClass().getResource(file).toString();

        setType("CheckPoint");
        isDanger = false;
        isBlocking = false;
        this.count = count;
    }

    @Override
    public void applyEffect(Player player) {
        if(player.getRobot().getCurrentObjective() == (count - 1)) {
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
