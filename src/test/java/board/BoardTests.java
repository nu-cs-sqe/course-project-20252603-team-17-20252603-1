package board;

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

        assertTrue(board.movePiece(6, 0, 4, 0));
        assertNull(board.getPieceAt(6, 0));

        Piece movedPawn = board.getPieceAt(4, 0);
        assertNotNull(movedPawn);
        assertEquals("PAWN", movedPawn.getType());
        assertEquals("WHITE", movedPawn.getColor());

        board.initializeBoard();

        Piece blackRook = board.getPieceAt(0, 0);
        assertNotNull(blackRook);
        assertEquals("ROOK", blackRook.getType());
        assertEquals("BLACK", blackRook.getColor());

        assertNull(board.getPieceAt(3, 3));

        Piece restoredWhitePawn = board.getPieceAt(6, 0);
        assertNotNull(restoredWhitePawn);
        assertEquals("PAWN", restoredWhitePawn.getType());
        assertEquals("WHITE", restoredWhitePawn.getColor());

        assertNull(board.getPieceAt(4, 0));

        Piece whiteKing = board.getPieceAt(7, 4);
        assertNotNull(whiteKing);
        assertEquals("KING", whiteKing.getType());
        assertEquals("WHITE", whiteKing.getColor());
    }



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

    @Test
    void isWithinBoundsZeroEightReturnsFalse() {
        Board board = new Board();
        board.initializeBoard();

        assertFalse(board.isWithinBounds(0,8));
    }

    @Test
    void movePieceWhitePawnOneSquareForwardMovesPiece() {
        Board board = new Board();
        board.initializeBoard();
        assertTrue(board.movePiece(6, 0, 5, 0));
        assertNull(board.getPieceAt(6, 0));
        Piece moved = board.getPieceAt(5, 0);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }
    @Test
    void movePieceWhitePawnTwoSquaresForwardFromStartRank() {
        Board board = new Board();
        board.initializeBoard();
        assertTrue(board.movePiece(6, 0, 4, 0));
        assertNull(board.getPieceAt(6, 0));
        assertNull(board.getPieceAt(5, 0));
        Piece moved = board.getPieceAt(4, 0);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceWhitePawnThreeSquaresForwardIsRejected() {
        Board board = new Board();
        board.initializeBoard();
        assertFalse(board.movePiece(6, 0, 3, 0));
        Piece stillThere = board.getPieceAt(6, 0);
        assertNotNull(stillThere);
        assertEquals("PAWN", stillThere.getType());
        assertEquals("WHITE", stillThere.getColor());
        assertNull(board.getPieceAt(3, 0));
    }
    @Test
    void movePieceFromEmptySquareIsRejected() {
        Board board = new Board();
        board.initializeBoard();
        assertFalse(board.movePiece(3, 3, 4, 3));
        assertNull(board.getPieceAt(4, 3));
    }
    @Test
    void movePieceWithStartOutOfBoundsIsRejected() {
        Board board = new Board();
        board.initializeBoard();
        assertFalse(board.movePiece(-1, 0, 0, 0));
    }

    @Test
    void movePieceWithEndOutOfBoundsIsRejected() {
        Board board = new Board();
        board.initializeBoard();
        assertFalse(board.movePiece(0, 0, 8, 0));
        Piece unchanged = board.getPieceAt(0, 0);
        assertNotNull(unchanged);
        assertEquals("ROOK", unchanged.getType());
        assertEquals("BLACK", unchanged.getColor());
    }


    @Test
    void whitePieceReturnsWhite() {
        Piece piece = new Piece("PAWN", "WHITE");

        assertEquals("WHITE", piece.getColor());
    }

    @Test
    void blackPieceReturnsBlack() {
        Piece piece = new Piece("PAWN", "BLACK");

        assertEquals("BLACK", piece.getColor());
    }

    @Test
    void kingPieceReturnsKing() {
        Piece piece = new Piece("KING", "WHITE");

        assertEquals("KING", piece.getType());
    }

    @Test
    void queenPieceReturnsQueen() {
        Piece piece = new Piece("QUEEN", "WHITE");

        assertEquals("QUEEN", piece.getType());
    }

    @Test
    void rookPieceReturnsRook() {
        Piece piece = new Piece("ROOK", "WHITE");

        assertEquals("ROOK", piece.getType());
    }

    @Test
    void bishopPieceReturnsBishop() {
        Piece piece = new Piece("BISHOP", "WHITE");

        assertEquals("BISHOP", piece.getType());
    }

    @Test
    void knightPieceReturnsKnight() {
        Piece piece = new Piece("KNIGHT", "WHITE");

        assertEquals("KNIGHT", piece.getType());
    }

    @Test
    void pawnPieceReturnsPawn() {
        Piece piece = new Piece("PAWN", "WHITE");

        assertEquals("PAWN", piece.getType());
    }

    @Test
    void movePieceBlackPawnOneSquareForwardMovesPiece() {
        Board board = new Board();
        board.initializeBoard();

        assertTrue(board.movePiece(1, 0, 2, 0));
        assertNull(board.getPieceAt(1, 0));

        Piece moved = board.getPieceAt(2, 0);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("BLACK", moved.getColor());
    }


    @Test
    void movePieceBlackPawnTwoSquaresForwardFromStartRank() {
        Board board = new Board();
        board.initializeBoard();

        assertTrue(board.movePiece(1, 0, 3, 0));
        assertNull(board.getPieceAt(1, 0));
        assertNull(board.getPieceAt(2, 0));

        Piece moved = board.getPieceAt(3, 0);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("BLACK", moved.getColor());
    }


    @Test
    void movePiecePawnCannotMoveForwardIntoOccupiedSquare() {
        Board board = new Board();
        board.initializeBoard();

        assertFalse(board.movePiece(6, 0, 7, 0));

        Piece pawn = board.getPieceAt(6, 0);
        assertNotNull(pawn);
        assertEquals("PAWN", pawn.getType());
        assertEquals("WHITE", pawn.getColor());

        Piece rook = board.getPieceAt(7, 0);
        assertNotNull(rook);
        assertEquals("ROOK", rook.getType());
        assertEquals("WHITE", rook.getColor());
    }

    @Test
    void movePiecePawnCanCaptureDiagonally() {
        Board board = new Board();
        board.initializeBoard();

        assertTrue(board.movePiece(1, 1, 3, 1));
        assertTrue(board.movePiece(3, 1, 4, 1));
        assertTrue(board.movePiece(4, 1, 5, 1));

        assertTrue(board.movePiece(6, 0, 5, 1));
        assertNull(board.getPieceAt(6, 0));

        Piece moved = board.getPieceAt(5, 1);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("WHITE", moved.getColor());
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
    void movePiecePawnCannotCaptureStraightForward() {
        Board board = new Board();
        board.initializeBoard();
        placePiece(board, 5, 0, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePiece(6, 0, 5, 0));

        Piece whitePawn = board.getPieceAt(6, 0);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(5, 0);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());
    }

    @Test
    void movePieceBlackPawnCanCaptureDiagonally() {
        Board board = new Board();
        board.initializeBoard();
        placePiece(board, 2, 1, new Piece("PAWN", "WHITE"));

        assertTrue(board.movePiece(1, 0, 2, 1));
        assertNull(board.getPieceAt(1, 0));

        Piece moved = board.getPieceAt(2, 1);
        assertNotNull(moved);
        assertEquals("PAWN", moved.getType());
        assertEquals("BLACK", moved.getColor());
    }

    @Test
    void movePieceBlackPawnCannotCaptureStraightForward() {
        Board board = new Board();
        board.initializeBoard();
        placePiece(board, 2, 0, new Piece("PAWN", "WHITE"));

        assertFalse(board.movePiece(1, 0, 2, 0));

        Piece blackPawn = board.getPieceAt(1, 0);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        Piece whitePawn = board.getPieceAt(2, 0);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());
    }

    @Test
    void movePiecePawnCannotMoveTwoSquaresIntoOccupiedDestination() {
        Board board = new Board();
        board.initializeBoard();
        placePiece(board, 4, 0, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePiece(6, 0, 4, 0));

        Piece whitePawn = board.getPieceAt(6, 0);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(4, 0);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());
    }

    @Test
    void movePieceRookMovesHorizontally() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));

        assertTrue(board.movePiece(4, 4, 4, 7));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(4, 7);
        assertNotNull(moved);
        assertEquals("ROOK", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceRookMovesVertically() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));

        assertTrue(board.movePiece(4, 4, 1, 4));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(1, 4);
        assertNotNull(moved);
        assertEquals("ROOK", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceRookBlockedByPieceIsRejected() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));
        placePiece(board, 4, 6, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePiece(4, 4, 4, 7));

        Piece rook = board.getPieceAt(4, 4);
        assertNotNull(rook);
        assertEquals("ROOK", rook.getType());
        assertEquals("WHITE", rook.getColor());

        Piece blocker = board.getPieceAt(4, 6);
        assertNotNull(blocker);
        assertEquals("PAWN", blocker.getType());
        assertEquals("BLACK", blocker.getColor());

        assertNull(board.getPieceAt(4, 7));
    }

    @Test
    void movePieceBishopMovesDiagonally() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("BISHOP", "WHITE"));

        assertTrue(board.movePiece(4, 4, 1, 1));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(1, 1);
        assertNotNull(moved);
        assertEquals("BISHOP", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceKnightMovesInLShape() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("KNIGHT", "WHITE"));

        assertTrue(board.movePiece(4, 4, 2, 5));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(2, 5);
        assertNotNull(moved);
        assertEquals("KNIGHT", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }



}
