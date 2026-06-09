package ui.swing;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;

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
}
