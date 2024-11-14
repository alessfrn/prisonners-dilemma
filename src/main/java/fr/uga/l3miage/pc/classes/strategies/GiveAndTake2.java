package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

import java.util.List;

public class GiveAndTake2 implements Strategy {
    @Override
    public TribeAction calculateAction(Game game) {
        if (game.getCurrentTurn() <= 1) {
            return TribeAction.returnRandomAction();
        }
        return getConsecutiveMove(game);
    }

    private TribeAction getConsecutiveMove(Game game) {
        List<TribeAction> actions = game.getXPreviousTurnActions(0,2);
        if (actions.get(0).equals(actions.get(1))) {
            return actions.get(0);
        } else {
            return TribeAction.returnRandomAction();
        }
    }
}
