package ui.swing;

import ui.controller.GameController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class ChessWindow extends JFrame {

	private static final int DEFAULT_WIDTH = 720;
	private static final int DEFAULT_HEIGHT = 640;

	private final GameController controller;

	private final JLabel statusLabel;
	private final JLabel errorLabel;

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
		errorLabel = new JLabel("", SwingConstants.CENTER);

		final BoardPanel boardPanel = new BoardPanel(controller, statusLabel, errorLabel, this::syncWindowTitle);

		JButton newGameButton = new JButton("New game");
		newGameButton.addActionListener(e -> {
			controller.startNewGame();
			boardPanel.resetUiAfterNewGame();
		});

		JPanel northStrip = new JPanel(new BorderLayout());
		northStrip.add(statusLabel, BorderLayout.CENTER);
		northStrip.add(newGameButton, BorderLayout.EAST);
		add(northStrip, BorderLayout.NORTH);

		add(errorLabel, BorderLayout.SOUTH);

		add(boardPanel, BorderLayout.CENTER);
		syncWindowTitle();
	}

	private void syncWindowTitle() {
		setTitle(GameStatusTexts.windowTitle(controller));
	}
}
