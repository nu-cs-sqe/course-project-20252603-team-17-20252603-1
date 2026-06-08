package ui;

import ui.console.ConsoleChessRunner;
import ui.controller.GameController;

public class Main {

	public static void main(String[] args) {
		GameController controller = new GameController();
		new ConsoleChessRunner(controller).run();
	}
}
