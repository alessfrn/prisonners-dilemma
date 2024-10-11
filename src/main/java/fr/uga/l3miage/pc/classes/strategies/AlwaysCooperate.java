package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.enums.TribeAction;

public class AlwaysCooperate implements Strategy {
    public TribeAction calculateAction() {
        return TribeAction.COOPERATE;
    }
}
