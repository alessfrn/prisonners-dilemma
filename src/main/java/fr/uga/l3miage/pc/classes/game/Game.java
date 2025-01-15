package fr.uga.l3miage.pc.classes.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uga.l3miage.pc.enums.GameStatus;
import fr.uga.l3miage.pc.enums.GameType;
import fr.uga.l3miage.pc.enums.TribeAction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static java.lang.Math.abs;
import static java.lang.Math.max;

@Getter
@Slf4j
public class Game {
    @JsonIgnore
    private final Turn[] turns;
    @JsonIgnore
    private final Tribe[] tribes = new Tribe[2];
    private final UUID id = UUID.randomUUID();
    @JsonIgnore
    private int currentTurn = 0;
    private final int maxTurns;
    @JsonIgnore
    private GameType gameType;
    @JsonIgnore
    private GameStatus gameStatus = GameStatus.NEW;
    @JsonIgnore
    private final TribeAction[] turnActions = new TribeAction[2];

    public Game(int turns, GameType gameType, Tribe creator) {
        this.maxTurns = turns;
        this.gameType = gameType;
        this.turns = new Turn[turns];
        this.tribes[0] = creator;
        creator.setGame(this);
    }

    public void joinGame(Tribe tribe) throws IllegalStateException {
        if (tribes[1] == null) {
            tribes[1] = tribe;
            tribe.setGame(this);
            startGame();
        } else {
            throw new IllegalStateException("Game is full");
        }
    }

    public void playTurn(TribeAction playerAction, int playerIndex) {
        turnActions[playerIndex] = playerAction;
        if (gameStatus.equals(GameStatus.WAITING_BOTH)) {
            gameStatus = playerIndex == 0 ? GameStatus.WAITING_P2 : GameStatus.WAITING_P1;
        }
        if (gameType.equals(GameType.AI)) {
            turnActions[abs(playerIndex - 1)] = tribes[abs(playerIndex - 1)].getStrategy().calculateAction(this);
        }
        if (turnActions[0] != null && turnActions[1] != null) {
            try {
                resolveActions(turnActions[0], turnActions[1]);
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        }
    }

    private void replacePlayerWithAI(int playerIndex) {
        tribes[playerIndex].playerToAI();
        gameType = GameType.AI;
    }

    public void resolveActions(TribeAction player1Action, TribeAction player2Action) throws IllegalStateException {
        if (shouldEndGameForLeaving(player1Action, player2Action)) {
            GameManager.getInstance().endGame(this, true);
            return;
        }

        TribeAction[] actions = handlePlayerLeaving(player1Action, player2Action);
        player1Action = actions[0];
        player2Action = actions[1];

        if (player1Action.equals(TribeAction.COOPERATE) && player2Action.equals(TribeAction.COOPERATE)) {
            turns[currentTurn] = new Turn(new TurnAction(player1Action, 3), new TurnAction(player2Action, 3));
        } else if (player1Action.equals(TribeAction.BETRAY) && player2Action.equals(TribeAction.BETRAY)) {
            turns[currentTurn] = new Turn(new TurnAction(player1Action, 1), new TurnAction(player2Action, 1));
        } else if (player1Action.equals(TribeAction.COOPERATE) && player2Action.equals(TribeAction.BETRAY)) {
            turns[currentTurn] = new Turn(new TurnAction(player1Action, 0), new TurnAction(player2Action, 5));
        } else if (player1Action.equals(TribeAction.BETRAY) && player2Action.equals(TribeAction.COOPERATE)) {
            turns[currentTurn] = new Turn(new TurnAction(player1Action, 5), new TurnAction(player2Action, 0));
        } else {
            throw new IllegalStateException("Invalid actions");
        }
        if (++currentTurn >= maxTurns) {
            GameManager.getInstance().endGame(this, false);
            return;
        }
        turnActions[0] = null;
        turnActions[1] = null;
        gameStatus = GameStatus.WAITING_BOTH;
    }

    private TribeAction[] handlePlayerLeaving(TribeAction player1Action, TribeAction player2Action) {
        if (player1Action.equals(TribeAction.LEAVE)) {
            replacePlayerWithAI(0);
            player1Action = tribes[0].getStrategy().calculateAction(this);
        } else if (player2Action.equals(TribeAction.LEAVE)) {
            replacePlayerWithAI(1);
            player2Action = tribes[1].getStrategy().calculateAction(this);
        }
        return new TribeAction[]{player1Action, player2Action};
    }

    private boolean shouldEndGameForLeaving(TribeAction player1Action, TribeAction player2Action) {
        return (player1Action.equals(TribeAction.LEAVE) && gameType.equals(GameType.AI)) ||
                (player2Action.equals(TribeAction.LEAVE) && gameType.equals(GameType.AI)) ||
                (player1Action.equals(TribeAction.LEAVE) && player2Action.equals(TribeAction.LEAVE));
    }

    public int fetchPreviousSystemTurnScoring() throws IllegalStateException {
        if (currentTurn > 0) {
            return turns[currentTurn - 1].getActions()[0].getScoring();
        }
        throw new IllegalStateException("No previous turn");
    }

    public TribeAction getPreviousTurnAction(int player) throws IllegalStateException {
        if (currentTurn > 0) {
            return turns[currentTurn - 1].getActions()[player].getAction();
        }
        throw new IllegalStateException("No previous turn");
    }

    public List<TribeAction> getXPreviousTurnActions(int player, int nbTurns) throws IllegalStateException {
        if (currentTurn < nbTurns) {
            throw new IllegalStateException("Not enough turns to execute this method");
        }
        ArrayList<TribeAction> actions = new ArrayList<>();
        for (int i = 1; i < nbTurns + 1; i++) {
            actions.add(turns[currentTurn - i].getActions()[player].getAction());
        }
        return actions;
    }

    public void finishGame(boolean leave) {
        if (leave) {
            gameStatus = GameStatus.ABANDONED;
        } else {
            gameStatus = GameStatus.COMPLETED;
        }
        Arrays.stream(tribes).forEach(tribe -> tribe.setGame(null));
    }

    public void startGame() throws IllegalStateException {
        if (gameStatus.equals(GameStatus.NEW) || gameStatus.equals(GameStatus.OPEN_LOBBY)) {
         this.gameStatus = GameStatus.WAITING_BOTH;
        } else {
            throw new IllegalStateException("Game cannot be started");
        }
    }

    public void openLobby() throws IllegalStateException {
        if (gameStatus.equals(GameStatus.NEW)) {
            this.gameStatus = GameStatus.OPEN_LOBBY;
        } else {
            throw new IllegalStateException("Game already started");
        }
    }
}