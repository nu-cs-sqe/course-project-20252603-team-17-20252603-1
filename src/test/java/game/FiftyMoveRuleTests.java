package game;

import org.junit.jupiter.api.Test;

import board.Board;
import board.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FiftyMoveRuleTests {

	@Test
	void startNewGameResetsHalfmoveClockToZero() {
		Game game = new Game();
		game.startNewGame();
		assertEquals(0, game.getHalfmoveClock());
	}

	@Test
	void quietKnightMovesIncrementHalfmoveClock() {
		Game game = new Game();
		game.startNewGame();
		Board b = game.getBoard();
		clearBoard(b);
		placePiece(b, 7, 7, new Piece("KING", "WHITE"));
		placePiece(b, 6, 5, new Piece("KNIGHT", "WHITE"));
		placePiece(b, 0, 0, new Piece("KING", "BLACK"));
		placePiece(b, 1, 1, new Piece("KNIGHT", "BLACK"));
		assertTrue(game.makeMove(6, 5, 5, 3));
		assertEquals(1, game.getHalfmoveClock());
		assertTrue(game.makeMove(1, 1, 2, 3));
		assertEquals(2, game.getHalfmoveClock());
	}

	private void clearBoard(Board board) {
		try {
			java.lang.reflect.Field stateField = Board.class.getDeclaredField("state");
			stateField.setAccessible(true);
			stateField.set(board, new Piece[8][8]);
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private void placePiece(Board board, int row, int col, Piece piece) {
		try {
			java.lang.reflect.Field stateField = Board.class.getDeclaredField("state");
			stateField.setAccessible(true);
			Piece[][] state = (Piece[][]) stateField.get(board);
			state[row][col] = piece;
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(e);
		}
	}

	private static void setHalfmoveClock(Game game, int value) throws Exception {
		java.lang.reflect.Field f = Game.class.getDeclaredField("halfmoveClock");
		f.setAccessible(true);
		f.setInt(game, value);
	}

	@Test
	void pawnMoveResetsHalfmoveClock() throws Exception {
		Game game = new Game();
		game.startNewGame();
		Board b = game.getBoard();
		clearBoard(b);
		placePiece(b, 7, 7, new Piece("KING", "WHITE"));
		placePiece(b, 6, 4, new Piece("PAWN", "WHITE"));
		placePiece(b, 0, 0, new Piece("KING", "BLACK"));
		setHalfmoveClock(game, 5);
		assertTrue(game.makeMove(6, 4, 5, 4));
		assertEquals(0, game.getHalfmoveClock());
	}
}
