package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.GameManager;

public class StrategyFactory {
    private final String[] strategies = {"AlwaysBetray", "AlwaysCooperate"};

    public Strategy createStrategy(String strategyName) {
        switch(strategyName) {
            case "AlwaysBetray":
                return new AlwaysBetray();
            case "AlwaysCooperate":
                return new AlwaysCooperate();
            case "PavlovRandom":
                return new PavlovRandom();
            default:
                return createStrategy(getRandomStrategy());
        }
    }

    private String getRandomStrategy() {
        return strategies[GameManager.getInstance().getRandom().nextInt(strategies.length)];
    }
}
