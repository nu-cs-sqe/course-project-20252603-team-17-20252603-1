package Board;

public class Board {

    public Piece[][] state; // temporarily public, revert to private once movePiece complete

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
        if (state[endRow][endCol] != null) {
            return false;
        }
        if (!"PAWN".equals(piece.getType())) {
            return false;
        }
        if ("WHITE".equals(piece.getColor())) {
            if (!isLegalWhitePawnForward(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else if ("BLACK".equals(piece.getColor())) {
            if (!isLegalBlackPawnForward(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else {
            return false;
        }
        state[endRow][endCol] = piece;
        state[startRow][startCol] = null;
        return true;
    }
    

    private boolean isLegalWhitePawnForward(int startRow, int startCol, int endRow, int endCol) {
        if (endCol != startCol) {
            return false;
        }
        int delta = endRow - startRow;
        if (delta == -1) {
            return true;
        }
        if (delta == -2 && startRow == 6) {
            return state[startRow - 1][startCol] == null;
        }
        return false;
    }


    private boolean isLegalBlackPawnForward(int startRow, int startCol, int endRow, int endCol) {
        if (endCol != startCol) {
            return false;
        }
        int delta = endRow - startRow;
        if (delta == 1) {
            return true;
        }
        if (delta == 2 && startRow == 1) {
            return state[startRow + 1][startCol] == null;
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
