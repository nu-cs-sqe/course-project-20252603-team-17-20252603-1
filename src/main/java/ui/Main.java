package ui;

import ui.console.ConsoleChessRunner;
import ui.controller.GameController;
import ui.swing.ChessWindow;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
			SwingUtilities.invokeLater(() -> {
				ChessWindow window = new ChessWindow(new GameController());
				window.setVisible(true);
			});
		} else {
			GameController controller = new GameController();
			new ConsoleChessRunner(controller).run();
		}
	}
}
