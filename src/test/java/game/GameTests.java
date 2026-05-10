package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class GameTests {

    @Test
    void startNewGameSetsCurrentPlayerToWhite() {
        Game game = new Game();

        game.startNewGame();

        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

}
