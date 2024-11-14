package fr.uga.l3miage.pc.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameStatus {
    @JsonProperty("NEW")
    NEW,
    @JsonProperty("OPEN_LOBBY")
    OPEN_LOBBY,
    @JsonProperty("CLOSED_LOBBY")
    CLOSED_LOBBY,
    @JsonProperty("READY_P1")
    READY_P1,
    @JsonProperty("READY_P2")
    READY_P2,
    @JsonProperty("READY")
    READY,
    @JsonProperty("WAITING_BOTH")
    WAITING_BOTH,
    @JsonProperty("WAITING_P1")
    WAITING_P1,
    @JsonProperty("WAITING_P2")
    WAITING_P2,
    @JsonProperty("ABANDONED")
    ABANDONED,
    @JsonProperty("COMPLETED")
    COMPLETED
}
