package fr.uga.l3miage.pc.classes.strategies;

import fr.uga.l3miage.pc.enums.TribeAction;

public interface Strategy {
    public TribeAction calculateAction();
}