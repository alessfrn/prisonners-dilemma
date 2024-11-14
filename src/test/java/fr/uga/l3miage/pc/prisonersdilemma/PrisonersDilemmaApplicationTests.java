package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.WebSocketController;
import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameConfig;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.classes.game.Tribe;
import fr.uga.l3miage.pc.classes.strategies.GiveAndTake;
import fr.uga.l3miage.pc.enums.GameStatus;
import fr.uga.l3miage.pc.enums.GameType;
import fr.uga.l3miage.pc.enums.Strategies;
import fr.uga.l3miage.pc.enums.TribeAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.*;

class PrisonersDilemmaApplicationTests {
	private final Random mockedRandom = mock(SecureRandom.class);
	private final GameManager gameManager = GameManager.getInstance();
	private final int strategiesNumber = Strategies.values().length;

	private Tribe tribeGenerator() {
		return new Tribe(GameManager.getInstance().getRandomStrategy(), true);
	}

	@Test
	void testGameManagerInstance() {
		Assertions.assertNotNull(gameManager);
		Assertions.assertNotNull(gameManager.getStrategyFactory());
		Assertions.assertNotNull(gameManager.getRandom());
	}

	@Test
	void testGetRandomStrategy() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(0);
		Assertions.assertEquals(Strategies.GiveAndTake, Strategies.getRandomStrategy());
		Assertions.assertInstanceOf(GiveAndTake.class, GameManager.getInstance().getRandomStrategy());
	}

	@Test
	void testGameCreation() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> { gameManager.startNewGame(0, GameType.AI, tribeGenerator()); });
		Assertions.assertNotNull(gameManager.startNewGame(1, GameType.AI, tribeGenerator()));
	}

	@Test
	void testGetActionByIndex() {
		Assertions.assertEquals(TribeAction.COOPERATE, TribeAction.getActionFromInteger(0));
		Assertions.assertEquals(TribeAction.BETRAY, TribeAction.getActionFromInteger(1));
	}

	@Test
	void testGetOppositeAction() {
		Assertions.assertEquals(TribeAction.COOPERATE, TribeAction.getOppositeAction(TribeAction.BETRAY));
		Assertions.assertEquals(TribeAction.BETRAY, TribeAction.getOppositeAction(TribeAction.COOPERATE));
	}

	@Test
	void testValidRandomAction() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		Assertions.assertEquals(TribeAction.COOPERATE, TribeAction.returnRandomAction());
		when(mockedRandom.nextInt(2)).thenReturn(1);
		Assertions.assertEquals(TribeAction.BETRAY, TribeAction.returnRandomAction());
	}

	@Test
	void testAlwaysBetray() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(9);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
	}

	@Test
	void testAlwaysCooperate() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(10);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void testPavlovRandom() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(13);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(4);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void testTruePeacemaker() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(7);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(5)).thenReturn(0);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(3);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void testGiveAndTake() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(0);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		game.playTurn(TribeAction.COOPERATE, 0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
	}

	@Test
	void testGiveAndTakeRandom() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(1);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(5)).thenReturn(0);
		game.playTurn(TribeAction.COOPERATE, 0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(3);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void testGiveAndTake2() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(3);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void testGiveAndTakeRandom2() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(2);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(5)).thenReturn(0);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(3);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}
	@Test
	void testNaiveProber() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(4);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(5)).thenReturn(0);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(3);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
	}

	@Test
	void testRepentantProber() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(5);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(5)).thenReturn(0);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(5)).thenReturn(3);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
		game.playTurn(TribeAction.BETRAY, 0);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
	}

	@Test
	void assertGameConfig() {
		GameConfig gameConfig = new GameConfig();
		Assertions.assertNotNull(gameConfig);
		UUID uuid = UUID.randomUUID();
		gameConfig = new GameConfig(5, GameType.AI, uuid);
		Assertions.assertNotNull(gameConfig);
		Assertions.assertEquals(5, gameConfig.getMaxTurns());
		Assertions.assertEquals(GameType.AI, gameConfig.getGameType());
		Assertions.assertEquals(uuid, gameConfig.getUserId());
		gameConfig = new GameConfig(UUID.randomUUID(), TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, gameConfig.getAction());
		Assertions.assertEquals(0, gameConfig.getPlayerIndex());
	}

	@Test
	void testRemoteGameFetch() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		Assertions.assertEquals(new ArrayList<Game>(),webSocketController.fetchGames());
	}

	@Test
	void testRemoteRegister() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		Assertions.assertEquals("46f39dbc-b122-40b0-bcf8-6c143f4db334",webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db334"));
	}

	@Test
	void testRemoteGameCreation() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db334");
		String gameId = webSocketController.createGame(new GameConfig(5, GameType.AI, UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334")));
		Assertions.assertEquals(gameId, webSocketController.getWorld().get(UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334")).getGame().getId().toString());
		Assertions.assertNull(webSocketController.createGame(new GameConfig(0, GameType.AI, UUID.randomUUID())));
	}

	@Test
	void testRemoteGameStatus() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db334");
		String gameId = webSocketController.createGame(new GameConfig(5, GameType.AI, UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334")));
		Assertions.assertEquals(GameStatus.WAITING_BOTH, webSocketController.getGameStatus(GameManager.getInstance().findGameWithID(UUID.fromString(gameId)).getGameStatus()));
	}

	@Test
	void testRemoteJoinGame() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db334");
		String gameId = webSocketController.createGame(new GameConfig(5, GameType.VERSUS, UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334")));
		Assertions.assertEquals(GameStatus.OPEN_LOBBY, GameManager.getInstance().findGameWithID(UUID.fromString(gameId)).getGameStatus());
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db335");
		webSocketController.joinGame(new GameConfig(UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db335"), UUID.fromString(gameId)));
		Assertions.assertEquals(GameStatus.WAITING_BOTH, GameManager.getInstance().findGameWithID(UUID.fromString(gameId)).getGameStatus());
	}

	@Test
	void testRemoteTakeAction() {
		WebSocketController webSocketController = new WebSocketController(new SimpMessagingTemplate(new MessageChannel() {
			@Override
			public boolean send(Message<?> message, long timeout) {
				return false;
			}
		}));
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db334");
		String gameId = webSocketController.createGame(new GameConfig(5, GameType.VERSUS, UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334")));
		webSocketController.registerPlayer("46f39dbc-b122-40b0-bcf8-6c143f4db335");
		webSocketController.joinGame(new GameConfig(UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db335"), UUID.fromString(gameId)));
		webSocketController.takeAction(new GameConfig(UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db334"), TribeAction.COOPERATE, 0));
		Assertions.assertEquals(GameStatus.WAITING_P2, GameManager.getInstance().findGameWithID(UUID.fromString(gameId)).getGameStatus());
		webSocketController.takeAction(new GameConfig(UUID.fromString("46f39dbc-b122-40b0-bcf8-6c143f4db335"), TribeAction.COOPERATE, 0));
	}


	@Test
	void testStrategyRandom() {
		gameManager.changeRandom(mockedRandom);
		when(mockedRandom.nextInt(strategiesNumber)).thenReturn(8);
		Game game = gameManager.startNewGame(5, GameType.AI, tribeGenerator());
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.COOPERATE, 0);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousTurnAction(1));
		when(mockedRandom.nextInt(2)).thenReturn(1);
		game.playTurn(TribeAction.BETRAY, 0);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousTurnAction(1));
	}

}
