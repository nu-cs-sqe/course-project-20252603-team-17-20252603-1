package ui.swing;

import game.Game;
import game.Move;
import ui.controller.GameController;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

/**
 * Read-only scrollable log of completed moves; filled from {@link game.Game#getMoveHistory()}.
 */
public class MoveHistoryView extends JPanel {

	private static final int PREFERRED_WIDTH = 220;

	private final JTextArea textArea;
	private final JScrollPane scrollPane;

	public MoveHistoryView() {
		super(new BorderLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(false);
		textArea.setWrapStyleWord(false);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		textArea.setText("");
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, 120));
		scrollPane.setBorder(BorderFactory.createTitledBorder("Move history"));
		add(scrollPane, BorderLayout.CENTER);
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
		if (history.isEmpty()) {
			textArea.setText("(No moves yet.)");
			textArea.setCaretPosition(0);
			return;
		}
		StringBuilder sb = new StringBuilder();
		int n = 1;
		for (Move m : history) {
			sb.append(MoveHistoryFormatter.formatLine(n, m)).append('\n');
			n++;
		}
		textArea.setText(sb.toString());
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
