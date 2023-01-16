package game.player;

import game.card.Card;
import game.card.CardType;

public class AI_Player extends Player {
    public AI_Player(int id, String username, Robot robot) {
        super(id, username, robot);
    }
    public void playTurn(){
        //preparation
        playPreparation();
        //UPGRADE PHASE
        playUpgradePhase();
        //PROGRAMMING PHASE
        playProgrammingPhase();
        //ACTIVATION PHASE
    }
    @Override
    public void playPreparation(){
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

}
