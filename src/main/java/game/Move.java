package game;

import java.util.Objects;

/**
 * Immutable description of a single completed legal move.
 * Intended for move history and draw logic (half-move clock, repetition).
 */
public final class Move {

	private final int startRow;
	private final int startCol;
	private final int endRow;
	private final int endCol;
	private final String pieceType;
	private final String color;
	private final boolean capture;
	private final String capturedPieceType;
	private final boolean castling;
	private final boolean enPassant;
	private final String promotionPieceType;

	public Move(int startRow, int startCol, int endRow, int endCol,
			String pieceType, String color,
			boolean capture, String capturedPieceType,
			boolean castling, boolean enPassant,
			String promotionPieceType) {
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
		this.pieceType = Objects.requireNonNull(pieceType, "pieceType");
		this.color = Objects.requireNonNull(color, "color");
		this.capture = capture;
		this.capturedPieceType = capturedPieceType;
		this.castling = castling;
		this.enPassant = enPassant;
		this.promotionPieceType = promotionPieceType;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getEndCol() {
		return endCol;
	}

	public String getPieceType() {
		return pieceType;
	}

	public String getColor() {
		return color;
	}

	public boolean isCapture() {
		return capture;
	}

	/**
	 * Type of the piece removed from the board, if any.
	 * For en passant this is the captured pawn even though the destination square was empty.
	 */
	public String getCapturedPieceType() {
		return capturedPieceType;
	}

	public boolean isCastling() {
		return castling;
	}

	public boolean isEnPassant() {
		return enPassant;
	}

	/**
	 * Piece type after promotion (e.g. QUEEN), or null if this move was not a promotion.
	 */
	public String getPromotionPieceType() {
		return promotionPieceType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Move move = (Move) o;
		return startRow == move.startRow
				&& startCol == move.startCol
				&& endRow == move.endRow
				&& endCol == move.endCol
				&& capture == move.capture
				&& castling == move.castling
				&& enPassant == move.enPassant
				&& pieceType.equals(move.pieceType)
				&& color.equals(move.color)
				&& Objects.equals(capturedPieceType, move.capturedPieceType)
				&& Objects.equals(promotionPieceType, move.promotionPieceType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(startRow, startCol, endRow, endCol, pieceType, color, capture,
				capturedPieceType, castling, enPassant, promotionPieceType);
	}
}
