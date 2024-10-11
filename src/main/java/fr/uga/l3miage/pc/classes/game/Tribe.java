package fr.uga.l3miage.pc.classes.game;

import fr.uga.l3miage.pc.classes.strategies.Strategy;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Tribe {
    private final UUID id = UUID.randomUUID();
    private Strategy strategy;

    public Tribe(Strategy strategy) {
        this.strategy = strategy;
    }

    public Tribe() {}
}
