package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.enums.GameType;
import lombok.Getter;

@Getter
public class GameConfig {
    private int maxTurns;
    private GameType gameType;
    private String userId;

    public GameConfig() {}

    public GameConfig(int maxTurns, GameType gameType, String userId) {
        this.maxTurns = maxTurns;
        this.gameType = gameType;
        this.userId = userId;
    }

}
