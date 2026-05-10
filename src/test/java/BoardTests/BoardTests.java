package BoardTests;

import Board.Board;
import Board.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {
    @Test
    void initializeBoardCreatesStandardChessStartingPosition() {
        Board board = new Board();

        board.initializeBoard();

        String[] backRow = {
                "ROOK", "KNIGHT", "BISHOP", "QUEEN",
                "KING", "BISHOP", "KNIGHT", "ROOK"
        };

        int pieceCount = 0;

        // go column by column
        for (int col = 0; col < 8; col++) {
            // Black back row
            Piece blackBackPiece = board.getPieceAt(0, col);
            assertNotNull(blackBackPiece);
            assertEquals(backRow[col], blackBackPiece.getType());
            assertEquals("BLACK", blackBackPiece.getColor());

            // Black pawns
            Piece blackPawn = board.getPieceAt(1, col);
            assertNotNull(blackPawn);
            assertEquals("PAWN", blackPawn.getType());
            assertEquals("BLACK", blackPawn.getColor());

            // Empty middle rows
            assertNull(board.getPieceAt(2, col));
            assertNull(board.getPieceAt(3, col));
            assertNull(board.getPieceAt(4, col));
            assertNull(board.getPieceAt(5, col));

            // White pawns
            Piece whitePawn = board.getPieceAt(6, col);
            assertNotNull(whitePawn);
            assertEquals("PAWN", whitePawn.getType());
            assertEquals("WHITE", whitePawn.getColor());

            // White back row
            Piece whiteBackPiece = board.getPieceAt(7, col);
            assertNotNull(whiteBackPiece);
            assertEquals(backRow[col], whiteBackPiece.getType());
            assertEquals("WHITE", whiteBackPiece.getColor());
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPieceAt(row, col) != null) {
                    pieceCount++;
                }
            }
        }

        assertEquals(32, pieceCount);
    }

    @Test
    void initializeBoardResetsBoardToStandardStartingPosition() {
        Board board = new Board();

        board.initializeBoard();

        // Mess up the board manually (ONLY TEMPORARY, ONCE MOVEPIECE DONE, RE-PRIVATIZE STATE)
        board.state[0][0] = null;
        board.state[3][3] = new Piece("QUEEN", "WHITE");
        board.state[7][4] = new Piece("PAWN", "BLACK");

        // Re-initialize
        board.initializeBoard();

        // Verify the messed-up squares were reset
        Piece blackRook = board.getPieceAt(0, 0);
        assertNotNull(blackRook);
        assertEquals("ROOK", blackRook.getType());
        assertEquals("BLACK", blackRook.getColor());

        assertNull(board.getPieceAt(3, 3));

        Piece whiteKing = board.getPieceAt(7, 4);
        assertNotNull(whiteKing);
        assertEquals("KING", whiteKing.getType());
        assertEquals("WHITE", whiteKing.getColor());
    }

    //getPieceAt tests

    @Test
    void getPieceAt00ReturnsBlackRook() {
        Board board = new Board();
        board.initializeBoard();

        Piece expected = new Piece("ROOK", "BLACK");
        Piece actual = board.getPieceAt(0, 0);

        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    void getPieceAt04ReturnsBlackKing() {
        Board board = new Board();
        board.initializeBoard();

        Piece expected = new Piece("KING", "BLACK");
        Piece actual = board.getPieceAt(0, 4);

        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    void getPieceAt74ReturnsWhiteKing() {
        Board board = new Board();
        board.initializeBoard();

        Piece expected = new Piece("KING", "WHITE");
        Piece actual = board.getPieceAt(7, 4);

        assertEquals(expected.getColor(), actual.getColor());
        assertEquals(expected.getType(), actual.getType());
    }

    @Test
    void getPieceAt33ReturnsNull() {
        Board board = new Board();
        board.initializeBoard();

        Piece actual = board.getPieceAt(3, 3);

        assertNull(actual);
    }

    @Test
    void getPieceAtNegOneZeroErrors() {
        Board board = new Board();
        board.initializeBoard();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPieceAt(-1, 0);
        });
    }

    @Test
    void getPieceAtEightZeroErrors() {
        Board board = new Board();
        board.initializeBoard();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPieceAt(8, 0);
        });
    }

    @Test
    void getPieceAtZeroNegOneErrors() {
        Board board = new Board();
        board.initializeBoard();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPieceAt(0, -1);
        });
    }

    @Test
    void getPieceAtZeroEightErrors() {
        Board board = new Board();
        board.initializeBoard();

        assertThrows(IllegalArgumentException.class, () -> {
            board.getPieceAt(0, 8);
        });
    }


    // isWithinBounds tests

    @Test
    void isWithinBounds00ReturnsTrue() {
        Board board = new Board();
        board.initializeBoard();

        assertTrue(board.isWithinBounds(0,0));
    }

    @Test
    void isWithinBounds77ReturnsTrue() {
        Board board = new Board();
        board.initializeBoard();

        assertTrue(board.isWithinBounds(7,7));
    }

    @Test
    void isWithinBoundsNegOneZeroReturnsFalse() {
        Board board = new Board();
        board.initializeBoard();

        assertFalse(board.isWithinBounds(-1,0));
    }

    @Test
    void isWithinBoundsEightZeroReturnsFalse() {
        Board board = new Board();
        board.initializeBoard();

        assertFalse(board.isWithinBounds(8,0));
    }

    @Test
    void isWithinBoundsZeroNegOneReturnsFalse() {
        Board board = new Board();
        board.initializeBoard();

        assertFalse(board.isWithinBounds(0,-1));
    }







}
