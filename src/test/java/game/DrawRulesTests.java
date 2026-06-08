package game;

import org.junit.jupiter.api.Test;

import board.Board;
import board.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DrawRulesTests {

	@Test
	void blackKnightVersusWhiteKingInsufficientMaterialDraw() {
		Game game = new Game();
		game.startNewGame();
		Board b = game.getBoard();
		clearBoard(b);
		placePiece(b, 0, 0, new Piece("KING", "BLACK"));
		placePiece(b, 1, 1, new Piece("KNIGHT", "BLACK"));
		placePiece(b, 7, 7, new Piece("KING", "WHITE"));
		game.switchTurn();
		assertTrue(game.makeMove(1, 1, 2, 3));
		assertTrue(game.isDraw());
		assertEquals("INSUFFICIENT_MATERIAL", game.getDrawReason());
	}

	@Test
	void blackKingQuietMoveAt100HalfmovesFiftyMoveDraw() throws Exception {
		Game game = new Game();
		game.startNewGame();
		Board b = game.getBoard();
		clearBoard(b);
		placePiece(b, 7, 7, new Piece("KING", "WHITE"));
		placePiece(b, 7, 6, new Piece("ROOK", "WHITE"));
		placePiece(b, 0, 0, new Piece("KING", "BLACK"));
		game.switchTurn();
		setHalfmoveClock(game, 99);
		assertTrue(game.makeMove(0, 0, 1, 0));
		assertTrue(game.isGameOver());
		assertEquals("FIFTY_MOVE", game.getDrawReason());
	}

	@Test
	void moveAfterGameOverStillRejected() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);
		placePiece(board, 0, 0, new Piece("KING", "BLACK"));
		placePiece(board, 2, 1, new Piece("KING", "WHITE"));
		placePiece(board, 2, 3, new Piece("QUEEN", "WHITE"));
		assertTrue(game.makeMove(2, 3, 1, 2));
		assertTrue(game.isGameOver());
		assertFalse(game.makeMove(0, 0, 0, 1));
		assertTrue(game.isGameOver());
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
}
