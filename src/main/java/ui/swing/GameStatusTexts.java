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
	public static String primaryStatusLine(GameController controller, UiMessages messages) {
		Game g = controller.getGame();
		if (controller.isGameOver()) {
			if (g.isDraw()) {
				String reason = messages.drawReasonLabel(g.getDrawReason());
				return messages.format("status.draw.by", reason);
			}
			if (g.getWinnerColor() != null) {
				String winner = messages.playerColorName(g.getWinnerColor());
				return messages.format("status.checkmate", winner);
			}
			return messages.get("status.game.over");
		}
		String current = messages.playerColorName(g.getCurrentPlayer().getColor());
		String line = messages.format("status.to.move", current);
		if (g.isKingInCheck(g.getCurrentPlayer().getColor())) {
			line = line + messages.get("status.check.suffix");
		}
		return line;
	}

	/**
	 * Short window caption mirroring the primary status line.
	 */
	public static String windowTitle(GameController controller, UiMessages messages) {
		return messages.format("window.suffix", primaryStatusLine(controller, messages));
	}
}
