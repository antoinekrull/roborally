package game.card;

import java.util.Collections;

public class UpgradeDeck extends Deck{
    @Override
    public void createDeck() {
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 4; j++){
                addCard(new AdminPrivilegeCard());
                addCard(new RearLaserCard());
                addCard(new MemorySwapCard());
                addCard(new SpamBlockerCard());
            }
        }
        Collections.shuffle(super.deck);
    }
}
