package fr.uga.l3miage.pc.classes.game;

import lombok.Getter;

@Getter
public class Turn {
    private TurnAction[] actions = new TurnAction[2];

    public Turn(TurnAction action1, TurnAction action2) {
        this.actions[0] = action1;
        this.actions[1] = action2;
    }
}
