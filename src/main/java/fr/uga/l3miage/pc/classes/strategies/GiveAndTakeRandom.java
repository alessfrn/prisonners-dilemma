package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

public class GiveAndTakeRandom implements Strategy {
    @Override
    public TribeAction calculateAction(Game game) {
        if (game.getCurrentTurn() == 0 || GameManager.getInstance().getRandom().nextInt(5) == 3) {
            return TribeAction.returnRandomAction();
        }
        return game.getPreviousTurnAction(0);
    }
}
