package ui.swing;

import javax.swing.JOptionPane;
import java.awt.Component;

/**
 * Swing UI for selecting a promotion piece; does not implement chess rules.
 */
public final class PromotionChooser {

	private PromotionChooser() {
	}

	/**
	 * @return {@code QUEEN}, {@code ROOK}, {@code BISHOP}, or {@code KNIGHT}, or {@code null} if the
	 *         player cancels or closes the dialog.
	 */
	public static String choosePromotionType(Component parent) {
		String[] options = {
				"Queen (default)",
				"Rook",
				"Bishop",
				"Knight",
				"Cancel"
		};
		int choice = JOptionPane.showOptionDialog(
				parent,
				"Pawn reached the last rank — choose promotion:",
				"Pawn promotion",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		if (choice < 0 || choice == 4) {
			return null;
		}
		switch (choice) {
			case 0:
				return "QUEEN";
			case 1:
				return "ROOK";
			case 2:
				return "BISHOP";
			case 3:
				return "KNIGHT";
			default:
				return null;
		}
	}
}
