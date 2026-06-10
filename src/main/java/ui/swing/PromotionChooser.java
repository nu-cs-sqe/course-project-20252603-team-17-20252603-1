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
	 * @return model token {@code QUEEN}, {@code ROOK}, {@code BISHOP}, or {@code KNIGHT};
	 *         {@code null} if the player cancels or closes the dialog.
	 */
	public static String choosePromotionType(Component parent, UiMessages messages) {
		String[] options = {
				messages.get("promotion.queen"),
				messages.get("promotion.rook"),
				messages.get("promotion.bishop"),
				messages.get("promotion.knight"),
				messages.get("promotion.cancel")
		};
		int choice = JOptionPane.showOptionDialog(
				parent,
				messages.get("promotion.message"),
				messages.get("promotion.title"),
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
