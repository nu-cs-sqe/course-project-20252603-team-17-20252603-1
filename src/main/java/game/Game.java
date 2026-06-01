package game;
import board.Piece;

import player.Player;
import board.Board;

public class Game {

    private Player currentPlayer;
    private Player whitePlayer;
    private Player blackPlayer;

    private Board board;

    private boolean gameOver;
    private String winnerColor;

    public void startNewGame() {
        whitePlayer = new Player("White Player", "WHITE");
        blackPlayer = new Player("Black Player", "BLACK");

        this.board = new Board();
        this.board.initializeBoard();

        currentPlayer = whitePlayer;

        gameOver = false;
        winnerColor = null;
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public String getWinnerColor() {
        return winnerColor;
    }

    public void switchTurn() {

        if (currentPlayer == whitePlayer) {
            currentPlayer = blackPlayer;
        }
        else {
            currentPlayer = whitePlayer;
        }
    }


    public boolean makeMove(int startRow, int startCol, int
            endRow, int endCol) {

        if (gameOver) {
            return false;
        }

        if (board == null || currentPlayer == null) {
            return false;
        }

        if (!board.isWithinBounds(startRow, startCol) || !board.isWithinBounds(endRow, endCol)) {
            return false;
        }

        Piece piece = board.getPieceAt(startRow, startCol);
        if (piece == null) {
            return false;
        }
        if (!piece.getColor().equals(currentPlayer.getColor())) {
            return false;
        }

        Piece destinationPiece = board.getPieceAt(endRow, endCol);

        boolean moveSuccessful = board.movePiece(startRow, startCol, endRow, endCol);

        if(!moveSuccessful) { return false; }

        if (destinationPiece != null && "KING".equals(destinationPiece.getType())) {
            gameOver = true;
            winnerColor = currentPlayer.getColor();
            return true;
        }

        switchTurn();

        return true;
    }

    public boolean isKingInCheck(String color) {
        return false;
    }



    public boolean isGameOver() {
        return gameOver;
    }
}
