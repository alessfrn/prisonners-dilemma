package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.enums.GameType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GameConfig {
    private int maxTurns;
    private GameType gameType;
    private UUID userId;
    private UUID gameId;

    public GameConfig() {}

    public GameConfig(int maxTurns, GameType gameType, UUID userId) {
        this.maxTurns = maxTurns;
        this.gameType = gameType;
        this.userId = userId;
    }

    public GameConfig(UUID userId, UUID gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

}
