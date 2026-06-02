package game;

import board.Board;
import board.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Castling behavior (BVA BOARD-MOVE-034+). These tests drive implementation of
 * kingside/queenside castling and illegal-castle rejection through {@link Game#makeMove}.
 */
public class CastlingTests {

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

    @Test
    void whiteKingsideCastlePlacesKingAndRookCorrectly() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertTrue(game.makeMove(7, 4, 7, 6));

        assertNull(board.getPieceAt(7, 4));
        assertNull(board.getPieceAt(7, 7));
        Piece king = board.getPieceAt(7, 6);
        Piece rook = board.getPieceAt(7, 5);
        assertNotNull(king);
        assertNotNull(rook);
        assertEquals("KING", king.getType());
        assertEquals("ROOK", rook.getType());
        assertEquals("WHITE", king.getColor());
        assertEquals("WHITE", rook.getColor());
    }

    @Test
    void whiteQueensideCastlePlacesKingAndRookCorrectly() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 0, new Piece("ROOK", "WHITE"));
        placePiece(board, 0, 7, new Piece("KING", "BLACK"));

        assertTrue(game.makeMove(7, 4, 7, 2));

        assertNull(board.getPieceAt(7, 4));
        assertNull(board.getPieceAt(7, 0));
        Piece king = board.getPieceAt(7, 2);
        Piece rook = board.getPieceAt(7, 3);
        assertNotNull(king);
        assertNotNull(rook);
        assertEquals("KING", king.getType());
        assertEquals("ROOK", rook.getType());
    }

    @Test
    void whiteKingsideCastleRejectedWhenPathBlocked() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 7, 5, new Piece("BISHOP", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertFalse(game.makeMove(7, 4, 7, 6));
        assertNotNull(board.getPieceAt(7, 4));
        assertNotNull(board.getPieceAt(7, 7));
    }

    @Test
    void whiteKingsideCastleRejectedAfterKingHasMoved() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertTrue(game.makeMove(7, 4, 6, 4));
        assertTrue(game.makeMove(0, 0, 0, 1));
        assertTrue(game.makeMove(6, 4, 7, 4));
        assertTrue(game.makeMove(0, 1, 0, 0));

        assertFalse(game.makeMove(7, 4, 7, 6));
    }

    @Test
    void whiteKingsideCastleRejectedAfterKingsideRookHasMoved() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertTrue(game.makeMove(7, 7, 7, 6));
        assertTrue(game.makeMove(0, 0, 0, 1));
        assertTrue(game.makeMove(7, 6, 7, 7));
        assertTrue(game.makeMove(0, 1, 0, 0));

        assertFalse(game.makeMove(7, 4, 7, 6));
    }

    @Test
    void whiteKingsideCastleRejectedWhenKingWouldPassThroughAttackedSquare() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 4, 5, new Piece("ROOK", "BLACK"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertFalse(game.makeMove(7, 4, 7, 6));
    }

    @Test
    void whiteKingsideCastleRejectedWhenKingWouldLandInCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 0, 6, new Piece("ROOK", "BLACK"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertFalse(game.makeMove(7, 4, 7, 6));
    }

    @Test
    void whiteKingsideCastleRejectedWhileKingIsInCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 7, 7, new Piece("ROOK", "WHITE"));
        placePiece(board, 1, 4, new Piece("QUEEN", "BLACK"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
        assertFalse(game.makeMove(7, 4, 7, 6));
    }

    @Test
    void blackKingsideCastlePlacesKingAndRookCorrectly() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 0, 7, new Piece("ROOK", "BLACK"));
        placePiece(board, 7, 0, new Piece("KING", "WHITE"));

        assertTrue(game.makeMove(7, 0, 6, 0));
        assertTrue(game.makeMove(0, 4, 0, 6));

        assertNull(board.getPieceAt(0, 4));
        assertNull(board.getPieceAt(0, 7));
        Piece king = board.getPieceAt(0, 6);
        Piece rook = board.getPieceAt(0, 5);
        assertNotNull(king);
        assertNotNull(rook);
        assertEquals("KING", king.getType());
        assertEquals("ROOK", rook.getType());
        assertEquals("BLACK", king.getColor());
    }

    @Test
    void blackQueensideCastlePlacesKingAndRookCorrectly() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 0, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 7, 7, new Piece("KING", "WHITE"));

        assertTrue(game.makeMove(7, 7, 6, 7));
        assertTrue(game.makeMove(0, 4, 0, 2));

        assertNull(board.getPieceAt(0, 4));
        assertNull(board.getPieceAt(0, 0));
        assertEquals("KING", board.getPieceAt(0, 2).getType());
        assertEquals("ROOK", board.getPieceAt(0, 3).getType());
    }
}
