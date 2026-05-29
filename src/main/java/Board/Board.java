package board;

public class Board {

    private Piece[][] state;

    public Board() {
        this.state = new Piece[8][8];
    }

    public void initializeBoard() {
        this.state = new Piece[8][8];
        // first create the top row, which will be the first
        // row at the top assuming that you are sitting on the
        // white side.
        this.state[0] = generateBoundaryRows("BLACK");
        this.state[1] = generatePawnRow("BLACK");

        this.state[6] = generatePawnRow("WHITE");
        this.state[7] = generateBoundaryRows("WHITE");

        return;
    }

    public Piece getPieceAt(int row, int col) {
        if (!isWithinBounds(row, col)) {
            throw new IllegalArgumentException("INVALID POSITION");
        }

        return this.state[row][col];
    }

    public boolean isWithinBounds(int row, int col) {
        return (row >= 0 && row < 8) && (col >= 0 && col < 8);
    }



    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        if (!isWithinBounds(startRow, startCol) || !isWithinBounds(endRow, endCol)) {
            return false;
        }
        Piece piece = state[startRow][startCol];
        if (piece == null) {
            return false;
        }
        Piece destination = state[endRow][endCol];
        if (destination != null && destination.getColor().equals(piece.getColor())) {
            return false;
        }

        if ("PAWN".equals(piece.getType())) {
            if (!isLegalPawnMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else if ("ROOK".equals(piece.getType())) {
            if (!isLegalRookMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else if  ("BISHOP".equals(piece.getType())){
            if (!isLegalBishopMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else {
            return false;
        }

        state[endRow][endCol] = piece;
        state[startRow][startCol] = null;
        return true;
    }

    private boolean isLegalBishopMove(int startRow, int startCol, int endRow, int endCol) {
        if (Math.abs(endRow - startRow) != Math.abs(endCol - startCol)) {
            return false;
        }

        return isPathClear(startRow, startCol, endRow, endCol);
    }


    private boolean isLegalRookMove(int startRow, int startCol, int endRow, int endCol) {
        if (startRow != endRow && startCol != endCol) {
            return false;
        }
        return isPathClear(startRow, startCol, endRow, endCol);
    }

    private boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {
        int rowStep = Integer.compare(endRow, startRow);
        int colStep = Integer.compare(endCol, startCol);

        int row = startRow + rowStep;
        int col = startCol + colStep;

        while (row != endRow || col != endCol) {
            if (state[row][col] != null) {
                return false;
            }

            row += rowStep;
            col += colStep;
        }

        return true;
    }



    private boolean isLegalPawnMove(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = state[startRow][startCol];
        Piece destination = state[endRow][endCol];

        int direction;
        int startRank;
        String opponentColor;

        if ("WHITE".equals(piece.getColor())) {
            direction = -1;
            startRank = 6;
            opponentColor = "BLACK";
        } else if ("BLACK".equals(piece.getColor())) {
            direction = 1;
            startRank = 1;
            opponentColor = "WHITE";
        } else {
            return false;
        }

        int rowDelta = endRow - startRow;
        int colDelta = endCol - startCol;

        if (colDelta == 0 && destination == null) {
            if (rowDelta == direction) {
                return true;
            }

            if (rowDelta == 2 * direction && startRow == startRank) {
                return state[startRow + direction][startCol] == null;
            }
        }

        if (Math.abs(colDelta) == 1 && rowDelta == direction) {
            return destination != null && opponentColor.equals(destination.getColor());
        }

        return false;
    }






    private Piece[] generateBoundaryRows(String color) {
        Piece[] row = new Piece[8];

        row[0] = new Piece("ROOK", color);
        row[1] = new Piece("KNIGHT", color);
        row[2] = new Piece("BISHOP", color);
        row[3] = new Piece("QUEEN", color);
        row[4] = new Piece("KING", color);
        row[5] = new Piece("BISHOP", color);
        row[6] = new Piece("KNIGHT", color);
        row[7] = new Piece("ROOK", color);

        return row;
    }

    private Piece[] generatePawnRow(String color) {
        Piece[] row = new Piece[8];

        for (int col = 0; col < 8; col++) {
            row[col] = new Piece("PAWN", color);
        }

        return row;
    }



}
