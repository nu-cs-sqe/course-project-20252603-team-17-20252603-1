package ui.swing;

import ui.controller.GameController;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Swing surface for drawing the chess board; model access via {@link GameController} only.
 */
public class BoardPanel extends JPanel {

	private static final int PREFERRED_PX = 400;

	private final GameController controller;

	public BoardPanel(GameController controller) {
		if (controller == null) {
			throw new IllegalArgumentException("controller");
		}
		this.controller = controller;
		setOpaque(true);
		setBackground(new Color(220, 220, 220));
		setPreferredSize(new Dimension(PREFERRED_PX, PREFERRED_PX));
	}

	protected GameController getController() {
		return controller;
	}
}
