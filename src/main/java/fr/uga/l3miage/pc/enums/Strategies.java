package fr.uga.l3miage.pc.enums;

import fr.uga.l3miage.pc.classes.game.GameManager;

import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public enum Strategies {
    GiveAndTake,
    GiveAndTakeRandom,
    GiveAndTakeRandom2,
    GiveAndTake2,
    NaiveProber,
    RepentantProber,
    NaivePeacemaker,
    TruePeacemaker,
    Random,
    AlwaysBetray,
    AlwaysCooperate,
    Resentful,
    Pavlov,
    PavlovRandom,
    Adaptive,
    Gradual,
    SuspiciousGiveAndTake,
    SweetResentful,
    GetRandomStrategy;

    public static Strategies getStrategyFromInteger(int integerStrategy) {
        return stream(Strategies.values())
                .filter(strategy -> strategy.ordinal() == integerStrategy)
                .toList()
                .get(0);
    }

    public static Strategies getRandomStrategy() {
        return getStrategyFromInteger(GameManager.getInstance().getRandom().nextInt(Strategies.values().length));
    }
}