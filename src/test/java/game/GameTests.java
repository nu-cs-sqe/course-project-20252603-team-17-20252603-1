package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.*;


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

}
