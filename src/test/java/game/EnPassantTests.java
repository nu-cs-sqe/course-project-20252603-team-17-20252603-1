package game;

import board.Board;
import board.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void enPassantRejectedIfNotImmediateNextMove() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 3, new Piece("PAWN", "BLACK"));
        placePiece(board, 6, 7, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 7, new Piece("PAWN", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(1, 3, 3, 3));
        assertTrue(game.makeMove(6, 7, 5, 7));
        assertTrue(game.makeMove(1, 7, 2, 7));

        assertFalse(game.makeMove(3, 4, 2, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void whiteEnPassantRejectedFromWrongRank() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 4, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 2, new Piece("PAWN", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(1, 2, 3, 2));
        assertFalse(game.makeMove(4, 4, 3, 3));

        Piece whitePawn = board.getPieceAt(4, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 2);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(3, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void blackEnPassantRejectedFromWrongRank() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 6, 2, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 4, new Piece("PAWN", "BLACK"));

        assertTrue(game.makeMove(6, 2, 4, 2));
        assertFalse(game.makeMove(3, 4, 4, 3));

        Piece blackPawn = board.getPieceAt(3, 4);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        Piece whitePawn = board.getPieceAt(4, 2);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        assertNull(board.getPieceAt(4, 3));
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
    }

    @Test
    void enPassantRejectedWhenPreviousMoveWasNotTwoSquarePawnMove() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 2, 3, new Piece("PAWN", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(2, 3, 3, 3));
        assertFalse(game.makeMove(3, 4, 2, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void enPassantRejectedWhenTwoSquarePawnMoveIsNotAdjacent() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 1, new Piece("PAWN", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(1, 1, 3, 1));
        assertFalse(game.makeMove(3, 4, 2, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 1);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void enPassantRejectedWhenPawnMovesWrongDirection() {
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
        assertFalse(game.makeMove(3, 4, 4, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(4, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void enPassantRejectedWhenItLeavesOwnKingInCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 3, 7, new Piece("KING", "WHITE"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 1, 3, new Piece("PAWN", "BLACK"));
        placePiece(board, 3, 0, new Piece("ROOK", "BLACK"));

        game.switchTurn();

        assertTrue(game.makeMove(1, 3, 3, 3));
        assertFalse(game.makeMove(3, 4, 2, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }







}
