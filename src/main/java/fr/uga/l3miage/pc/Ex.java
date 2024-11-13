package fr.uga.l3miage.pc;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameConfig;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.classes.game.Tribe;
import fr.uga.l3miage.pc.enums.GameStatus;
import fr.uga.l3miage.pc.enums.GameType;
import fr.uga.l3miage.pc.enums.Strategies;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.core.AbstractDestinationResolvingMessagingTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class Ex {
    Map<UUID, Tribe> world = new ConcurrentHashMap<>();
    private SimpMessagingTemplate template ;

    @Autowired
    public Ex(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/register")
    @SendToUser("queue/register")
    public String registerPlayer() throws Exception {
        UUID uuid = UUID.randomUUID();
        world.put(uuid, new Tribe(GameManager.getInstance().getStrategyFactory().createStrategy(Strategies.GetRandomStrategy), true));
        return uuid.toString();
    }

    @MessageMapping("/start")
    @SendToUser("/queue/start")
    public GameStatus greeting(@Payload GameConfig config) {
        try {
            Game game = GameManager.getInstance().startNewGameRandomStrategy(config.getMaxTurns(), config.getGameType(), world.get(UUID.fromString(config.getUserId())));
            // template.convertAndSend("/queue/status/" + config.getUserId() , getGameStatus(game.getGameStatus()));
            return game.getGameStatus();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
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
        return GameManager.getInstance().getActiveGames().stream().filter(game -> game.getGameStatus().equals(GameStatus.WAITING_FOR_PLAYERS)).toList();
    }
}
