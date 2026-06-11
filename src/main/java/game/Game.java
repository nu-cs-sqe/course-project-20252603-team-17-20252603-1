package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import player.Player;
import board.Board;
import board.Piece;
import board.Position;




public class Game {

	private static final int BOARD_SIZE = 8;
	private static final int WHITE_HOME_ROW = 7;
	private static final int BLACK_HOME_ROW = 0;
	private static final int WHITE_EN_PASSANT_ROW = 3;
	private static final int BLACK_EN_PASSANT_ROW = 4;
	private static final int KING_START_COL = 4;
	private static final int QUEENSIDE_CASTLE_COL = 2;
	private static final int KINGSIDE_CASTLE_COL = 6;
	private static final int CASTLING_COL_DELTA = 2;
	private static final int PAWN_DOUBLE_MOVE_ROWS = 2;
	private static final int THREEFOLD_REPETITION_COUNT = 3;
	private static final int FIFTY_MOVE_RULE_HALF_MOVES = 100;

	private static final String WHITE = "WHITE";
	private static final String BLACK = "BLACK";

	private static final String KING = "KING";
	private static final String QUEEN = "QUEEN";
	private static final String ROOK = "ROOK";
	private static final String BISHOP = "BISHOP";
	private static final String KNIGHT = "KNIGHT";
	private static final String PAWN = "PAWN";


	private static final String DRAW_STALEMATE = "STALEMATE";
	private static final String DRAW_INSUFFICIENT_MATERIAL = "INSUFFICIENT_MATERIAL";
	private static final String DRAW_FIFTY_MOVE = "FIFTY_MOVE";
	private static final String DRAW_THREEFOLD_REPETITION = "THREEFOLD_REPETITION";

	private Player currentPlayer;
	private Player whitePlayer;
	private Player blackPlayer;

	private Board board;

	private boolean draw;
	private boolean gameOver;
	private String winnerColor;
	private String drawReason;

	private Piece lastMovedPiece;
	private Position lastMoveStart;
	private Position lastMoveEnd;

	private final List<Move> moveHistory = new ArrayList<>();

	private int halfmoveClock;

	private final List<String> positionRepetitionHistory = new ArrayList<>();

	public Game() {
		this.board = new Board();
		this.whitePlayer = new Player("White Player", WHITE);
		this.blackPlayer = new Player("Black Player", BLACK);
		this.currentPlayer = this.whitePlayer;
		clearGameState();
	}

	Game(Board board, Player whitePlayer, Player blackPlayer, Player currentPlayer) {
		this.board = board;
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		this.currentPlayer = currentPlayer;
		clearGameState();
	}

	private void clearGameState() {
		lastMovedPiece = null;
		lastMoveStart = null;
		lastMoveEnd = null;

		moveHistory.clear();
		positionRepetitionHistory.clear();

		gameOver = false;
		draw = false;
		winnerColor = null;
		drawReason = null;
		halfmoveClock = 0;
	}

	public void startNewGame() {
		board.initializeBoard();
		clearGameState();
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

	public String getDrawReason() {
		return drawReason;
	}

	public int getHalfmoveClock() {
		return halfmoveClock;
	}

	public String getPositionSignature(String sideToMoveNext) {
		return buildPositionSignature(sideToMoveNext);
	}

	public int getRepetitionHistorySize() {
		return positionRepetitionHistory.size();
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
		return makeMove(startRow, startCol, endRow, endCol, null);
	}

	/**
	 * Applies a move for the current player. For pawn moves to the promotion rank,
	 * {@code promotionPiece} selects the promoted type ({@code QUEEN}, {@code ROOK},
	 * {@code BISHOP}, {@code KNIGHT}); {@code null} uses the board default (queen).
	 */
	public boolean makeMove(int startRow, int startCol, int endRow, int endCol, String promotionPiece) {
		if (!canAttemptMove(startRow, startCol, endRow, endCol)) {
			return false;
		}

		Piece piece = board.getPieceAt(startRow, startCol);
		Piece destinationPiece = board.getPieceAt(endRow, endCol);

		boolean enPassantMove = isEnPassantMove(startRow, startCol, endRow, endCol, piece);

		if (wouldCaptureKing(destinationPiece)) {
			return false;
		}

		if (!isCastlingPathLegal(startRow, startCol, endRow, endCol, piece)) {
			return false;
		}

		if (wouldLeaveOwnKingInCheck(startRow, startCol, endRow, endCol, promotionPiece, enPassantMove)) {
			return false;
		}

		if (!applyMove(startRow, startCol, endRow, endCol, promotionPiece, enPassantMove)) {
			return false;
		}

		recordLastMove(piece, startRow, startCol, endRow, endCol);
		appendCompletedMove(piece, startRow, startCol, endRow, endCol, destinationPiece, enPassantMove);
		updateHalfmoveClock(piece, destinationPiece, enPassantMove);

		String opponentColor = WHITE.equals(currentPlayer.getColor()) ? BLACK : WHITE;

		if (isCheckmate(opponentColor)) {
			gameOver = true;
			winnerColor = currentPlayer.getColor();
			drawReason = null;
			return true;
		}

		if (isStalemate(opponentColor)) {
			gameOver = true;
			winnerColor = null;
			draw = true;
			drawReason = DRAW_STALEMATE;
			return true;
		}

		if (isDeadPositionInsufficientMaterial()) {
			gameOver = true;
			winnerColor = null;
			draw = true;
			drawReason = DRAW_INSUFFICIENT_MATERIAL;
			return true;
		}

		if (halfmoveClock >= FIFTY_MOVE_RULE_HALF_MOVES) {
			gameOver = true;
			winnerColor = null;
			draw = true;
			drawReason = DRAW_FIFTY_MOVE;
			return true;
		}

		appendRepetitionAndCheckThreefold(opponentColor);
		if (gameOver) {
			return true;
		}

		switchTurn();

		return true;
	}

	private boolean wouldLeaveOwnKingInCheck(int startRow, int startCol, int endRow, int endCol,
	                                         String promotionPiece, boolean enPassantMove) {
		Board simulatedBoard = board.copy();

		boolean moved;
		if (enPassantMove) {
			moved = simulatedBoard.movePieceEnPassant(
					new Position(startRow, startCol),
					new Position(endRow, endCol),
					new Position(startRow, endCol));
		} else {
			moved = simulatedBoard.movePiece(startRow, startCol, endRow, endCol, promotionPiece);
		}

		if (!moved) {
			return true;
		}

		return isKingInCheckOnBoard(simulatedBoard, currentPlayer.getColor());
	}

	private String buildPositionSignature(String sideToMoveNext) {
		if (board == null) {
			return "";
		}
		return board.piecePlacementKey() + "|" + board.castlingRightsKey() + "|" + sideToMoveNext;
	}

	private void appendRepetitionAndCheckThreefold(String sideToMoveNext) {
		String repSig = buildPositionSignature(sideToMoveNext);
		positionRepetitionHistory.add(repSig);
		if (Collections.frequency(positionRepetitionHistory, repSig) >= THREEFOLD_REPETITION_COUNT) {
			gameOver = true;
			winnerColor = null;
			draw = true;
			drawReason = "THREEFOLD_REPETITION";
		}
	}

	private void updateHalfmoveClock(Piece moved, Piece destinationBeforeMove,
			boolean enPassantMove) {
		boolean capture = enPassantMove || destinationBeforeMove != null;
		if (PAWN.equals(moved.getType()) || capture) {
			halfmoveClock = 0;
		} else {
			halfmoveClock++;
		}
	}

	private boolean isDeadPositionInsufficientMaterial() {
		int total = 0;
		int kings = 0;
		int minors = 0;
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				Piece p = board.getPieceAt(r, c);
				if (p == null) {
					continue;
				}
				total++;
				String t = p.getType();
				if (KING.equals(t)) {
					kings++;
				} else if (KNIGHT.equals(t) || BISHOP.equals(t)) {
					minors++;
				} else {
					return false;
				}
			}
		}
		if (total == 2 && kings == 2) {
			return true;
		}
		if (total == 3 && kings == 2 && minors == 1) {
			return true;
		}
		if (total == 4 && kings == 2 && minors == 2) {
			return isSameSquareColorBishopPair();
		}
		return false;
	}

	private boolean isSameSquareColorBishopPair() {
		Integer squareColor = null;
		int bishops = 0;
		for (int r = 0; r < BOARD_SIZE; r++) {
			for (int c = 0; c < BOARD_SIZE; c++) {
				Piece p = board.getPieceAt(r, c);
				if (p == null || !BISHOP.equals(p.getType())) {
					continue;
				}
				bishops++;
				int parity = (r + c) % 2;
				if (squareColor == null) {
					squareColor = parity;
				} else if (!squareColor.equals(parity)) {
					return false;
				}
			}
		}
		return bishops == 2;
	}

	private boolean isEnPassantMove(int startRow, int startCol, int endRow, int endCol, Piece piece) {
		if (!PAWN.equals(piece.getType()) || board.getPieceAt(endRow, endCol) != null) {
			return false;
		}

		int direction = WHITE.equals(piece.getColor()) ? -1 : 1;

		if (WHITE.equals(piece.getColor()) && startRow != WHITE_EN_PASSANT_ROW) {
			return false;
		}

		if (BLACK.equals(piece.getColor()) && startRow != BLACK_EN_PASSANT_ROW) {
			return false;
		}


		return endRow - startRow == direction
				&& Math.abs(endCol - startCol) == 1
				&& lastMovedPiece != null
				&& PAWN.equals(lastMovedPiece.getType())
				&& !piece.getColor().equals(lastMovedPiece.getColor())
				&& lastMoveStart != null
				&& lastMoveEnd != null
				&& Math.abs(lastMoveEnd.getRow() - lastMoveStart.getRow()) == PAWN_DOUBLE_MOVE_ROWS
				&& lastMoveEnd.getRow() == startRow
				&& lastMoveEnd.getCol() == endCol;
	}

	private void recordLastMove(Piece piece, int startRow, int startCol, int endRow, int endCol) {
		lastMovedPiece = piece;
		lastMoveStart = new Position(startRow, startCol);
		lastMoveEnd = new Position(endRow, endCol);
	}

	private void appendCompletedMove(Piece moved, int startRow, int startCol, int endRow, int endCol,
			Piece destinationBeforeMove, boolean enPassantMove) {
		boolean castling = isCastlingMove(startRow, startCol, endRow, endCol, moved);
		boolean capture = enPassantMove || destinationBeforeMove != null;
		String capturedType = null;
		if (enPassantMove) {
			capturedType = PAWN;
		} else if (destinationBeforeMove != null) {
			capturedType = destinationBeforeMove.getType();
		}
		String promotionPieceType = null;
		if (PAWN.equals(moved.getType()) && isPawnPromotionRank(moved.getColor(), endRow)) {
			Piece atDestination = board.getPieceAt(endRow, endCol);
			if (atDestination != null) {
				promotionPieceType = atDestination.getType();
			}
		}
		moveHistory.add(new Move(startRow, startCol, endRow, endCol,
				moved.getType(), moved.getColor(),
				capture, capturedType,
				castling, enPassantMove,
				promotionPieceType));
	}

	private boolean isPawnPromotionRank(String color, int endRow) {
		if (WHITE.equals(color)) {
			return endRow == BLACK_HOME_ROW;
		}
		if (BLACK.equals(color)) {
			return endRow == WHITE_HOME_ROW;
		}
		return false;
	}

	public List<Move> getMoveHistory() {
		return Collections.unmodifiableList(moveHistory);
	}



	private boolean isCastlingMove(int startRow, int startCol, int endRow, int endCol, Piece piece) {
		if (startRow != endRow
				|| Math.abs(endCol - startCol) != CASTLING_COL_DELTA
				|| startCol != KING_START_COL) {
			return false;
		}

		if (WHITE.equals(piece.getColor())) {
			return startRow == WHITE_HOME_ROW
					&& (endCol == QUEENSIDE_CASTLE_COL || endCol == KINGSIDE_CASTLE_COL);
		}

		if (BLACK.equals(piece.getColor())) {
			return startRow == BLACK_HOME_ROW
					&& (endCol == QUEENSIDE_CASTLE_COL || endCol == KINGSIDE_CASTLE_COL);
		}

		return false;
	}

	private boolean canAttemptMove(int startRow, int startCol, int endRow, int endCol) {
		if (gameOver || board == null || currentPlayer == null) {
			return false;
		}

		if (!board.isWithinBounds(startRow, startCol) || !board.isWithinBounds(endRow, endCol)) {
			return false;
		}

		Piece piece = board.getPieceAt(startRow, startCol);
		return piece != null && currentPlayer.getColor().equals(piece.getColor());
	}

	private boolean wouldCaptureKing(Piece destinationPiece) {
		return destinationPiece != null && KING.equals(destinationPiece.getType());
	}

	private boolean applyMove(int startRow, int startCol, int endRow, int endCol,
	                          String promotionPiece, boolean enPassantMove) {
		if (enPassantMove) {
			return board.movePieceEnPassant(
					new Position(startRow, startCol),
					new Position(endRow, endCol),
					new Position(startRow, endCol));
		}

		return board.movePiece(startRow, startCol, endRow, endCol, promotionPiece);
	}





	public boolean isKingInCheck(String color) {
		if (board == null) {
			return false;
		}

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Piece piece = board.getPieceAt(row, col);

				if (piece != null
						&& KING.equals(piece.getType())
						&& color.equals(piece.getColor())) {
					String opponentColor = WHITE.equals(color) ? BLACK : WHITE;
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

		for (int startRow = 0; startRow < BOARD_SIZE; startRow++) {
			for (int startCol = 0; startCol < BOARD_SIZE; startCol++) {
				Piece piece = board.getPieceAt(startRow, startCol);

				if (piece != null && byColor.equals(piece.getColor())) {
					if (ROOK.equals(piece.getType())
							&& rookAttacksSquare(startRow, startCol, row, col)) {
						return true;
					}

					if (BISHOP.equals(piece.getType())
							&& bishopAttacksSquare(startRow, startCol, row, col)) {
						return true;
					}

					if (KNIGHT.equals(piece.getType())
							&& knightAttacksSquare(startRow, startCol, row, col)) {
						return true;
					}

					if (QUEEN.equals(piece.getType())
							&& queenAttacksSquare(startRow, startCol, row, col)) {
						return true;
					}
					if (PAWN.equals(piece.getType())
							&& pawnAttacksSquare(startRow, startCol, row, col)) {
						return true;
					}

					if (KING.equals(piece.getType())
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
		if (WHITE.equals(pawn.getColor())) {
			direction = -1;
		} else if (BLACK.equals(pawn.getColor())) {
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

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				Piece piece = board.getPieceAt(row, col);

				if (piece != null
						&& KING.equals(piece.getType())
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

		Board simulatedBoard = board.copy();

		if (!simulatedBoard.movePiece(startRow, startCol, endRow, endCol)) {
			return false;
		}

		return !isKingInCheckOnBoard(simulatedBoard, color);
	}

	private boolean hasAnyLegalMove(String color) {
		if (board == null) {
			return false;
		}

		for (int startRow = 0; startRow < BOARD_SIZE; startRow++) {
			for (int startCol = 0; startCol < BOARD_SIZE; startCol++) {
				Piece piece = board.getPieceAt(startRow, startCol);

				if (piece == null || !color.equals(piece.getColor())) {
					continue;
				}

				for (int endRow = 0; endRow < BOARD_SIZE; endRow++) {
					for (int endCol = 0; endCol < BOARD_SIZE; endCol++) {
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
		Board simulatedBoard = board.copy();

		if (!simulatedBoard.movePiece(startRow, startCol, endRow, endCol)) {
			return false;
		}

		return !isKingInCheckOnBoard(simulatedBoard, color);
	}


	private boolean isCastlingPathLegal(int startRow, int startCol, int endRow, int endCol, Piece piece) {
		if (!isCastlingMove(startRow, startCol, endRow, endCol, piece)) {
			return true;
		}

		if (isKingInCheck(currentPlayer.getColor())) {
			return false;
		}

		int midCol = startCol + Integer.signum(endCol - startCol);
		Board throughProbe = board.copy();

		if (!throughProbe.movePiece(startRow, startCol, startRow, midCol)) {
			return false;
		}

		return !isKingInCheckOnBoard(throughProbe, currentPlayer.getColor());
	}




	public boolean isGameOver() {
		return gameOver;
	}

	private boolean isKingInCheckOnBoard(Board boardToCheck, String color) {
		Board originalBoard = board;
		board = boardToCheck;

		boolean inCheck = isKingInCheck(color);

		board = originalBoard;
		return inCheck;
	}
}
