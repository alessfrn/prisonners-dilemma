package fr.uga.l3miage.pc.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GameType {
    @JsonProperty("VERSUS")
    VERSUS,
    @JsonProperty("AI")
    AI;
}
