package game;

import org.junit.jupiter.api.Test;

import board.Board;
import board.Piece;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Draw by insufficient material (FIDE-style dead positions).
 */
class InsufficientMaterialTests {


	private static final int BOARD_SIZE = 8;

	private static final String WHITE = "WHITE";
	private static final String BLACK = "BLACK";

	private static final String KING = "KING";
	private static final String BISHOP = "BISHOP";

	private static final int BLACK_BACK_ROW = 0;
	private static final int WHITE_BACK_ROW = 7;

	private static final int C_COL = 2;
	private static final int E_COL = 4;
	private static final int F_COL = 5;

	private static final int WHITE_KING_START_ROW = 7;
	private static final int WHITE_KING_END_ROW = 6;

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

	@Test
	void kingVersusKingWithQueenKeepsGameGoingAfterNonCaptureKingMove() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);
		placePiece(board, 7, 7, new Piece("KING", "WHITE"));
		placePiece(board, 0, 0, new Piece("KING", "BLACK"));
		placePiece(board, 4, 4, new Piece("QUEEN", "WHITE"));

		assertTrue(game.makeMove(7, 7, 6, 7));
		assertFalse(game.isGameOver());
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

	@Test
	void sameColorBishopsOnlyEndsAsInsufficientMaterialDraw() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);

		placePiece(board, WHITE_BACK_ROW, E_COL, new Piece(KING, WHITE));
		placePiece(board, BLACK_BACK_ROW, E_COL, new Piece(KING, BLACK));

		placePiece(board, WHITE_BACK_ROW, C_COL, new Piece(BISHOP, WHITE));
		placePiece(board, BLACK_BACK_ROW, F_COL, new Piece(BISHOP, BLACK));

		assertTrue(game.makeMove(WHITE_KING_START_ROW, E_COL, WHITE_KING_END_ROW, E_COL));
		assertTrue(game.isGameOver());
		assertTrue(game.isDraw());
		assertEquals("INSUFFICIENT_MATERIAL", game.getDrawReason());
	}

	@Test
	void oppositeColorBishopsOnlyDoesNotEndAsInsufficientMaterialDraw() {
		Game game = new Game();
		game.startNewGame();
		Board board = game.getBoard();
		clearBoard(board);

		placePiece(board, WHITE_BACK_ROW, E_COL, new Piece(KING, WHITE));
		placePiece(board, BLACK_BACK_ROW, E_COL, new Piece(KING, BLACK));

		placePiece(board, WHITE_BACK_ROW, C_COL, new Piece(BISHOP, WHITE));
		placePiece(board, BLACK_BACK_ROW, C_COL, new Piece(BISHOP, BLACK));

		assertTrue(game.makeMove(WHITE_KING_START_ROW, E_COL, WHITE_KING_END_ROW, E_COL));
		assertFalse(game.isGameOver());
		assertFalse(game.isDraw());
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
