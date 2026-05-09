package player;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTests {

    @Test
    void getNameReturnsPlayerName(){
        Player player = new Player("White Player", "WHITE");

        assertEquals("White Player", player.getName());

    }
}
