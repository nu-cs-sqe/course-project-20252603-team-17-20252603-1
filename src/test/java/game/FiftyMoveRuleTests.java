package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FiftyMoveRuleTests {

	@Test
	void startNewGameResetsHalfmoveClockToZero() {
		Game game = new Game();
		game.startNewGame();
		assertEquals(0, game.getHalfmoveClock());
	}
}
