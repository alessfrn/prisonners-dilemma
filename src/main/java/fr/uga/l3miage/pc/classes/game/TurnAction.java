package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.enums.TribeAction;
import lombok.Getter;


@Getter
public class TurnAction {
    private TribeAction action;

    private int scoring;

    public TurnAction(TribeAction action, int scoring) {
        this.action = action;
        this.scoring = scoring;
    }


}
