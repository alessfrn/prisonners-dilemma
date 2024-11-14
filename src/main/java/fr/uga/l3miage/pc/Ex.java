package fr.uga.l3miage.pc;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameConfig;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.classes.game.Tribe;
import fr.uga.l3miage.pc.enums.GameStatus;
import fr.uga.l3miage.pc.enums.Strategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

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
    public void greeting(@Payload GameConfig config) {
        try {
            Game game = GameManager.getInstance().startNewGameRandomStrategy(config.getMaxTurns(), config.getGameType(), world.get(config.getUserId()));
            template.convertAndSend("/queue/status/" + config.getUserId().toString() , getGameStatus(game.getGameStatus()));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
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

    @MessageMapping("/join")
    public void joinGame(@Payload GameConfig gameConfig) {
        Game game = GameManager.getInstance().findGameWithID(gameConfig.getGameId());
        if (game != null) {
            try {
                game.joinGame(world.get(gameConfig.getUserId()));
                template.convertAndSend("/queue/status/" + gameConfig.getUserId().toString() , getGameStatus(game.getGameStatus()));
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
