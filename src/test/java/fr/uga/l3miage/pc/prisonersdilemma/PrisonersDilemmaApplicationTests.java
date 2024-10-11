package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;
import static org.mockito.Mockito.*;

class PrisonersDilemmaApplicationTests {
	private final Random mockedRandom = mock(SecureRandom.class);
	private final GameManager gameManager = GameManager.getInstance();

	@Test
	void testGameManagerInstance() {
		Assertions.assertNotNull(gameManager);
		Assertions.assertNotNull(gameManager.getStrategyFactory());
		Assertions.assertNotNull(gameManager.getRandom());
	}

	@Test
	void testGameCreation() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> { gameManager.startNewGameRandomStrategy(0); });
		Assertions.assertThrows(IllegalArgumentException.class, () -> { gameManager.startNewGameSetStrategy(0, ""); });
		gameManager.startNewGameRandomStrategy(1);
		Assertions.assertNotNull(gameManager.getActiveGame());
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
	void testPavlovRandom() {
		gameManager.changeRandom(mockedRandom);
		gameManager.startNewGameSetStrategy(5, "PavlovRandom");
		Game game = gameManager.getActiveGame();
		when(mockedRandom.nextInt(2)).thenReturn(0);
		game.playTurn(TribeAction.COOPERATE);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousSystemTurnAction());
		when(mockedRandom.nextInt(5)).thenReturn(4);
		game.playTurn(TribeAction.BETRAY);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousSystemTurnAction());
		game.playTurn(TribeAction.COOPERATE);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousSystemTurnAction());
		game.playTurn(TribeAction.BETRAY);
		Assertions.assertEquals(TribeAction.BETRAY, game.getPreviousSystemTurnAction());
		game.playTurn(TribeAction.COOPERATE);
		Assertions.assertEquals(TribeAction.COOPERATE, game.getPreviousSystemTurnAction());
	}

}
