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


}
