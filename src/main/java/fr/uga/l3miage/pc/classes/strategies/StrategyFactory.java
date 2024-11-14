package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.Strategies;

public class StrategyFactory {
    public Strategy createStrategy(Strategies strategy) {
        switch(strategy) {
            case AlwaysBetray:
                return new AlwaysBetray();
            case AlwaysCooperate:
                return new AlwaysCooperate();
            case PavlovRandom:
                return new PavlovRandom();
            case TruePeacemaker:
                return new TruePeacemaker();
            case GiveAndTake:
                return new GiveAndTake();
            case GiveAndTakeRandom:
                return new GiveAndTakeRandom();
            case GiveAndTake2:
                return new GiveAndTake2();
            case GiveAndTakeRandom2:
                return new GiveAndTakeRandom2();
            case NaiveProber:
                return new NaiveProber();
            case RepentantProber:
                return new RepentantProber();
            case Random:
                return new Random();
            default:
                return createStrategy(Strategies.getRandomStrategy());
        }
    }
}
