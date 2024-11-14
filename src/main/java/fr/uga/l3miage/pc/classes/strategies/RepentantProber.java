package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;

public class RepentantProber implements Strategy  {
    private boolean testDone = false;
    private int testTurn;
    @Override
    public TribeAction calculateAction(Game game) throws IllegalStateException {
        if (game == null) {
            throw new IllegalStateException("No active game");
        }
        if (game.getCurrentTurn() == 0) {
            return TribeAction.returnRandomAction();
        }
        if (testDone && (testTurn) == game.getCurrentTurn() && game.getPreviousTurnAction(0).equals(TribeAction.BETRAY)) {
            testDone = false;
            return TribeAction.COOPERATE;
        }
        if (GameManager.getInstance().getRandom().nextInt(5) == 3) {
            if (!testDone) {
                testDone = true;
                testTurn = game.getCurrentTurn() + 2;
            }
            return TribeAction.BETRAY;
        }
        return game.getPreviousTurnAction(0);
    }
}
