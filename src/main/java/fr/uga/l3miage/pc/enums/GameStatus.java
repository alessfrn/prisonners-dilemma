package fr.uga.l3miage.pc.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GameStatus {
    @JsonProperty("NEW")
    NEW,
    @JsonProperty("WAITING_FOR_PLAYERS")
    WAITING_FOR_PLAYERS,
    @JsonProperty("PLAYING")
    PLAYING,
    @JsonProperty("ABANDONED")
    ABANDONED,
    @JsonProperty("COMPLETED")
    COMPLETED
}
