package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import board.Board;
import board.Piece;



public class GameTests {

    @Test
    void startNewGameSetsCurrentPlayerToWhite() {
        Game game = new Game();

        game.startNewGame();

        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void switchTurnChangesCurrentPlayerToBlack() {
        Game game = new Game();

        game.startNewGame();
        game.switchTurn();

        assertEquals("BLACK", game.getCurrentPlayer().getColor());
    }

    @Test
    void switchTurnChangesCurrentPlayerBackToWhite() {
        Game game = new Game();

        game.startNewGame();
        game.switchTurn();
        game.switchTurn();

        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void isGameOverReturnsFalseForNewGame() {
        Game game = new Game();

        game.startNewGame();

        assertFalse(game.isGameOver());
    }

    @Test
    void startNewGameCreatesBoard() {
        Game game = new Game();

        game.startNewGame();

        assertNotNull(game.getBoard());
    }

    @Test
    void startNewGameInitializesBoard() {
        Game game = new Game();

        game.startNewGame();

        Board board = game.getBoard();

        assertNotNull(board.getPieceAt(6, 0));
    }
    @Test
    void startNewGameAgainResetsBoard() {
        Game game = new Game();

        game.startNewGame();
        Board firstBoard = game.getBoard();

        game.startNewGame();
        Board secondBoard = game.getBoard();

        assertNotNull(secondBoard);
        assertNotSame(firstBoard, secondBoard);
    }

    @Test
    void getBoardReturnsNullBeforeGameStarts() {
        Game game = new Game();

        assertNull(game.getBoard());
    }

    @Test
    void validWhitePawnMoveSwitchesTurnToBlack() {
        Game game = new Game();

        game.startNewGame();

        boolean moved = game.makeMove(6, 0, 5, 0);

        assertTrue(moved);
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
    }

    @Test
    void validBlackPawnMoveSwitchesTurnBackToWhite() {
        Game game = new Game();

        game.startNewGame();
        game.makeMove(6, 0, 5, 0);

        boolean moved = game.makeMove(1, 0, 2, 0);

        assertTrue(moved);
        assertEquals("WHITE",game.getCurrentPlayer().getColor());
    }

    @Test
    void invalidMoveDoesNotSwitchTurn() {
        Game game = new Game();

        game.startNewGame();

        boolean moved = game.makeMove(6, 0, 3, 0);

        assertFalse(moved);
        assertEquals("WHITE",game.getCurrentPlayer().getColor());
    }

    @Test
    void playerCannotMoveOpponentsPiece() {
        Game game = new Game();

        game.startNewGame();

        boolean moved = game.makeMove(1, 0, 2, 0);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveBeforeStartNewGameReturnsFalse() {
        Game game = new Game();

        boolean moved = game.makeMove(6, 0, 5, 0);

        assertFalse(moved);
    }

    @Test
    void moveFromEmptySquareReturnsFalseAndDoesNotSwitchTurn() {
        Game game = new Game();
        game.startNewGame();

        boolean moved = game.makeMove(4, 4, 5, 4);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void moveFromOutOfBoundsStartReturnsFalseAndDoesNotSwitchTurn() {
        Game game = new Game();
        game.startNewGame();

        boolean moved = game.makeMove(-1, 0, 0, 0);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void moveToOutOfBoundsEndReturnsFalseAndDoesNotSwitchTurn() {
        Game game = new Game();
        game.startNewGame();

        boolean moved = game.makeMove(6, 0, 8, 0);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
        assertNotNull(game.getBoard().getPieceAt(6, 0));
    }

    @Test
    void moveToOccupiedSquareReturnsFalseAndDoesNotSwitchTurn() {
        Game game = new Game();
        game.startNewGame();

        boolean moved = game.makeMove(6, 0, 7, 0);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
        assertNotNull(game.getBoard().getPieceAt(6, 0));
        assertNotNull(game.getBoard().getPieceAt(7, 0));
    }

    @Test
    void invalidKnightMoveReturnsFalseAndDoesNotSwitchTurn() {
        Game game = new Game();
        game.startNewGame();

        boolean moved = game.makeMove(7, 1, 6, 1);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
        assertNotNull(game.getBoard().getPieceAt(7, 1));
    }


    @Test
    void moveAfterGameOverIsRejected() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 2, new Piece("KING", "WHITE"));
        placePiece(board, 2, 1, new Piece("QUEEN", "WHITE"));

        assertTrue(game.makeMove(2, 1, 1, 1));
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());

        boolean movedAfterGameOver = game.makeMove(0, 0, 0, 1);

        assertFalse(movedAfterGameOver);
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());
    }



    @Test
    void newGameWhiteKingIsNotInCheck() {
        Game game = new Game();
        game.startNewGame();

        assertFalse(game.isKingInCheck("WHITE"));
    }

    @Test
    void newGameBlackKingIsNotInCheck() {
        Game game = new Game();
        game.startNewGame();

        assertFalse(game.isKingInCheck("BLACK"));
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
    void blackRookCheckingWhiteKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void blackRookAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void squareNotAttackedReturnsFalse() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("ROOK", "BLACK"));

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void blockedRookDoesNotAttackSquare() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 4, 2, new Piece("PAWN", "WHITE"));

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }


    @Test
    void blockedRookDoesNotCheckWhiteKing() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 4, 2, new Piece("PAWN", "WHITE"));

        assertFalse(game.isKingInCheck("WHITE"));
    }

    @Test
    void blackBishopAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 1, 1, new Piece("BISHOP", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void blackBishopCheckingWhiteKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 1, 1, new Piece("BISHOP", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
    }


    @Test
    void blackKnightAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 2, 3, new Piece("KNIGHT", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void blackKnightCheckingWhiteKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 2, 3, new Piece("KNIGHT", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void blackQueenAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 1, 1, new Piece("QUEEN", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void blackQueenCheckingWhiteKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 1, 1, new Piece("QUEEN", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void blackPawnAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void blackPawnCheckingWhiteKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 3, 3, new Piece("PAWN", "BLACK"));

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void blackKingAttacksAdjacentSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 3, 3, new Piece("KING", "BLACK"));

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void outOfBoundsSquareUnderAttackReturnsFalse() {
        Game game = new Game();
        game.startNewGame();

        assertFalse(game.isSquareUnderAttack(-1, 0, "BLACK"));
    }

    @Test
    void squareUnderAttackBeforeGameStartsReturnsFalse() {
        Game game = new Game();

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void kingInCheckBeforeGameStartsReturnsFalse() {
        Game game = new Game();

        assertFalse(game.isKingInCheck("WHITE"));
    }

    @Test
    void whiteRookCheckingBlackKingReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "WHITE"));

        assertTrue(game.isKingInCheck("BLACK"));
    }

    @Test
    void whitePawnAttacksSquareReturnsTrue() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 5, 3, new Piece("PAWN", "WHITE"));

        assertTrue(game.isSquareUnderAttack(4, 4, "WHITE"));
    }

    @Test
    void movingShieldingRookAwayFromKingIsRejected() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 4, 2, new Piece("ROOK", "WHITE"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 0, 4, new Piece("KING", "BLACK"));

        boolean moved = game.makeMove(4, 2, 5, 2);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());

        Piece whiteRook = board.getPieceAt(4, 2);
        assertNotNull(whiteRook);
        assertEquals("ROOK", whiteRook.getType());
        assertEquals("WHITE", whiteRook.getColor());

        assertNull(board.getPieceAt(5, 2));
    }

    @Test
    void kingCannotMoveIntoCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 5, 0, new Piece("ROOK", "BLACK"));

        boolean moved = game.makeMove(4, 4, 5, 4);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());

        Piece king = board.getPieceAt(4, 4);
        assertNotNull(king);
        assertEquals("KING", king.getType());
        assertEquals("WHITE", king.getColor());

        assertNull(board.getPieceAt(5, 4));
    }

    @Test
    void kingCanMoveOutOfCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));

        boolean moved = game.makeMove(4, 4, 5, 4);

        assertTrue(moved);
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
        assertNull(board.getPieceAt(4, 4));

        Piece king = board.getPieceAt(5, 4);
        assertNotNull(king);
        assertEquals("KING", king.getType());
        assertEquals("WHITE", king.getColor());
    }


    @Test
    void playerCannotIgnoreCheckWithUnrelatedMove() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 6, 7, new Piece("PAWN", "WHITE"));

        boolean moved = game.makeMove(6, 7, 5, 7);

        assertFalse(moved);
        assertEquals("WHITE", game.getCurrentPlayer().getColor());

        Piece pawn = board.getPieceAt(6, 7);
        assertNotNull(pawn);
        assertEquals("PAWN", pawn.getType());
        assertEquals("WHITE", pawn.getColor());

        assertNull(board.getPieceAt(5, 7));
    }

    @Test
    void blackCannotMoveShieldingPieceAndExposeKing() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 0, 2, new Piece("ROOK", "BLACK"));
        placePiece(board, 0, 0, new Piece("ROOK", "WHITE"));
        placePiece(board, 7, 4, new Piece("KING", "WHITE"));

        game.switchTurn();

        boolean moved = game.makeMove(0, 2, 1, 2);

        assertFalse(moved);
        assertEquals("BLACK", game.getCurrentPlayer().getColor());

        Piece blackRook = board.getPieceAt(0, 2);
        assertNotNull(blackRook);
        assertEquals("ROOK", blackRook.getType());
        assertEquals("BLACK", blackRook.getColor());

        assertNull(board.getPieceAt(1, 2));
    }

    @Test
    void playerCanCaptureCheckingPiece() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 7, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 2, 0, new Piece("ROOK", "WHITE"));

        boolean moved = game.makeMove(2, 0, 4, 0);

        assertTrue(moved);
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
        assertNull(board.getPieceAt(2, 0));

        Piece rook = board.getPieceAt(4, 0);
        assertNotNull(rook);
        assertEquals("ROOK", rook.getType());
        assertEquals("WHITE", rook.getColor());
    }

    @Test
    void playerCanBlockCheck() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 7, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 2, 1, new Piece("BISHOP", "WHITE"));

        boolean moved = game.makeMove(2, 1, 4, 3);

        assertTrue(moved);
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
        assertNull(board.getPieceAt(2, 1));

        Piece bishop = board.getPieceAt(4, 3);
        assertNotNull(bishop);
        assertEquals("BISHOP", bishop.getType());
        assertEquals("WHITE", bishop.getColor());
    }

    @Test
    void whiteNotInCheckIsNotCheckmate() {
        Game game = new Game();
        game.startNewGame();

        assertFalse(game.isCheckmate("WHITE"));
    }

    @Test
    void blackInCheckIsNotStalemate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 4, new Piece("KING", "BLACK"));
        placePiece(board, 7, 4, new Piece("KING", "WHITE"));
        placePiece(board, 1, 4, new Piece("ROOK", "WHITE"));

        assertFalse(game.isStalemate("BLACK"));
    }

    @Test
    void whiteInCheckButCanEscapeIsNotCheckmate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));

        assertFalse(game.isCheckmate("WHITE"));
    }

    @Test
    void whiteInCheckWithNoLegalMovesIsCheckmate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "WHITE"));
        placePiece(board, 1, 1, new Piece("QUEEN", "BLACK"));
        placePiece(board, 2, 2, new Piece("KING", "BLACK"));

        assertTrue(game.isCheckmate("WHITE"));
    }

    @Test
    void blackWithLegalKingMoveIsNotStalemate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 7, 7, new Piece("KING", "WHITE"));

        assertFalse(game.isStalemate("BLACK"));
    }


    @Test
    void blackNotInCheckWithNoLegalKingMovesIsStalemate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 1, new Piece("KING", "WHITE"));
        placePiece(board, 1, 2, new Piece("QUEEN", "WHITE"));

        assertTrue(game.isStalemate("BLACK"));
    }

    @Test
    void blackWithLegalPawnMoveIsNotStalemate() {
        Game game = new Game();
        game.startNewGame();

        assertFalse(game.isStalemate("BLACK"));
    }

    @Test
    void whiteInCheckButCanBlockWithRookIsNotCheckmate() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 4, 4, new Piece("KING", "WHITE"));
        placePiece(board, 0, 7, new Piece("KING", "BLACK"));
        placePiece(board, 4, 0, new Piece("ROOK", "BLACK"));
        placePiece(board, 2, 2, new Piece("ROOK", "WHITE"));

        assertFalse(game.isCheckmate("WHITE"));
    }

    @Test
    void moveThatCheckmatesBlackEndsGameAndWhiteWins() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 2, new Piece("KING", "WHITE"));
        placePiece(board, 2, 1, new Piece("QUEEN", "WHITE"));

        boolean moved = game.makeMove(2, 1, 1, 1);

        assertTrue(moved);
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());
        assertTrue(game.isCheckmate("BLACK"));
    }

    @Test
    void moveAfterCheckmateIsRejected() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 2, new Piece("KING", "WHITE"));
        placePiece(board, 2, 1, new Piece("QUEEN", "WHITE"));

        assertTrue(game.makeMove(2, 1, 1, 1));
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());

        boolean movedAfterCheckmate = game.makeMove(0, 0, 0, 1);

        assertFalse(movedAfterCheckmate);
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());
    }

    @Test
    void moveThatStalematesBlackEndsGameAsDraw() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 1, new Piece("KING", "WHITE"));
        placePiece(board, 2, 3, new Piece("QUEEN", "WHITE"));

        boolean moved = game.makeMove(2, 3, 1, 2);

        assertTrue(moved);
        assertTrue(game.isGameOver());
        assertTrue(game.isDraw());
        assertNull(game.getWinnerColor());
        assertTrue(game.isStalemate("BLACK"));
    }

    @Test
    void startNewGameResetsDrawState() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 0, 0, new Piece("KING", "BLACK"));
        placePiece(board, 2, 1, new Piece("KING", "WHITE"));
        placePiece(board, 2, 3, new Piece("QUEEN", "WHITE"));

        assertTrue(game.makeMove(2, 3, 1, 2));
        assertTrue(game.isDraw());

        game.startNewGame();

        assertFalse(game.isDraw());
        assertNull(game.getWinnerColor());
        assertFalse(game.isGameOver());
    }

    @Test
    void moveThatCheckmatesWhiteEndsGameAndBlackWins() {
        Game game = new Game();
        game.startNewGame();
        Board board = game.getBoard();
        clearBoard(board);

        placePiece(board, 7, 7, new Piece("KING", "WHITE"));
        placePiece(board, 5, 5, new Piece("KING", "BLACK"));
        placePiece(board, 5, 6, new Piece("QUEEN", "BLACK"));

        game.switchTurn();

        boolean moved = game.makeMove(5, 6, 6, 6);

        assertTrue(moved);
        assertTrue(game.isGameOver());
        assertEquals("BLACK", game.getWinnerColor());
        assertTrue(game.isCheckmate("WHITE"));
    }





}
