package player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    @Test
    void getNameReturnsPlayerName(){
        Player player = new Player("White Player", "WHITE");

        assertEquals("White Player", player.getName());

    }

    @Test
    void getColorReturnsPlayerColor() {
        Player player = new Player("Black Player", "BLACK");

        assertEquals("BLACK", player.getColor());
    }

    @Test
    void getNameReturnsBlackPlayerName() {
        Player player = new Player("Black Player", "BLACK");

        assertEquals("Black Player", player.getName());
    }

    @Test
    void getColorReturnsWhitePlayerColor() {
        Player player = new Player("White Player", "WHITE");

        assertEquals("WHITE", player.getColor());
    }

}
