package ui.swing;

import game.Game;
import ui.controller.GameController;

/**
 * Read-only strings for the Swing status chrome; chess rules stay in {@link Game}.
 */
public final class GameStatusTexts {

	private GameStatusTexts() {
	}

	/**
	 * Single-line summary for the primary status label (turn, check, game over).
	 */
	public static String primaryStatusLine(GameController controller) {
		Game g = controller.getGame();
		if (controller.isGameOver()) {
			if (g.isDraw()) {
				return "Draw by " + summarizeDrawReason(g.getDrawReason());
			}
			if (g.getWinnerColor() != null) {
				return "Checkmate — " + g.getWinnerColor() + " wins";
			}
			return "Game over";
		}
		String line = g.getCurrentPlayer().getColor() + " to move";
		if (g.isKingInCheck(g.getCurrentPlayer().getColor())) {
			line = line + " — check";
		}
		return line;
	}

	/**
	 * Short window caption mirroring the primary status line.
	 */
	public static String windowTitle(GameController controller) {
		return "Chess — " + primaryStatusLine(controller);
	}

	private static String summarizeDrawReason(String code) {
		if (code == null) {
			return "draw";
		}
		switch (code) {
			case "STALEMATE":
				return "stalemate";
			case "INSUFFICIENT_MATERIAL":
				return "insufficient material";
			case "FIFTY_MOVE":
				return "fifty-move rule";
			case "THREEFOLD_REPETITION":
				return "threefold repetition";
			default:
				return code.toLowerCase().replace('_', ' ');
		}
	}
}
