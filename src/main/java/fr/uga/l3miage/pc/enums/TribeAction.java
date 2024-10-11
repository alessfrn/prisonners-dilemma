package fr.uga.l3miage.pc.enums;

import fr.uga.l3miage.pc.classes.game.GameManager;

public enum TribeAction {
    COOPERATE,
    BETRAY;

    public static TribeAction getActionFromInteger(int integerAction) {
        if (integerAction == 0) {
            return COOPERATE;
        } else {
            return BETRAY;
        }
    }

    public static TribeAction getOppositeAction(TribeAction action) {
        if (action == COOPERATE) {
            return BETRAY;
        } else {
            return COOPERATE;
        }
    }

    public static TribeAction returnRandomAction() {
        return getActionFromInteger(GameManager.getInstance().getRandom().nextInt(2));
    }

}
