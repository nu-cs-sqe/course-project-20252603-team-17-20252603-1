package BoardTests;

import Board.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTests {
    @Test
    void initEmptyBoardGeneratesInternalState() {
        Board board = new Board();

        board.initializeBoard();

        int pieceCount = 0;

        for(int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPieceAt(row, col) != null) {
                    pieceCount++;
                }
            }
        }

        assertEquals(32, pieceCount);
    }

}
