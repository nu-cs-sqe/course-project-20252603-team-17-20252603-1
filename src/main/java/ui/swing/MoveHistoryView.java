package ui.swing;

import game.Game;
import game.Move;
import ui.controller.GameController;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

/**
 * Read-only scrollable log of completed moves; filled from {@link game.Game#getMoveHistory()}.
 */
public class MoveHistoryView extends JPanel {

	private static final int PREFERRED_WIDTH = 220;

	private final JTextArea textArea;

	public MoveHistoryView() {
		super(new BorderLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setText("");
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setPreferredSize(new Dimension(PREFERRED_WIDTH, 120));
		add(scroll, BorderLayout.CENTER);
	}

	/** Rebuilds the log from the controller's game (no moves if no game). */
	public void refreshFrom(GameController controller) {
		if (controller == null) {
			textArea.setText("");
			return;
		}
		Game g = controller.getGame();
		if (g == null) {
			textArea.setText("");
			return;
		}
		List<Move> history = g.getMoveHistory();
		StringBuilder sb = new StringBuilder();
		int n = 1;
		for (Move m : history) {
			sb.append(MoveHistoryFormatter.formatLine(n, m)).append('\n');
			n++;
		}
		textArea.setText(sb.toString());
	}
}
