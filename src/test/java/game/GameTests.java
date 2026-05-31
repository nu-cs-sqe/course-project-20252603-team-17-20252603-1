package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;

import board.Board;


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
    void capturingBlackKingMakesGameOverAndWhiteWins() {
        Game game = new Game();
        game.startNewGame();

        assertTrue(game.makeMove(6, 4, 4, 4)); // White opens queen path
        assertTrue(game.makeMove(1, 0, 2, 0)); // Black filler move

        assertTrue(game.makeMove(7, 3, 3, 7)); // White queen out
        assertTrue(game.makeMove(1, 1, 2, 1)); // Black filler move

        assertTrue(game.makeMove(3, 7, 1, 5)); // White queen moves near king
        assertTrue(game.makeMove(1, 2, 2, 2)); // Black filler move

        boolean moved = game.makeMove(1, 5, 0, 4); // White queen captures Black king

        assertTrue(moved);
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());
    }

    @Test
    void capturingWhiteKingMakesGameOverAndBlackWins() {
        Game game = new Game();
        game.startNewGame();

        assertTrue(game.makeMove(6, 0, 5, 0)); // White filler
        assertTrue(game.makeMove(1, 4, 3, 4)); // Black opens queen path

        assertTrue(game.makeMove(6, 1, 5, 1)); // White filler
        assertTrue(game.makeMove(0, 3, 4, 7)); // Black queen out

        assertTrue(game.makeMove(6, 2, 5, 2)); // White filler
        assertTrue(game.makeMove(4, 7, 6, 5)); // Black queen near king

        assertTrue(game.makeMove(6, 3, 5, 3)); // White filler

        boolean moved = game.makeMove(6, 5, 7, 4); // Black queen captures White king

        assertTrue(moved);
        assertTrue(game.isGameOver());
        assertEquals("BLACK", game.getWinnerColor());
    }

    @Test
    void moveAfterGameOverIsRejected() {
        Game game = new Game();
        game.startNewGame();

        assertTrue(game.makeMove(6, 4, 4, 4)); // White opens queen path
        assertTrue(game.makeMove(1, 0, 2, 0)); // Black filler

        assertTrue(game.makeMove(7, 3, 3, 7)); // White queen out
        assertTrue(game.makeMove(1, 1, 2, 1)); // Black filler

        assertTrue(game.makeMove(3, 7, 1, 5)); // White queen near king
        assertTrue(game.makeMove(1, 3, 2, 3)); // Black filler

        assertTrue(game.makeMove(1, 5, 0, 4)); // White captures Black king

        boolean movedAfterGameOver = game.makeMove(6, 0, 5, 0);

        assertFalse(movedAfterGameOver);
        assertTrue(game.isGameOver());
        assertEquals("WHITE", game.getWinnerColor());
    }



}
