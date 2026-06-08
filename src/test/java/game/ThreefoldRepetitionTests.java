package game;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreefoldRepetitionTests {

	@Test
	void repetitionHistoryClearedOnStartNewGame() {
		Game game = new Game();
		game.startNewGame();
		assertTrue(game.makeMove(6, 0, 5, 0));
		assertEquals(1, game.getRepetitionHistorySize());
		game.startNewGame();
		assertEquals(0, game.getRepetitionHistorySize());
	}

	@Test
	void threefoldRepetitionEndsGameAsDraw() throws Exception {
		Game game = new Game();
		game.startNewGame();
		invokeAppendRepetition(game, "WHITE");
		invokeAppendRepetition(game, "WHITE");
		assertFalse(game.isGameOver());
		invokeAppendRepetition(game, "WHITE");
		assertTrue(game.isGameOver());
		assertTrue(game.isDraw());
		assertNull(game.getWinnerColor());
		assertEquals(
				"THREEFOLD_REPETITION", game.getDrawReason());
	}

	private static void invokeAppendRepetition(Game game, String sideToMoveNext) throws Exception {
		Method m = Game.class.getDeclaredMethod(
				"appendRepetitionAndCheckThreefold", String.class);
		m.setAccessible(true);
		m.invoke(game, sideToMoveNext);
	}
}
