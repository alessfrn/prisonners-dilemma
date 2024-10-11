package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.enums.TribeAction;
import lombok.Getter;

@Getter
public class Game {
    private Turn[] turns;
    private Tribe[] tribes = new Tribe[2];

    @Getter
    private int currentTurn = 0;

    @Getter
    private final int maxTurns;

    public Game(int turns) {
        this.maxTurns = turns;
        this.turns = new Turn[turns];
        tribes[0] = new Tribe(GameManager.getInstance().getStrategyFactory().createStrategy(""));
    }

    public Game(int turns, String strategyName) {
        this.maxTurns = turns;
        this.turns = new Turn[turns];
        tribes[0] = new Tribe(GameManager.getInstance().getStrategyFactory().createStrategy(strategyName));
    }

    public void addTribe(Tribe tribe) throws IllegalStateException {
        if (tribes[1] == null) {
            tribes[1] = tribe;
        } else {
            throw new IllegalStateException("Game is full");
        }
    }

    public void playTurn(TribeAction playerAction) {
        TribeAction systemAction = tribes[0].getStrategy().calculateAction();
        try {
            resolveActions(systemAction, playerAction);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
        currentTurn++;
    }

    public void resolveActions(TribeAction systemAction, TribeAction playerAction) throws IllegalStateException {
        if (systemAction.equals(TribeAction.COOPERATE) && playerAction.equals(TribeAction.COOPERATE)) {
            turns[currentTurn] = new Turn(new TurnAction(systemAction, 3), new TurnAction(playerAction, 3));
        } else if (systemAction.equals(TribeAction.BETRAY) && playerAction.equals(TribeAction.BETRAY)) {
            turns[currentTurn] = new Turn(new TurnAction(systemAction, 1), new TurnAction(playerAction, 1));
        } else if (systemAction.equals(TribeAction.COOPERATE) && playerAction.equals(TribeAction.BETRAY)) {
            turns[currentTurn] = new Turn(new TurnAction(systemAction, 0), new TurnAction(playerAction, 5));
        } else if (systemAction.equals(TribeAction.BETRAY) && playerAction.equals(TribeAction.COOPERATE)) {
            turns[currentTurn] = new Turn(new TurnAction(systemAction, 5), new TurnAction(playerAction, 0));
        } else {
            throw new IllegalStateException("Invalid actions");
        }
    }

    public int getPreviousSystemTurnScoring() throws IllegalStateException {
        if (currentTurn > 0) {
            return turns[currentTurn - 1].getActions()[0].getScoring();
        }
        throw new IllegalStateException("No previous turn");
    }

    public TribeAction getPreviousSystemTurnAction() throws IllegalStateException {
        if (currentTurn > 0) {
            return turns[currentTurn - 1].getActions()[0].getAction();
        }
        throw new IllegalStateException("No previous turn");
    }
}