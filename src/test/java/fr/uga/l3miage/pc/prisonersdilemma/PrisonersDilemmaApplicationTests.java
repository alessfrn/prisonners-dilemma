package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.classes.game.Tribe;
import fr.uga.l3miage.pc.classes.strategies.GiveAndTake;
import fr.uga.l3miage.pc.enums.GameType;
import fr.uga.l3miage.pc.enums.Strategies;
import fr.uga.l3miage.pc.enums.TribeAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;
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
}
