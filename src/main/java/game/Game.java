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

    private boolean draw;
    private boolean gameOver;
    private String winnerColor;

    public void startNewGame() {
        whitePlayer = new Player("White Player", "WHITE");
        blackPlayer = new Player("Black Player", "BLACK");

        this.board = new Board();
        this.board.initializeBoard();

        currentPlayer = whitePlayer;

        gameOver = false;
        draw = false;
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

    public boolean isDraw() {
        return draw;
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

        Board originalBoard = board;
        Board simulatedBoard = board.copy();

        boolean simulatedMoveSuccessful = simulatedBoard.movePiece(startRow, startCol, endRow, endCol);

        if (!simulatedMoveSuccessful) {
            return false;
        }

        board = simulatedBoard;
        boolean leavesOwnKingInCheck = isKingInCheck(currentPlayer.getColor());
        board = originalBoard;

        if (leavesOwnKingInCheck) {
            return false;
        }

        boolean moveSuccessful = board.movePiece(startRow, startCol, endRow, endCol);

        if (!moveSuccessful) {
            return false;
        }

        if (destinationPiece != null && "KING".equals(destinationPiece.getType())) {
            gameOver = true;
            winnerColor = currentPlayer.getColor();
            return true;
        }

        String opponentColor = "WHITE".equals(currentPlayer.getColor()) ? "BLACK" : "WHITE";

        if (isCheckmate(opponentColor)) {
            gameOver = true;
            winnerColor = currentPlayer.getColor();
            return true;
        }

        if (isStalemate(opponentColor)) {
            gameOver = true;
            winnerColor = null;
            draw = true;
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

                    if ("QUEEN".equals(piece.getType())
                            && queenAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }
                    if ("PAWN".equals(piece.getType())
                            && pawnAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }

                    if ("KING".equals(piece.getType())
                            && kingAttacksSquare(startRow, startCol, row, col)) {
                        return true;
                    }


                }
            }
        }

        return false;
    }

    private boolean kingAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        int rowDelta = Math.abs(targetRow - startRow);
        int colDelta = Math.abs(targetCol - startCol);

        return rowDelta <= 1 && colDelta <= 1 && (rowDelta != 0 || colDelta != 0);
    }

    private boolean pawnAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        Piece pawn = board.getPieceAt(startRow, startCol);

        int direction;
        if ("WHITE".equals(pawn.getColor())) {
            direction = -1;
        } else if ("BLACK".equals(pawn.getColor())) {
            direction = 1;
        } else {
            return false;
        }

        return targetRow - startRow == direction
                && Math.abs(targetCol - startCol) == 1;
    }


    private boolean queenAttacksSquare(int startRow, int startCol, int targetRow, int targetCol) {
        return rookAttacksSquare(startRow, startCol, targetRow, targetCol)
                || bishopAttacksSquare(startRow, startCol, targetRow, targetCol);
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

    public boolean isCheckmate(String color) {
        return isKingInCheck(color) && !hasAnyLegalMove(color);
    }


    public boolean isStalemate(String color) {
        return !isKingInCheck(color) && !hasAnyLegalMove(color);
    }

    private boolean kingHasLegalMove(String color) {
        int[] kingPosition = findKingPosition(color);

        if (kingPosition == null) {
            return false;
        }

        int kingRow = kingPosition[0];
        int kingCol = kingPosition[1];

        for (int rowDelta = -1; rowDelta <= 1; rowDelta++) {
            for (int colDelta = -1; colDelta <= 1; colDelta++) {
                if (rowDelta == 0 && colDelta == 0) {
                    continue;
                }

                int targetRow = kingRow + rowDelta;
                int targetCol = kingCol + colDelta;

                if (kingCanMoveTo(color, kingRow, kingCol, targetRow, targetCol)) {
                    return true;
                }
            }
        }

        return false;
    }

    private int[] findKingPosition(String color) {
        if (board == null) {
            return null;
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(row, col);

                if (piece != null
                        && "KING".equals(piece.getType())
                        && color.equals(piece.getColor())) {
                    return new int[] { row, col };
                }
            }
        }

        return null;
    }

    private boolean kingCanMoveTo(String color, int startRow, int startCol, int endRow, int endCol) {
        if (!board.isWithinBounds(endRow, endCol)) {
            return false;
        }

        Piece destination = board.getPieceAt(endRow, endCol);

        if (destination != null && color.equals(destination.getColor())) {
            return false;
        }

        Board originalBoard = board;
        Board simulatedBoard = board.copy();

        boolean moved = simulatedBoard.movePiece(startRow, startCol, endRow, endCol);
        if (!moved) {
            return false;
        }

        board = simulatedBoard;
        boolean stillInCheck = isKingInCheck(color);
        board = originalBoard;

        return !stillInCheck;
    }

    private boolean hasAnyLegalMove(String color) {
        if (board == null) {
            return false;
        }

        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                Piece piece = board.getPieceAt(startRow, startCol);

                if (piece == null || !color.equals(piece.getColor())) {
                    continue;
                }

                for (int endRow = 0; endRow < 8; endRow++) {
                    for (int endCol = 0; endCol < 8; endCol++) {
                        if (pieceCanMoveWithoutLeavingCheck(color, startRow, startCol, endRow, endCol)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean pieceCanMoveWithoutLeavingCheck(String color, int startRow, int startCol,
                                                    int endRow, int endCol) {
        Board originalBoard = board;
        Board simulatedBoard = board.copy();

        boolean moved = simulatedBoard.movePiece(startRow, startCol, endRow, endCol);
        if (!moved) {
            return false;
        }

        board = simulatedBoard;
        boolean leavesKingInCheck = isKingInCheck(color);
        board = originalBoard;

        return !leavesKingInCheck;
    }






    public boolean isGameOver() {
        return gameOver;
    }
}
