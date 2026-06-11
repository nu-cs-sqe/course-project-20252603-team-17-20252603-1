package board;

public class Board {

    private static final int BOARD_SIZE = 8;
    private static final int POSITION_KEY_CAPACITY = 256;

    private static final int BLACK_BACK_ROW = 0;
    private static final int BLACK_PAWN_ROW = 1;
    private static final int WHITE_PAWN_ROW = 6;
    private static final int WHITE_BACK_ROW = 7;

    private static final int ROOK_QUEENSIDE_COL = 0;
    private static final int KNIGHT_QUEENSIDE_COL = 1;
    private static final int BISHOP_QUEENSIDE_COL = 2;
    private static final int QUEEN_START_COL = 3;
    private static final int KING_START_COL = 4;
    private static final int BISHOP_KINGSIDE_COL = 5;
    private static final int KNIGHT_KINGSIDE_COL = 6;
    private static final int ROOK_KINGSIDE_COL = 7;

    private static final int QUEENSIDE_CASTLE_COL = 2;
    private static final int KINGSIDE_CASTLE_COL = 6;
    private static final int QUEENSIDE_CASTLE_KING_PATH_COL = 3;
    private static final int KINGSIDE_CASTLE_KING_PATH_COL = 5;
    private static final int CASTLING_COL_DELTA = 2;

    private static final int WHITE_PAWN_DIRECTION = -1;
    private static final int BLACK_PAWN_DIRECTION = 1;
    private static final int PAWN_DOUBLE_MOVE_DISTANCE = 2;

    private static final int KING_MOVE_LIMIT = 1;
    private static final int KNIGHT_LONG_MOVE = 2;
    private static final int KNIGHT_SHORT_MOVE = 1;

    private static final String WHITE = "WHITE";
    private static final String BLACK = "BLACK";

    private static final String KING = "KING";
    private static final String QUEEN = "QUEEN";
    private static final String ROOK = "ROOK";
    private static final String BISHOP = "BISHOP";
    private static final String KNIGHT = "KNIGHT";
    private static final String PAWN = "PAWN";

    private static final String EMPTY_PROMOTION = "";
    private static final String EMPTY_SQUARE_KEY = "--";
    private static final String POSITION_SEPARATOR = ";";
    private static final String PIECE_SEPARATOR = ",";
    private static final String CASTLING_RIGHT_AVAILABLE = "1";
    private static final String CASTLING_RIGHT_UNAVAILABLE = "0";

    private Piece[][] state;

    private boolean whiteCastleKingSide;
    private boolean whiteCastleQueenSide;
    private boolean blackCastleKingSide;
    private boolean blackCastleQueenSide;

    public Board() {
        this.state = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    public void initializeBoard() {
        this.state = new Piece[BOARD_SIZE][BOARD_SIZE];

        this.state[BLACK_BACK_ROW] = generateBoundaryRows(BLACK);
        this.state[BLACK_PAWN_ROW] = generatePawnRow(BLACK);

        this.state[WHITE_PAWN_ROW] = generatePawnRow(WHITE);
        this.state[WHITE_BACK_ROW] = generateBoundaryRows(WHITE);

        whiteCastleKingSide = true;
        whiteCastleQueenSide = true;
        blackCastleKingSide = true;
        blackCastleQueenSide = true;
    }

    public Piece getPieceAt(int row, int col) {
        if (!isWithinBounds(row, col)) {
            throw new IllegalArgumentException("INVALID POSITION");
        }

        return this.state[row][col];
    }

    public boolean isWithinBounds(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }

    public String piecePlacementKey() {
        StringBuilder sb = new StringBuilder(POSITION_KEY_CAPACITY);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece p = state[row][col];
                if (p == null) {
                    sb.append(EMPTY_SQUARE_KEY);
                } else {
                    sb.append(p.getType()).append(PIECE_SEPARATOR).append(p.getColor());
                }
                sb.append(POSITION_SEPARATOR);
            }
        }
        return sb.toString();
    }

    public String castlingRightsKey() {
        return (whiteCastleKingSide ? CASTLING_RIGHT_AVAILABLE : CASTLING_RIGHT_UNAVAILABLE)
                + (whiteCastleQueenSide ? CASTLING_RIGHT_AVAILABLE : CASTLING_RIGHT_UNAVAILABLE)
                + (blackCastleKingSide ? CASTLING_RIGHT_AVAILABLE : CASTLING_RIGHT_UNAVAILABLE)
                + (blackCastleQueenSide ? CASTLING_RIGHT_AVAILABLE : CASTLING_RIGHT_UNAVAILABLE);
    }

    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        return movePiece(startRow, startCol, endRow, endCol, null);
    }

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

        String promoTrim = promotionPiece == null ? EMPTY_PROMOTION : promotionPiece.trim();
        boolean promoSpecified = !promoTrim.isEmpty();

        if (promoSpecified && !PAWN.equals(piece.getType())) {
            return false;
        }

        if (PAWN.equals(piece.getType()) && promoSpecified && !isPawnPromotionRank(piece.getColor(), endRow)) {
            return false;
        }

        if (!isLegalMoveForPiece(piece, startRow, startCol, endRow, endCol)) {
            return false;
        }

        if (PAWN.equals(piece.getType()) && isPawnPromotionRank(piece.getColor(), endRow)) {
            String chosenType = resolvePromotionType(promoTrim);
            if (chosenType == null) {
                return false;
            }
            state[endRow][endCol] = new Piece(chosenType, piece.getColor());
        } else if (KING.equals(piece.getType())
                && isCastlingAttempt(startRow, startCol, endRow, endCol, piece.getColor())) {
            executeCastling(startRow, startCol, endRow, endCol);
        } else {
            state[endRow][endCol] = piece;
        }

        state[startRow][startCol] = null;
        updateCastlingRightsAfterMove(piece, startRow, startCol, endRow, endCol, destination);
        return true;
    }

    private boolean isLegalMoveForPiece(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        if (PAWN.equals(piece.getType())) {
            return isLegalPawnMove(startRow, startCol, endRow, endCol);
        }

        if (ROOK.equals(piece.getType())) {
            return isLegalRookMove(startRow, startCol, endRow, endCol);
        }

        if (BISHOP.equals(piece.getType())) {
            return isLegalBishopMove(startRow, startCol, endRow, endCol);
        }

        if (KNIGHT.equals(piece.getType())) {
            return isLegalKnightMove(startRow, startCol, endRow, endCol);
        }

        if (QUEEN.equals(piece.getType())) {
            return isLegalQueenMove(startRow, startCol, endRow, endCol);
        }

        if (KING.equals(piece.getType())) {
            if (isCastlingAttempt(startRow, startCol, endRow, endCol, piece.getColor())) {
                return isLegalCastling(startRow, startCol, endRow, endCol, piece.getColor());
            }
            return isLegalKingMove(startRow, startCol, endRow, endCol);
        }

        return false;
    }

    private void updateCastlingRightsAfterMove(Piece moved, int startRow, int startCol, int endRow, int endCol,
                                               Piece captured) {
        if (KING.equals(moved.getType())) {
            if (WHITE.equals(moved.getColor())) {
                whiteCastleKingSide = false;
                whiteCastleQueenSide = false;
            } else {
                blackCastleKingSide = false;
                blackCastleQueenSide = false;
            }
        }

        if (ROOK.equals(moved.getType())) {
            updateCastlingRightsAfterRookMove(moved, startRow, startCol);
        }

        if (captured != null && ROOK.equals(captured.getType())) {
            updateCastlingRightsAfterRookCapture(captured, endRow, endCol);
        }
    }

    private void updateCastlingRightsAfterRookMove(Piece moved, int startRow, int startCol) {
        if (WHITE.equals(moved.getColor()) && startRow == WHITE_BACK_ROW) {
            if (startCol == ROOK_QUEENSIDE_COL) {
                whiteCastleQueenSide = false;
            }
            if (startCol == ROOK_KINGSIDE_COL) {
                whiteCastleKingSide = false;
            }
        }

        if (BLACK.equals(moved.getColor()) && startRow == BLACK_BACK_ROW) {
            if (startCol == ROOK_QUEENSIDE_COL) {
                blackCastleQueenSide = false;
            }
            if (startCol == ROOK_KINGSIDE_COL) {
                blackCastleKingSide = false;
            }
        }
    }

    private void updateCastlingRightsAfterRookCapture(Piece captured, int endRow, int endCol) {
        if (WHITE.equals(captured.getColor()) && endRow == WHITE_BACK_ROW) {
            if (endCol == ROOK_QUEENSIDE_COL) {
                whiteCastleQueenSide = false;
            }
            if (endCol == ROOK_KINGSIDE_COL) {
                whiteCastleKingSide = false;
            }
        }

        if (BLACK.equals(captured.getColor()) && endRow == BLACK_BACK_ROW) {
            if (endCol == ROOK_QUEENSIDE_COL) {
                blackCastleQueenSide = false;
            }
            if (endCol == ROOK_KINGSIDE_COL) {
                blackCastleKingSide = false;
            }
        }
    }

    private boolean isCastlingAttempt(int startRow, int startCol, int endRow, int endCol, String color) {
        if (startRow != endRow
                || Math.abs(endCol - startCol) != CASTLING_COL_DELTA
                || startCol != KING_START_COL) {
            return false;
        }

        if (WHITE.equals(color) && startRow != WHITE_BACK_ROW) {
            return false;
        }

        if (BLACK.equals(color) && startRow != BLACK_BACK_ROW) {
            return false;
        }

        return endCol == QUEENSIDE_CASTLE_COL || endCol == KINGSIDE_CASTLE_COL;
    }

    private boolean isLegalCastling(int startRow, int startCol, int endRow, int endCol, String color) {
        if (WHITE.equals(color)) {
            if (endCol == KINGSIDE_CASTLE_COL) {
                return whiteCastleKingSide
                        && isRookAt(startRow, ROOK_KINGSIDE_COL, WHITE, ROOK)
                        && state[startRow][KINGSIDE_CASTLE_KING_PATH_COL] == null
                        && state[startRow][KINGSIDE_CASTLE_COL] == null;
            }

            if (endCol == QUEENSIDE_CASTLE_COL) {
                return whiteCastleQueenSide
                        && isRookAt(startRow, ROOK_QUEENSIDE_COL, WHITE, ROOK)
                        && state[startRow][KNIGHT_QUEENSIDE_COL] == null
                        && state[startRow][QUEENSIDE_CASTLE_COL] == null
                        && state[startRow][QUEENSIDE_CASTLE_KING_PATH_COL] == null;
            }
        } else if (BLACK.equals(color)) {
            if (endCol == KINGSIDE_CASTLE_COL) {
                return blackCastleKingSide
                        && isRookAt(startRow, ROOK_KINGSIDE_COL, BLACK, ROOK)
                        && state[startRow][KINGSIDE_CASTLE_KING_PATH_COL] == null
                        && state[startRow][KINGSIDE_CASTLE_COL] == null;
            }

            if (endCol == QUEENSIDE_CASTLE_COL) {
                return blackCastleQueenSide
                        && isRookAt(startRow, ROOK_QUEENSIDE_COL, BLACK, ROOK)
                        && state[startRow][KNIGHT_QUEENSIDE_COL] == null
                        && state[startRow][QUEENSIDE_CASTLE_COL] == null
                        && state[startRow][QUEENSIDE_CASTLE_KING_PATH_COL] == null;
            }
        }

        return false;
    }

    private boolean isRookAt(int row, int col, String color, String type) {
        Piece p = state[row][col];
        return p != null && color.equals(p.getColor()) && type.equals(p.getType());
    }

    private void executeCastling(int startRow, int startCol, int endRow, int endCol) {
        Piece king = state[startRow][startCol];
        state[startRow][startCol] = null;

        if (endCol == KINGSIDE_CASTLE_COL) {
            Piece rook = state[startRow][ROOK_KINGSIDE_COL];
            state[startRow][ROOK_KINGSIDE_COL] = null;
            state[startRow][endCol] = king;
            state[startRow][KINGSIDE_CASTLE_KING_PATH_COL] = rook;
        } else {
            Piece rook = state[startRow][ROOK_QUEENSIDE_COL];
            state[startRow][ROOK_QUEENSIDE_COL] = null;
            state[startRow][endCol] = king;
            state[startRow][QUEENSIDE_CASTLE_KING_PATH_COL] = rook;
        }
    }

    private boolean isPawnPromotionRank(String color, int endRow) {
        if (WHITE.equals(color)) {
            return endRow == BLACK_BACK_ROW;
        }

        if (BLACK.equals(color)) {
            return endRow == WHITE_BACK_ROW;
        }

        return false;
    }

    private String resolvePromotionType(String promoTrim) {
        if (promoTrim.isEmpty()) {
            return QUEEN;
        }

        String upper = promoTrim.toUpperCase();
        if (QUEEN.equals(upper) || ROOK.equals(upper) || BISHOP.equals(upper) || KNIGHT.equals(upper)) {
            return upper;
        }

        return null;
    }

    private boolean isLegalKingMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDelta = Math.abs(endRow - startRow);
        int colDelta = Math.abs(endCol - startCol);

        return rowDelta <= KING_MOVE_LIMIT
                && colDelta <= KING_MOVE_LIMIT
                && (rowDelta != 0 || colDelta != 0);
    }

    private boolean isLegalQueenMove(int startRow, int startCol, int endRow, int endCol) {
        return isLegalRookMove(startRow, startCol, endRow, endCol)
                || isLegalBishopMove(startRow, startCol, endRow, endCol);
    }

    private boolean isLegalKnightMove(int startRow, int startCol, int endRow, int endCol) {
        int rowDelta = Math.abs(endRow - startRow);
        int colDelta = Math.abs(endCol - startCol);

        return (rowDelta == KNIGHT_LONG_MOVE && colDelta == KNIGHT_SHORT_MOVE)
                || (rowDelta == KNIGHT_SHORT_MOVE && colDelta == KNIGHT_LONG_MOVE);
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

        if (WHITE.equals(piece.getColor())) {
            direction = WHITE_PAWN_DIRECTION;
            startRank = WHITE_PAWN_ROW;
            opponentColor = BLACK;
        } else if (BLACK.equals(piece.getColor())) {
            direction = BLACK_PAWN_DIRECTION;
            startRank = BLACK_PAWN_ROW;
            opponentColor = WHITE;
        } else {
            return false;
        }

        int rowDelta = endRow - startRow;
        int colDelta = endCol - startCol;

        if (colDelta == 0 && destination == null) {
            if (rowDelta == direction) {
                return true;
            }

            if (rowDelta == PAWN_DOUBLE_MOVE_DISTANCE * direction && startRow == startRank) {
                return state[startRow + direction][startCol] == null;
            }
        }

        if (Math.abs(colDelta) == KING_MOVE_LIMIT && rowDelta == direction) {
            return destination != null && opponentColor.equals(destination.getColor());
        }

        return false;
    }

    private Piece[] generateBoundaryRows(String color) {
        Piece[] row = new Piece[BOARD_SIZE];

        row[ROOK_QUEENSIDE_COL] = new Piece(ROOK, color);
        row[KNIGHT_QUEENSIDE_COL] = new Piece(KNIGHT, color);
        row[BISHOP_QUEENSIDE_COL] = new Piece(BISHOP, color);
        row[QUEEN_START_COL] = new Piece(QUEEN, color);
        row[KING_START_COL] = new Piece(KING, color);
        row[BISHOP_KINGSIDE_COL] = new Piece(BISHOP, color);
        row[KNIGHT_KINGSIDE_COL] = new Piece(KNIGHT, color);
        row[ROOK_KINGSIDE_COL] = new Piece(ROOK, color);

        return row;
    }

    private Piece[] generatePawnRow(String color) {
        Piece[] row = new Piece[BOARD_SIZE];

        for (int col = 0; col < BOARD_SIZE; col++) {
            row[col] = new Piece(PAWN, color);
        }

        return row;
    }

    public Board copy() {
        Board copy = new Board();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
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

        if (piece == null || !PAWN.equals(piece.getType())) {
            return false;
        }

        if (capturedPawn == null
                || !PAWN.equals(capturedPawn.getType())
                || piece.getColor().equals(capturedPawn.getColor())) {
            return false;
        }

        if (state[end.getRow()][end.getCol()] != null) {
            return false;
        }

        int direction = WHITE.equals(piece.getColor()) ? WHITE_PAWN_DIRECTION : BLACK_PAWN_DIRECTION;
        if (end.getRow() - start.getRow() != direction) {
            return false;
        }

        return Math.abs(end.getCol() - start.getCol()) == KING_MOVE_LIMIT;
    }

    private void executeEnPassant(Position start, Position end, Position capturedPawnPosition) {
        Piece piece = state[start.getRow()][start.getCol()];

        state[end.getRow()][end.getCol()] = piece;
        state[start.getRow()][start.getCol()] = null;
        state[capturedPawnPosition.getRow()][capturedPawnPosition.getCol()] = null;
    }
}