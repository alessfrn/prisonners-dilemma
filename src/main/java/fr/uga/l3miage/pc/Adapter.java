package fr.uga.l3miage.pc;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.strategies.Strategy;
import fr.uga.l3miage.pc.enums.Strategies;
import fr.uga.l3miage.pc.enums.TribeAction;
import fr.uga.miage.m1.my_projet_g1_10.enums.Decision;
import fr.uga.miage.m1.my_projet_g1_10.enums.Strategie;
import fr.uga.miage.m1.my_projet_g1_10.strategies.IStrategie;
import fr.uga.miage.m1.my_projet_g1_10.strategiesCreators.StrategieFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class Adapter implements Strategy {
    private final IStrategie strategyFromRachid;


    public Adapter(Strategies s) {
        this.strategyFromRachid = StrategieFactory.getStrategie(convertMyEnumToRachidEnum(s));
    }
    public Strategie convertMyEnumToRachidEnum(Strategies s) {
        return switch (s) {
            case ALWAYS_BETRAY_FROM_RACHID -> Strategie.TOUJOURSTRAHIR;
            case ALWAYS_COOPERATE_FROM_RACHID -> Strategie.TOUJOURSCOOPERER;
            default -> {
                log.error("Unhandled strategy");
                yield null;
            }
        };
    }

    public List<Decision> convertGameToDecisionList(Game game) {
        List<TribeAction> actions = game.getXPreviousTurnActions(1, game.getCurrentTurn());
        return actions.stream().map(this::convertTribeActionToDecision).toList();
    }

    public Decision convertTribeActionToDecision(TribeAction action) {
        if (action == TribeAction.COOPERATE) return Decision.COOPERER;
        else return Decision.TRAHIR;
    }

    public TribeAction convertDecisionToTribeAction(Decision d) {
        if (d == Decision.COOPERER) return TribeAction.COOPERATE;
        else return TribeAction.BETRAY;
    }

    public TribeAction calculateAction(Game game) {
        return convertDecisionToTribeAction(strategyFromRachid.decider(convertGameToDecisionList(game)));
    }
}
