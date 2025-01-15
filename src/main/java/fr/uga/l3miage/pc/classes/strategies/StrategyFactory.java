package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.Adapter;
import fr.uga.l3miage.pc.enums.Strategies;

public class StrategyFactory {
    public Strategy createStrategy(Strategies strategy) {
        switch(strategy) {
            case ALWAYS_BETRAY:
                return new AlwaysBetray();
            case ALWAYS_COOPERATE:
                return new AlwaysCooperate();
            case PAVLOV_RANDOM:
                return new PavlovRandom();
            case TRUE_PEACEMAKER:
                return new TruePeacemaker();
            case GIVE_AND_TAKE:
                return new GiveAndTake();
            case GIVE_AND_TAKE_RANDOM:
                return new GiveAndTakeRandom();
            case GIVE_AND_TAKE_2:
                return new GiveAndTake2();
            case GIVE_AND_TAKE_RANDOM_2:
                return new GiveAndTakeRandom2();
            case NAIVE_PROBER:
                return new NaiveProber();
            case REPENTANT_PROBER:
                return new RepentantProber();
            case RANDOM:
                return new Random();
            case ALWAYS_BETRAY_FROM_RACHID:
                return new Adapter(Strategies.ALWAYS_BETRAY_FROM_RACHID);
            case ALWAYS_COOPERATE_FROM_RACHID:
                return new Adapter(Strategies.ALWAYS_COOPERATE_FROM_RACHID);
            default:
                return createStrategy(Strategies.getRandomStrategy());
        }
    }
}
