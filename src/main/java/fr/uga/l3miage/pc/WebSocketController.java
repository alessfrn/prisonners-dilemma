package fr.uga.l3miage.pc;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameConfig;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.classes.game.Tribe;
import fr.uga.l3miage.pc.enums.GameStatus;
import fr.uga.l3miage.pc.enums.Strategies;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {
    @Getter
    Map<UUID, Tribe> world = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate template ;

    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/register")
    @SendToUser("queue/register")
    public String registerPlayer(@Header("simpSessionId") String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        world.put(uuid, new Tribe(GameManager.getInstance().getStrategyFactory().createStrategy(Strategies.GetRandomStrategy), true));
        return uuid.toString();
    }

    @MessageMapping("/create-game")
    @SendToUser("queue/create-game")
    public String createGame(@Payload GameConfig config) {
        try {
            Game game = GameManager.getInstance().startNewGame(config.getMaxTurns(), config.getGameType(), world.get(config.getUserId()));
            return game.getId().toString();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @MessageMapping("/status")
    @SendToUser("/queue/status")
    public GameStatus getGameStatus(GameStatus gameStatus) {
        return gameStatus;
    }

    @MessageMapping("/fetch-games")
    @SendToUser("/queue/fetch-games")
    public List<Game> fetchGames() {
        return GameManager.getInstance().getActiveGames().stream().filter(game -> game.getGameStatus().equals(GameStatus.OPEN_LOBBY)).toList();
    }

    @MessageMapping("/join")
    public void joinGame(@Payload GameConfig gameConfig) {
        Game game = GameManager.getInstance().findGameWithID(gameConfig.getGameId());
        game.joinGame(world.get(gameConfig.getUserId()));
        try {
            template.convertAndSend("/queue/status/" + game.getId().toString(), getGameStatus(game.getGameStatus()));
        } catch (MessageDeliveryException e) {
            System.out.println("Cannot send message");
        }
    }

    @MessageMapping("/take-action")
    public void takeAction(@Payload GameConfig gameConfig) {
        Game game = world.get(gameConfig.getUserId()).getGame();
        game.playTurn(gameConfig.getAction(), gameConfig.getPlayerIndex());
        try {
            template.convertAndSend("/queue/status/" + game.getId().toString(), getGameStatus(game.getGameStatus()));
        } catch (MessageDeliveryException e) {
            System.out.println("Cannot send message");
        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        Tribe userTribe = world.get(UUID.fromString(event.getSessionId()));
        if (userTribe.getGame() != null) {
            GameManager.getInstance().disconnectUser(userTribe);
            template.convertAndSend("/queue/status/" + userTribe.getGame().getId().toString(), getGameStatus(userTribe.getGame().getGameStatus()));
        }
    }

}
