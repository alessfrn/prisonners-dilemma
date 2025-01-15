package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.classes.strategies.Strategy;
import fr.uga.l3miage.pc.classes.strategies.StrategyFactory;
import fr.uga.l3miage.pc.enums.GameType;
import fr.uga.l3miage.pc.enums.Strategies;
import fr.uga.l3miage.pc.enums.TribeAction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.*;

@Getter
@Slf4j
public class GameManager {
    private static GameManager instance;

    private final StrategyFactory strategyFactory = new StrategyFactory();

    private Random random = new SecureRandom();

    private final ArrayList<Game> activeGames = new ArrayList<>();

    private final ArrayList<Game> finishedGames = new ArrayList<>();

    private GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public Game startNewGame(int turns, GameType gameType, Tribe tribe) throws IllegalArgumentException {
        if (turns <= 0) {
            throw new IllegalArgumentException("A game cannot last 0 turns");
        }
        if (tribe.getGame() != null) {
            throw new IllegalArgumentException("Tribe is already in a game");
        }
        Game newGame = new Game(turns, gameType, tribe);
        if (gameType.equals(GameType.AI)) {
            try {
                newGame.joinGame(new Tribe(getRandomStrategy(), false));
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        } else {
            newGame.openLobby();
        }
        activeGames.add(newGame);
        return newGame;
    }

    public Strategy getRandomStrategy() {
        return strategyFactory.createStrategy(Strategies.getRandomStrategy());
    }

    public void endGame(Game game, boolean leave) {
        game.finishGame(leave);
        finishedGames.add(game);
        activeGames.remove(game);
    }

    public void changeRandom(Random random) {
        this.random = random;
    }

    public Game findGameWithID(UUID gameId) throws NoSuchElementException {
        return activeGames.stream().filter(x -> x.getId().equals(gameId)).findFirst().orElseThrow();
    }

    public void disconnectUser(Tribe tribe) {
        tribe.getGame().playTurn(TribeAction.LEAVE, Arrays.stream(tribe.getGame().getTribes()).toList().indexOf(tribe));
    }
}
