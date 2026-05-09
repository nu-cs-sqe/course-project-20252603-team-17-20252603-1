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

}
