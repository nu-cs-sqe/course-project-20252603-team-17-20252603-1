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
        return movePiece(startRow, startCol, endRow, endCol, null);
    }

    /**
     * Moves a piece. For pawn moves to the promotion rank, {@code promotionPiece} selects the
     * promoted type ({@code QUEEN}, {@code ROOK}, {@code BISHOP}, {@code KNIGHT}); {@code null}
     * or blank means queen.
     */
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol, String promotionPiece) {
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

        String promoTrim = promotionPiece == null ? "" : promotionPiece.trim();
        boolean promoSpecified = !promoTrim.isEmpty();
        if (promoSpecified && !"PAWN".equals(piece.getType())) {
            return false;
        }
        if ("PAWN".equals(piece.getType()) && promoSpecified && !isPawnPromotionRank(piece.getColor(), endRow)) {
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
        } else if  ("BISHOP".equals(piece.getType())) {
            if (!isLegalBishopMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else if ("KNIGHT".equals(piece.getType())){
            if (!isLegalKnightMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        }  else if ("QUEEN".equals(piece.getType())) {
            if (!isLegalQueenMove(startRow, startCol, endRow, endCol)) {
                return false;
            }
        } else if ("KING".equals(piece.getType())) {
            if (!isLegalKingMove(startRow, startCol, endRow, endCol)) {
                return false;
            }

        } else {
            return false;
        }

        if ("PAWN".equals(piece.getType()) && isPawnPromotionRank(piece.getColor(), endRow)) {
            String chosenType = resolvePromotionType(promoTrim);
            if (chosenType == null) {
                return false;
            }
            state[endRow][endCol] = new Piece(chosenType, piece.getColor());
        } else {
            state[endRow][endCol] = piece;
        }
        state[startRow][startCol] = null;
        return true;
    }

    private boolean isPawnPromotionRank(String color, int endRow) {
        if ("WHITE".equals(color)) {
            return endRow == 0;
        }
        if ("BLACK".equals(color)) {
            return endRow == 7;
        }
        return false;
    }

    /**
     * @param promoTrim trimmed promotion token; empty means queen
     * @return promotion type, or null if invalid when a choice was required
     */
    private String resolvePromotionType(String promoTrim) {
        if (promoTrim.isEmpty()) {
            return "QUEEN";
        }
        String upper = promoTrim.toUpperCase();
        if ("QUEEN".equals(upper) || "ROOK".equals(upper) || "BISHOP".equals(upper) || "KNIGHT".equals(upper)) {
            return upper;
        }
        return null;
    }

    private boolean isLegalKingMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDelta = Math.abs(endRow - startRow);
        int colDelta = Math.abs(endCol - startCol);

        return rowDelta <= 1 && colDelta <= 1 && (rowDelta != 0 || colDelta != 0);
    }

    private boolean isLegalQueenMove(int startRow, int startCol, int endRow, int endCol) {
        return isLegalRookMove(startRow, startCol, endRow, endCol)
                || isLegalBishopMove(startRow, startCol, endRow, endCol);
    }

    private boolean isLegalKnightMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDelta = Math.abs(endRow - startRow);
        int colDelta = Math.abs(endCol - startCol);

        return (rowDelta == 2 && colDelta == 1) || (rowDelta == 1 && colDelta == 2);
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

    public Board copy() {
        Board copy = new Board();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                copy.state[row][col] = this.state[row][col];
            }
        }

        return copy;
    }






}
