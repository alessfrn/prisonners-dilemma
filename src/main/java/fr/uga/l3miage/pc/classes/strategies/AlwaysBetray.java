package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.enums.TribeAction;

public class AlwaysBetray implements Strategy {
    public TribeAction calculateAction() {
        return TribeAction.BETRAY;
    }
}
