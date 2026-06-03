package game;

import board.Board;
import board.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnPassantTests {

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
    void whiteEnPassantCapturesBlackPawnImmediatelyAfterTwoSquareMove() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 3, new Piece("PAWN", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(1, 3, 3, 3));
        assertTrue(game.makeMove(3, 4, 2, 3));

        assertNull(board.getPieceAt(3, 4));
        assertNull(board.getPieceAt(3, 3));

        Piece whitePawn = board.getPieceAt(2, 3);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
    }

    @Test
    void blackEnPassantCapturesWhitePawnImmediatelyAfterTwoSquareMove() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 6, 3, new Piece("PAWN", "WHITE"));
        placePiece(board, 4, 4, new Piece("PAWN", "BLACK"));

        assertTrue(game.makeMove(6, 3, 4, 3));
        assertTrue(game.makeMove(4, 4, 5, 3));

        assertNull(board.getPieceAt(4, 4));
        assertNull(board.getPieceAt(4, 3));

        Piece blackPawn = board.getPieceAt(5, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

}
