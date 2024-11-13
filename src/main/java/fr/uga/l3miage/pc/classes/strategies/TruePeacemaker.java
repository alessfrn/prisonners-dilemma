package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

import java.util.List;

public class TruePeacemaker implements Strategy {
    @Override
    public TribeAction calculateAction(Game game) {
        if (game.getCurrentTurn() == 0 || game.getCurrentTurn() == 1 || GameManager.getInstance().getRandom().nextInt(5) == 3) {
            return TribeAction.COOPERATE;
        }
        if (checkForConsecutiveBetrayals(game)) {
            return TribeAction.BETRAY;
        }
        return TribeAction.COOPERATE;
    }

    private boolean checkForConsecutiveBetrayals(Game game) {
        List<TribeAction> actions = game.getXPreviousTurnActions(1,2);
        return (actions.get(0).equals(actions.get(1)) && actions.get(0).equals(TribeAction.BETRAY));
    }
}
