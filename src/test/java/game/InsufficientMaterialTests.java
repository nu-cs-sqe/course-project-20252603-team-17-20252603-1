package game;

import org.junit.jupiter.api.Test;

import board.Board;
import board.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Draw by insufficient material (FIDE-style dead positions).
 */
class InsufficientMaterialTests {

	@Test
	void kVersusKAfterWhiteKingMoveEndsDrawWithInsufficientMaterialReason() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);
		placePiece(board, 7, 4, new Piece("KING", "WHITE"));
		placePiece(board, 0, 4, new Piece("KING", "BLACK"));

		assertTrue(game.makeMove(7, 4, 6, 4));
		assertTrue(game.isGameOver());
		assertTrue(game.isDraw());
		assertNull(game.getWinnerColor());
		assertEquals("INSUFFICIENT_MATERIAL", game.getDrawReason());
	}

	@Test
	void kingKnightVersusKingAfterKnightMoveEndsInsufficientDraw() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);
		placePiece(board, 7, 7, new Piece("KING", "WHITE"));
		placePiece(board, 6, 5, new Piece("KNIGHT", "WHITE"));
		placePiece(board, 0, 0, new Piece("KING", "BLACK"));

		assertTrue(game.makeMove(6, 5, 5, 3));
		assertTrue(game.isGameOver());
		assertTrue(game.isDraw());
		assertEquals("INSUFFICIENT_MATERIAL", game.getDrawReason());
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
}
