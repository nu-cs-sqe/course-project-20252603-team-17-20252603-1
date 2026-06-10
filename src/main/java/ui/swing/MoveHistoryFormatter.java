package ui.swing;

import game.Move;

/**
 * Plain-text lines for the move history panel; rules stay in {@link game.Game}.
 */
public final class MoveHistoryFormatter {

	private MoveHistoryFormatter() {
	}

	/** One-based move index and move record → single log line (minimal form). */
	public static String formatLine(int moveNumberOneBased, Move m) {
		if (m.isCastling()) {
			String colorLetter = m.getColor().substring(0, 1);
			boolean queenSide = m.getEndCol() < m.getStartCol();
			String castle = queenSide ? "O-O-O" : "O-O";
			return moveNumberOneBased + ". " + colorLetter + " " + castle;
		}
		String colorLetter = m.getColor().substring(0, 1);
		String piece = abbrevPieceType(m.getPieceType());
		String from = coord(m.getStartRow(), m.getStartCol());
		String to = coord(m.getEndRow(), m.getEndCol());
		String sep = m.isCapture() ? "x" : "->";
		String line = moveNumberOneBased + ". " + colorLetter + " " + piece + " "
				+ from + sep + to;
		if (m.isEnPassant()) {
			line = line + " e.p.";
		}
		if (m.getPromotionPieceType() != null) {
			line = line + "=" + abbrevPieceType(m.getPromotionPieceType());
		}
		return line;
	}

	private static String abbrevPieceType(String type) {
		if ("KNIGHT".equals(type)) {
			return "N";
		}
		return type.substring(0, 1);
	}

	private static String coord(int row, int col) {
		return "(" + row + "," + col + ")";
	}
}
