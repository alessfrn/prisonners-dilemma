package fr.uga.l3miage.pc.prisonersdilemma;

import fr.uga.l3miage.pc.classes.game.Game;
import fr.uga.l3miage.pc.classes.game.GameManager;
import fr.uga.l3miage.pc.enums.TribeAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.mockito.Mockito.*;

class PrisonersDilemmaApplicationTests {
	private final Random mockedRandom = mock(Random.class);

	@Test
	void contextLoads() {
	}

	@Test
	void testGameManagerInstance() {
		Assertions.assertNotNull(GameManager.getInstance());
		Assertions.assertNotNull(GameManager.getInstance().getStrategyFactory());
		Assertions.assertNotNull(GameManager.getInstance().getRandom());
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
		GameManager.getInstance().changeRandom(mockedRandom);
		when(mockedRandom.nextInt(2)).thenReturn(0);
		Assertions.assertEquals(TribeAction.COOPERATE, TribeAction.returnRandomAction());
		when(mockedRandom.nextInt(2)).thenReturn(1);
		Assertions.assertEquals(TribeAction.BETRAY, TribeAction.returnRandomAction());
	}

	@Test
	void testPavlovRandom() {
		GameManager.getInstance().changeRandom(mockedRandom);
		GameManager.getInstance().startNewGameSetStrategy(5, "PavlovRandom");
		Game game = GameManager.getInstance().getActiveGame();
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
