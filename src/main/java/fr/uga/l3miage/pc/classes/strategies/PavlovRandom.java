package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

public class PavlovRandom implements Strategy {
    public TribeAction calculateAction() throws IllegalStateException {
        Game game = GameManager.getInstance().getActiveGame();
        if (game == null) {
            throw new IllegalStateException("No active game");
        }
        if (game.getCurrentTurn() == 0 || GameManager.getInstance().getRandom().nextInt(5) == 3) {
            return TribeAction.returnRandomAction();
        }
        if (game.getPreviousSystemTurnScoring() == 3 || game.getPreviousSystemTurnScoring() == 5) {
            return game.getPreviousTurnAction(0);
        }
        return TribeAction.getOppositeAction(game.getPreviousTurnAction(0));
    }
}
