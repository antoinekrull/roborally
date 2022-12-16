package game.card;

public class MoveCard2 extends Card {
    int velocity = 2;

    public MoveCard2(){
        setCardName("Move 2");
    }

    public int getVelocity() {
        return velocity;
    }
    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

}
