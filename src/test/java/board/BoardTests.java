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

    @Test
    void movePieceQueenMovesStraight() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("QUEEN", "WHITE"));

        assertTrue(board.movePiece(4, 4, 4, 1));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(4, 1);
        assertNotNull(moved);
        assertEquals("QUEEN", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceQueenMovesDiagonally() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("QUEEN", "WHITE"));

        assertTrue(board.movePiece(4, 4, 1, 1));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(1, 1);
        assertNotNull(moved);
        assertEquals("QUEEN", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceKingMovesOneSquare() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("KING", "WHITE"));

        assertTrue(board.movePiece(4, 4, 5, 5));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(5, 5);
        assertNotNull(moved);
        assertEquals("KING", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceCannotCaptureSameColorPiece() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));
        placePiece(board, 4, 7, new Piece("PAWN", "WHITE"));

        assertFalse(board.movePiece(4, 4, 4, 7));

        Piece rook = board.getPieceAt(4, 4);
        assertNotNull(rook);
        assertEquals("ROOK", rook.getType());
        assertEquals("WHITE", rook.getColor());

        Piece pawn = board.getPieceAt(4, 7);
        assertNotNull(pawn);
        assertEquals("PAWN", pawn.getType());
        assertEquals("WHITE", pawn.getColor());
    }

    @Test
    void movePieceCanCaptureOpponentPiece() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));
        placePiece(board, 4, 7, new Piece("PAWN", "BLACK"));

        assertTrue(board.movePiece(4, 4, 4, 7));
        assertNull(board.getPieceAt(4, 4));

        Piece moved = board.getPieceAt(4, 7);
        assertNotNull(moved);
        assertEquals("ROOK", moved.getType());
        assertEquals("WHITE", moved.getColor());
    }

    @Test
    void movePieceBishopBlockedByPieceIsRejected() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("BISHOP", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePiece(4, 4, 2, 2));

        Piece bishop = board.getPieceAt(4, 4);
        assertNotNull(bishop);
        assertEquals("BISHOP", bishop.getType());
        assertEquals("WHITE", bishop.getColor());

        Piece blocker = board.getPieceAt(3, 3);
        assertNotNull(blocker);
        assertEquals("PAWN", blocker.getType());
        assertEquals("BLACK", blocker.getColor());

        assertNull(board.getPieceAt(2, 2));
    }

    @Test
    void movePieceKingCannotMoveTwoSquares() {
        Board board = new Board();
        placePiece(board, 4, 4, new Piece("KING", "WHITE"));

        assertFalse(board.movePiece(4, 4, 6, 4));

        Piece king = board.getPieceAt(4, 4);
        assertNotNull(king);
        assertEquals("KING", king.getType());
        assertEquals("WHITE", king.getColor());

        assertNull(board.getPieceAt(6, 4));
    }

    @Test
    void whitePawnPromotesToQueenByDefaultOnLastRank() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 1, 0, new Piece("PAWN", "WHITE"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertTrue(board.movePiece(1, 0, 0, 0));
        assertNull(board.getPieceAt(1, 0));

        Piece promoted = board.getPieceAt(0, 0);
        assertNotNull(promoted);
        assertEquals("QUEEN", promoted.getType());
        assertEquals("WHITE", promoted.getColor());
    }

    @Test
    void blackPawnPromotesToQueenByDefaultOnLastRank() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 6, 7, new Piece("PAWN", "BLACK"));
        placePiece(board, 0, 0, new Piece("KING", "WHITE"));

        assertTrue(board.movePiece(6, 7, 7, 7));
        assertNull(board.getPieceAt(6, 7));

        Piece promoted = board.getPieceAt(7, 7);
        assertNotNull(promoted);
        assertEquals("QUEEN", promoted.getType());
        assertEquals("BLACK", promoted.getColor());
    }

    @Test
    void whitePawnPromotesToRookWhenSpecified() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 1, 2, new Piece("PAWN", "WHITE"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertTrue(board.movePiece(1, 2, 0, 2, "ROOK"));

        Piece promoted = board.getPieceAt(0, 2);
        assertNotNull(promoted);
        assertEquals("ROOK", promoted.getType());
        assertEquals("WHITE", promoted.getColor());
    }

    @Test
    void whitePawnPromotesToKnightWhenSpecifiedLowercase() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 1, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertTrue(board.movePiece(1, 4, 0, 4, "knight"));

        Piece promoted = board.getPieceAt(0, 4);
        assertNotNull(promoted);
        assertEquals("KNIGHT", promoted.getType());
        assertEquals("WHITE", promoted.getColor());
    }

    @Test
    void promotionRejectedWhenNotOnFinalRank() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 3, 0, new Piece("PAWN", "WHITE"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertFalse(board.movePiece(3, 0, 2, 0, "ROOK"));

        Piece pawn = board.getPieceAt(3, 0);
        assertNotNull(pawn);
        assertEquals("PAWN", pawn.getType());
        assertNull(board.getPieceAt(2, 0));
    }

    @Test
    void promotionRejectedForInvalidPieceType() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 1, 1, new Piece("PAWN", "WHITE"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertFalse(board.movePiece(1, 1, 0, 1, "PAWN"));
        assertFalse(board.movePiece(1, 1, 0, 1, "KING"));

        Piece pawn = board.getPieceAt(1, 1);
        assertNotNull(pawn);
        assertEquals("PAWN", pawn.getType());
    }

    @Test
    void whitePawnCapturePromotesToQueen() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 1, 3, new Piece("PAWN", "WHITE"));
        placePiece(board, 0, 4, new Piece("ROOK", "BLACK"));
        placePiece(board, 7, 7, new Piece("KING", "BLACK"));

        assertTrue(board.movePiece(1, 3, 0, 4));

        Piece promoted = board.getPieceAt(0, 4);
        assertNotNull(promoted);
        assertEquals("QUEEN", promoted.getType());
        assertEquals("WHITE", promoted.getColor());
    }

    @Test
    void rookMoveWithPromotionParameterRejected() {
        Board board = new Board();
        clearBoard(board);
        placePiece(board, 4, 4, new Piece("ROOK", "WHITE"));

        assertFalse(board.movePiece(4, 4, 4, 7, "QUEEN"));

        assertNotNull(board.getPieceAt(4, 4));
        assertNull(board.getPieceAt(4, 7));
    }

    @Test
    void whitePawnCanMoveEnPassantAndRemoveCapturedPawn() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertTrue(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        assertNull(board.getPieceAt(3, 4));
        assertNull(board.getPieceAt(3, 3));

        Piece whitePawn = board.getPieceAt(2, 3);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());
    }

    @Test
    void blackPawnCanMoveEnPassantAndRemoveCapturedPawn() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("PAWN", "BLACK"));
        placePiece(board, 4, 3, new Piece("PAWN", "WHITE"));

        assertTrue(board.movePieceEnPassant(4, 4, 5, 3, 4, 3));

        assertNull(board.getPieceAt(4, 4));
        assertNull(board.getPieceAt(4, 3));

        Piece blackPawn = board.getPieceAt(5, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());
    }

    @Test
    void enPassantRejectedWhenStartSquareIsEmpty() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        assertNull(board.getPieceAt(3, 4));

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
    }

    @Test
    void enPassantRejectedWhenMovingPieceIsNotPawn() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("ROOK", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        Piece whiteRook = board.getPieceAt(3, 4);
        assertNotNull(whiteRook);
        assertEquals("ROOK", whiteRook.getType());
        assertEquals("WHITE", whiteRook.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(2, 3));
    }

    @Test
    void enPassantRejectedWhenCapturedPieceIsSameColor() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "WHITE"));

        assertFalse(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        Piece movingPawn = board.getPieceAt(3, 4);
        assertNotNull(movingPawn);
        assertEquals("PAWN", movingPawn.getType());
        assertEquals("WHITE", movingPawn.getColor());

        Piece capturedSquarePawn = board.getPieceAt(3, 3);
        assertNotNull(capturedSquarePawn);
        assertEquals("PAWN", capturedSquarePawn.getType());
        assertEquals("WHITE", capturedSquarePawn.getColor());

        assertNull(board.getPieceAt(2, 3));
    }

    @Test
    void enPassantRejectedWhenCapturedPieceIsNotPawn() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 3, new Piece("ROOK", "BLACK"));

        assertFalse(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackRook = board.getPieceAt(3, 3);
        assertNotNull(blackRook);
        assertEquals("ROOK", blackRook.getType());
        assertEquals("BLACK", blackRook.getColor());

        assertNull(board.getPieceAt(2, 3));
    }

    @Test
    void enPassantRejectedWhenTargetSquareIsOccupied() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));
        placePiece(board, 2, 3, new Piece("KNIGHT", "BLACK"));

        assertFalse(board.movePieceEnPassant(3, 4, 2, 3, 3, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        Piece blackKnight = board.getPieceAt(2, 3);
        assertNotNull(blackKnight);
        assertEquals("KNIGHT", blackKnight.getType());
        assertEquals("BLACK", blackKnight.getColor());
    }

    @Test
    void enPassantRejectedWhenWhiteMovesWrongDirection() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 3, 4, new Piece("PAWN", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertFalse(board.movePieceEnPassant(3, 4, 4, 3, 3, 3));

        Piece whitePawn = board.getPieceAt(3, 4);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        Piece blackPawn = board.getPieceAt(3, 3);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        assertNull(board.getPieceAt(4, 3));
    }

    @Test
    void enPassantRejectedWhenBlackMovesWrongDirection() {
        Board board = new Board();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("PAWN", "BLACK"));
        placePiece(board, 4, 3, new Piece("PAWN", "WHITE"));

        assertFalse(board.movePieceEnPassant(4, 4, 3, 3, 4, 3));

        Piece blackPawn = board.getPieceAt(4, 4);
        assertNotNull(blackPawn);
        assertEquals("PAWN", blackPawn.getType());
        assertEquals("BLACK", blackPawn.getColor());

        Piece whitePawn = board.getPieceAt(4, 3);
        assertNotNull(whitePawn);
        assertEquals("PAWN", whitePawn.getType());
        assertEquals("WHITE", whitePawn.getColor());

        assertNull(board.getPieceAt(3, 3));
    }




}
