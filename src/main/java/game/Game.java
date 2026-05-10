package game;

import player.Player;

public class Game {

    private Player currentPlayer;
    private Player whitePlayer;
    private Player blackPlayer;

    public void startNewGame() {
        whitePlayer = new Player("White Player", "WHITE");
        blackPlayer = new Player("Black Player", "BLACK");

        currentPlayer = whitePlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchTurn() {

        if (currentPlayer == whitePlayer) {
            currentPlayer = blackPlayer;
        }
        else {
            currentPlayer = whitePlayer;
        }
    }

    public boolean isGameOver() {
        return false;
    }
}
