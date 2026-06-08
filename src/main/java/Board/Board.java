package board;

public class Board {

    private Piece[][] state;

    private boolean whiteCastleKingSide;
    private boolean whiteCastleQueenSide;
    private boolean blackCastleKingSide;
    private boolean blackCastleQueenSide;

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

        whiteCastleKingSide = true;
        whiteCastleQueenSide = true;
        blackCastleKingSide = true;
        blackCastleQueenSide = true;

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
            if (isCastlingAttempt(startRow, startCol, endRow, endCol, piece.getColor())) {
                if (!isLegalCastling(startRow, startCol, endRow, endCol, piece.getColor())) {
                    return false;
                }
            } else if (!isLegalKingMove(startRow, startCol, endRow, endCol)) {
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
        } else if ("KING".equals(piece.getType())
                && isCastlingAttempt(startRow, startCol, endRow, endCol, piece.getColor())) {
            executeCastling(startRow, startCol, endRow, endCol, piece.getColor());
        } else {
            state[endRow][endCol] = piece;
        }
        state[startRow][startCol] = null;
        updateCastlingRightsAfterMove(piece, startRow, startCol, endRow, endCol, destination);
        return true;
    }

    private void updateCastlingRightsAfterMove(Piece moved, int startRow, int startCol, int endRow, int endCol,
                                               Piece captured) {
        if ("KING".equals(moved.getType())) {
            if ("WHITE".equals(moved.getColor())) {
                whiteCastleKingSide = false;
                whiteCastleQueenSide = false;
            } else {
                blackCastleKingSide = false;
                blackCastleQueenSide = false;
            }
        }
        if ("ROOK".equals(moved.getType())) {
            if ("WHITE".equals(moved.getColor()) && startRow == 7) {
                if (startCol == 0) {
                    whiteCastleQueenSide = false;
                }
                if (startCol == 7) {
                    whiteCastleKingSide = false;
                }
            }
            if ("BLACK".equals(moved.getColor()) && startRow == 0) {
                if (startCol == 0) {
                    blackCastleQueenSide = false;
                }
                if (startCol == 7) {
                    blackCastleKingSide = false;
                }
            }
        }
        if (captured != null && "ROOK".equals(captured.getType())) {
            if ("WHITE".equals(captured.getColor()) && endRow == 7) {
                if (endCol == 0) {
                    whiteCastleQueenSide = false;
                }
                if (endCol == 7) {
                    whiteCastleKingSide = false;
                }
            }
            if ("BLACK".equals(captured.getColor()) && endRow == 0) {
                if (endCol == 0) {
                    blackCastleQueenSide = false;
                }
                if (endCol == 7) {
                    blackCastleKingSide = false;
                }
            }
        }
    }

    private boolean isCastlingAttempt(int startRow, int startCol, int endRow, int endCol, String color) {
        if (startRow != endRow || Math.abs(endCol - startCol) != 2 || startCol != 4) {
            return false;
        }
        if ("WHITE".equals(color) && startRow != 7) {
            return false;
        }
        if ("BLACK".equals(color) && startRow != 0) {
            return false;
        }
        return endCol == 2 || endCol == 6;
    }

    private boolean isLegalCastling(int startRow, int startCol, int endRow, int endCol, String color) {
        if ("WHITE".equals(color)) {
            if (endCol == 6) {
                return whiteCastleKingSide
                        && isRookAt(startRow, 7, "WHITE", "ROOK")
                        && state[startRow][5] == null
                        && state[startRow][6] == null;
            }
            if (endCol == 2) {
                return whiteCastleQueenSide
                        && isRookAt(startRow, 0, "WHITE", "ROOK")
                        && state[startRow][1] == null
                        && state[startRow][2] == null
                        && state[startRow][3] == null;
            }
        } else if ("BLACK".equals(color)) {
            if (endCol == 6) {
                return blackCastleKingSide
                        && isRookAt(startRow, 7, "BLACK", "ROOK")
                        && state[startRow][5] == null
                        && state[startRow][6] == null;
            }
            if (endCol == 2) {
                return blackCastleQueenSide
                        && isRookAt(startRow, 0, "BLACK", "ROOK")
                        && state[startRow][1] == null
                        && state[startRow][2] == null
                        && state[startRow][3] == null;
            }
        }
        return false;
    }

    private boolean isRookAt(int row, int col, String color, String type) {
        Piece p = state[row][col];
        return p != null && color.equals(p.getColor()) && type.equals(p.getType());
    }

    private void executeCastling(int startRow, int startCol, int endRow, int endCol, String color) {
        Piece king = state[startRow][startCol];
        state[startRow][startCol] = null;
        if (endCol == 6) {
            Piece rook = state[startRow][7];
            state[startRow][7] = null;
            state[startRow][endCol] = king;
            state[startRow][5] = rook;
        } else {
            Piece rook = state[startRow][0];
            state[startRow][0] = null;
            state[startRow][endCol] = king;
            state[startRow][3] = rook;
        }
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

        copy.whiteCastleKingSide = this.whiteCastleKingSide;
        copy.whiteCastleQueenSide = this.whiteCastleQueenSide;
        copy.blackCastleKingSide = this.blackCastleKingSide;
        copy.blackCastleQueenSide = this.blackCastleQueenSide;

        return copy;
    }

    public boolean movePieceEnPassant(Position start, Position end, Position capturedPawnPosition) {
        if (!isValidEnPassantMove(start, end, capturedPawnPosition)) {
            return false;
        }

        executeEnPassant(start, end, capturedPawnPosition);
        return true;
    }

    private boolean isValidEnPassantMove(Position start, Position end, Position capturedPawnPosition) {
        if (!isWithinBounds(start.getRow(), start.getCol())
                || !isWithinBounds(end.getRow(), end.getCol())
                || !isWithinBounds(capturedPawnPosition.getRow(), capturedPawnPosition.getCol())) {
            return false;
        }

        Piece piece = state[start.getRow()][start.getCol()];
        Piece capturedPawn = state[capturedPawnPosition.getRow()][capturedPawnPosition.getCol()];

        if (piece == null || !"PAWN".equals(piece.getType())) {
            return false;
        }

        if (capturedPawn == null
                || !"PAWN".equals(capturedPawn.getType())
                || piece.getColor().equals(capturedPawn.getColor())) {
            return false;
        }

        if (state[end.getRow()][end.getCol()] != null) {
            return false;
        }

        int direction = "WHITE".equals(piece.getColor()) ? -1 : 1;
        if (end.getRow() - start.getRow() != direction) {
            return false;
        }

        return Math.abs(end.getCol() - start.getCol()) == 1;
    }

    private void executeEnPassant(Position start, Position end, Position capturedPawnPosition) {
        Piece piece = state[start.getRow()][start.getCol()];

        state[end.getRow()][end.getCol()] = piece;
        state[start.getRow()][start.getCol()] = null;
        state[capturedPawnPosition.getRow()][capturedPawnPosition.getCol()] = null;
    }









}
