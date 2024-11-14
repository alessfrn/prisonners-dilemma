package fr.uga.l3miage.pc.classes.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.uga.l3miage.pc.classes.strategies.Strategy;
import fr.uga.l3miage.pc.enums.Strategies;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Tribe {
    @JsonIgnore
    private Strategy strategy;
    @Setter
    private Game game;
    private boolean isPlayer;

    public Tribe(Strategy strategy, boolean isPlayer) {
        this.strategy = strategy;
        this.isPlayer = isPlayer;
    }

    public void playerToAI() {
        this.isPlayer = false;
    }
}