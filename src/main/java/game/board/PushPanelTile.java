package game.board;

import game.Game;
import game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.javatuples.Pair;

import java.util.ArrayList;

public class PushPanelTile extends Tile {

    Direction pushDirection;
    ArrayList<Integer> registers;
    Pair<Integer, Integer> tileLocation;

    ArrayList<Integer> activeRegisterList = new ArrayList<>();
    public PushPanelTile(int xCoordinate, int yCoordinate, Direction pushDirection, ArrayList<Integer> registers) {
        super(xCoordinate, yCoordinate, "/textures/gameboard/pushPanel.png", "PushPanel");
        if (registers.size()==2){
            this.path = getClass().getResource("/textures/gameboard/pushPanel.png").toString();
            this.imageFXid = "PushPanelRegister2";
        }
        else {
            //TODO: change picture with new push panel with registers
            this.path = getClass().getResource("/textures/gameboard/pushPanel.png").toString();
            this.imageFXid = "PushPanel";
        }

        setType("PushPanel");
        isBlocking = true;
        isDanger = false;
        this.pushDirection = pushDirection;
        this.registers = registers;
        tileLocation = new Pair<>(xCoordinate, yCoordinate);
    }

    public Direction getPushDirection() {
        return pushDirection;
    }

    @Override
    public void applyEffect(Player player) throws Exception {
        //TODO: Ask GUI team if they need extra info for animations
        if(registers.contains(Game.currentRegister)){
            Pair<Integer, Integer> newPosition = new Pair<>(player.getRobot().getCurrentPosition().getValue0(),
                    player.getRobot().getCurrentPosition().getValue1());
            if(player.getRobot().getCurrentPosition().equals(tileLocation)){
                Pair<Integer, Integer> tempPosition;
                switch(this.pushDirection){
                    case NORTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() + 1);
                    case SOUTH -> tempPosition = newPosition.setAt1(newPosition.getValue1() - 1);
                    case EAST -> tempPosition = newPosition.setAt0(newPosition.getValue0() + 1);
                    case WEST -> tempPosition = newPosition.setAt0(newPosition.getValue0() - 1);
                    default -> throw(new Exception("Invalid direction"));
                }
                player.getRobot().setCurrentPosition(tempPosition);
            }
        }
    }
    public ArrayList<Integer> getActiveRegisterList() {
        return activeRegisterList;
    }
    public void setActiveRegisterList(ArrayList<Integer> activeRegisterList) {
        this.activeRegisterList = activeRegisterList;
    }
    @Override
    public void makeImage(GridPane tiles){
        ImageView img = new ImageView();
        Image im = new Image(path,(double) height, 70,true,false);
        img.setImage(im);
        int rot = 0;
        switch (pushDirection) {
            case NORTH -> rot = 0;
            case EAST -> rot = 90;
            case SOUTH -> rot = 180;
            case WEST -> rot = 270;
        }
        img.setRotate(rot);
        tiles.add(img,this.xCoordinate,this.yCoordinate);
    }
}
