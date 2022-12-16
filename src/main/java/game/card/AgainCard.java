package game.card;

import game.robot.Robot;

public class AgainCard extends Card{

    private CardType cardType = CardType.PROGRAMMING_CARD;

    public AgainCard(){
        setCardName("Again");
    }

    @Override
    public void applyEffect(Robot robot) throws Exception {
        int previousRegister = robot.getOwner().getCurrentRegister(this) - 1;
        robot.getOwner().getCardFromRegister(previousRegister).applyEffect(robot);
    }
}
