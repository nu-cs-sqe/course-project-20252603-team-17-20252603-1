package game;

import player.Player;

public class Game {

    private Player currentPlayer;

    public void startNewGame() {
        currentPlayer = new Player("White Player", "WHITE");
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
