package game;
import board.Piece;

import player.Player;
import board.Board;
import board.Piece;

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
        if (board == null) {
            return false;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);

                if (piece != null
                        && "KING".equals(piece.getType())
                        && color.equals(piece.getColor())) {
                    String opponentColor = "WHITE".equals(color) ? "BLACK" : "WHITE";
                    return isSquareUnderAttack(row, col, opponentColor);
                }
            }
        }

        return false;
    }

    public boolean isSquareUnderAttack(int row, int col, String byColor) {
        if (board == null || !board.isWithinBounds(row, col)) {
            return false;
        }

        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                Piece piece = board.getPieceAt(startRow, startCol);

                if (piece != null && byColor.equals(piece.getColor())) {
                    if ("ROOK".equals(piece.getType())
                            && rookAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }

                    if ("BISHOP".equals(piece.getType())
                            && bishopAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }

                    if ("KNIGHT".equals(piece.getType())
                            && knightAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }

                }
            }
        }

        return false;
    }

    private boolean knightAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        int rowDelta = Math.abs(targetRow - startRow);
        int colDelta = Math.abs(targetCol - startCol);

        return (rowDelta == 2 && colDelta == 1) || (rowDelta == 1 && colDelta == 2);
    }


    private boolean bishopAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        if (Math.abs(targetRow - startRow) != Math.abs(targetCol - startCol)) {
            return false;
        }

        return isPathClear(startRow, startCol, targetRow, targetCol);
    }


    private boolean rookAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        if (startRow != targetRow && startCol != targetCol) {
            return false;
        }

        return isPathClear(startRow, startCol, targetRow, targetCol);
    }

    private boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {
        int rowStep = Integer.compare(endRow, startRow);
        int colStep = Integer.compare(endCol, startCol);

        int row = startRow + rowStep;
        int col = startCol + colStep;

        while (row != endRow || col != endCol) {
            if (board.getPieceAt(row, col) != null) {
                return false;
            }

            row += rowStep;
            col += colStep;
        }

        return true;
    }






    public boolean isGameOver() {
        return gameOver;
    }
}
