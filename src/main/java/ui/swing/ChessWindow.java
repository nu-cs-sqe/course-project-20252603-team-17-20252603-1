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

	private static final int DEFAULT_WIDTH = 960;
	private static final int DEFAULT_HEIGHT = 640;

	private final GameController controller;
	private final UiMessages messages;

	private final JLabel statusLabel;
	private final JLabel errorLabel;

	private final MoveHistoryView moveHistoryView;

	public ChessWindow(GameController controller) {
		super();
		if (controller == null) {
			throw new IllegalArgumentException("controller");
		}
		this.controller = controller;
		this.messages = new UiMessages();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setLocationRelativeTo(null);
		setTitle(messages.get("app.title"));

		String turn = controller.getGame().getCurrentPlayer().getColor();
		String openingStatus = messages.format(
				"status.to.move",
				messages.playerColorName(turn));
		statusLabel = new JLabel(openingStatus, SwingConstants.CENTER);
		errorLabel = new JLabel("", SwingConstants.CENTER);

		moveHistoryView = new MoveHistoryView(messages);

		final BoardPanel boardPanel = new BoardPanel(
				controller,
				messages,
				statusLabel,
				errorLabel,
				this::syncWindowTitle,
				() -> moveHistoryView.refreshFrom(controller));

		JButton newGameButton = new JButton(messages.get("button.new.game"));
		newGameButton.addActionListener(e -> {
			controller.startNewGame();
			boardPanel.resetUiAfterNewGame();
			moveHistoryView.refreshFrom(controller);
		});

		JPanel northStrip = new JPanel(new BorderLayout());
		northStrip.add(statusLabel, BorderLayout.CENTER);
		northStrip.add(newGameButton, BorderLayout.EAST);
		add(northStrip, BorderLayout.NORTH);

		add(errorLabel, BorderLayout.SOUTH);

		add(moveHistoryView, BorderLayout.EAST);

		add(boardPanel, BorderLayout.CENTER);
		syncWindowTitle();
		moveHistoryView.refreshFrom(controller);
	}

	private void syncWindowTitle() {
		setTitle(GameStatusTexts.windowTitle(controller, messages));
	}
}
