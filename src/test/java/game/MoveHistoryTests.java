package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveHistoryTests {

	@Test
	void newGameHasEmptyMoveHistory() {
		Game game = new Game();
		game.startNewGame();
		assertTrue(game.getMoveHistory().isEmpty());
	}

	@Test
	void successfulMoveAppendsOneEntry() {
		Game game = new Game();
		game.startNewGame();
		assertTrue(game.makeMove(6, 0, 5, 0));
		assertEquals(1, game.getMoveHistory().size());
		Move m = game.getMoveHistory().get(0);
		assertEquals(6, m.getStartRow());
		assertEquals(0, m.getStartCol());
		assertEquals(5, m.getEndRow());
		assertEquals(0, m.getEndCol());
		assertEquals("PAWN", m.getPieceType());
		assertEquals("WHITE", m.getColor());
		assertFalse(m.isCapture());
		assertFalse(m.isCastling());
		assertFalse(m.isEnPassant());
	}

	@Test
	void twoPliesAppendsTwoEntries() {
		Game game = new Game();
		game.startNewGame();
		assertTrue(game.makeMove(6, 0, 5, 0));
		assertTrue(game.makeMove(1, 0, 2, 0));
		assertEquals(2, game.getMoveHistory().size());
		assertEquals("WHITE", game.getMoveHistory().get(0).getColor());
		assertEquals("BLACK", game.getMoveHistory().get(1).getColor());
	}

	@Test
	void getMoveHistoryIsUnmodifiable() {
		Game game = new Game();
		game.startNewGame();
		game.makeMove(6, 0, 5, 0);
		assertThrows(UnsupportedOperationException.class, () ->
				game.getMoveHistory().clear());
	}
}
