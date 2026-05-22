package game;

import player.Player;
import board.Board;

public class Game {

    private Player currentPlayer;
    private Player whitePlayer;
    private Player blackPlayer;

    private Board board;

    public void startNewGame() {
        whitePlayer = new Player("White Player", "WHITE");
        blackPlayer = new Player("Black Player", "BLACK");

        this.board = new Board();
        this.board.initializeBoard();

        currentPlayer = whitePlayer;
    }

    public Board getBoard() {
        return board;
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

    public boolean makeMove(int startRow, int startCol, int endRow, int endCol) {
        boolean moveSuccessful = board.movePiece(startRow, startCol, endRow, endCol);

        if (moveSuccessful) {
            switchTurn();
        }

        return moveSuccessful;
    }

    public boolean isGameOver() {
        return false;
    }
}
