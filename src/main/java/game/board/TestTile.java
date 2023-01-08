package game.board;





//Testclass to see if the arraylist is working correctly
public class TestTile {

    public String type;
    public String isOnBoard;
    public String orientations;
    private int velocity;
    public int count;


    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public TestTile(String type) {
        this.type = type;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public String getTileType() {
        return type;
    }
    public String getOrientations() {
        return orientations;
    }
    public void setOrientations(String orientations) {
        this.orientations = orientations;
    }
    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }
}



