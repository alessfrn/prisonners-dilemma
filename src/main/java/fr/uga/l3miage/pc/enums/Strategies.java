package fr.uga.l3miage.pc.enums;

import fr.uga.l3miage.pc.classes.game.GameManager;

import static java.util.Arrays.stream;

public enum Strategies {
    GIVE_AND_TAKE,
    GIVE_AND_TAKE_RANDOM,
    GIVE_AND_TAKE_RANDOM_2,
    GIVE_AND_TAKE_2,
    NAIVE_PROBER,
    REPENTANT_PROBER,
    NAIVE_PEACEMAKER,
    TRUE_PEACEMAKER,
    RANDOM,
    ALWAYS_BETRAY,
    ALWAYS_COOPERATE,
    RESENTFUL,
    PAVLOV,
    PAVLOV_RANDOM,
    ADAPTIVE,
    GRADUAL,
    SUSPICIOUS_GIVE_AND_TAKE,
    SWEET_RESENTFUL,
    GET_RANDOM_STRATEGY,
    ALWAYS_BETRAY_FROM_RACHID, // Strategy that will be taken from the other group's project
    ALWAYS_COOPERATE_FROM_RACHID; // Strategy that will be taken from the other group's project

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