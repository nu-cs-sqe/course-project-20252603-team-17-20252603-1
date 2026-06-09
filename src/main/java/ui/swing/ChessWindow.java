package ui.swing;

import ui.controller.GameController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class ChessWindow extends JFrame {

	private static final int DEFAULT_WIDTH = 720;
	private static final int DEFAULT_HEIGHT = 640;

	private final GameController controller;

	private final JLabel statusLabel;

	public ChessWindow(GameController controller) {
		super("Chess");
		if (controller == null) {
			throw new IllegalArgumentException("controller");
		}
		this.controller = controller;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);

		String turn = controller.getGame().getCurrentPlayer().getColor();
		statusLabel = new JLabel(turn + " to move", SwingConstants.CENTER);
		add(statusLabel, BorderLayout.NORTH);

		add(new BoardPanel(controller, statusLabel), BorderLayout.CENTER);
	}
}
