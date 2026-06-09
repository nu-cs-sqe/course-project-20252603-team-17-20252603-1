package ui.swing;

import board.Board;
import board.Piece;
import ui.controller.GameController;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Swing surface for drawing the chess board; model access via {@link GameController} only.
 */
public class BoardPanel extends JPanel {

	private static final int PREFERRED_PX = 400;

	private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
	private static final Color DARK_SQUARE = new Color(181, 136, 99);

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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int w = getWidth();
		int h = getHeight();
		int pad = 8;
		int innerW = w - 2 * pad;
		int innerH = h - 2 * pad;
		int square = Math.max(1, Math.min(innerW, innerH) / 8);
		int boardPx = square * 8;
		int ox = (w - boardPx) / 2;
		int oy = (h - boardPx) / 2;

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				boolean light = (row + col) % 2 == 0;
				g2.setColor(light ? LIGHT_SQUARE : DARK_SQUARE);
				int x = ox + col * square;
				int y = oy + row * square;
				g2.fillRect(x, y, square, square);
			}
		}

		drawPieces(g2, ox, oy, square);

		g2.dispose();
	}

	private void drawPieces(Graphics2D g2, int ox, int oy, int square) {
		Board board = controller.getGame().getBoard();
		int fontPx = Math.max(10, square * 5 / 10);
		g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontPx));
		FontMetrics fm = g2.getFontMetrics();

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				Piece piece = board.getPieceAt(row, col);
				if (piece == null) {
					continue;
				}
				boolean light = (row + col) % 2 == 0;
				g2.setColor(light ? new Color(40, 40, 40) : new Color(250, 250, 250));
				String label = pieceLabel(piece);
				int cx = ox + col * square + (square - fm.stringWidth(label)) / 2;
				int cy = oy + row * square + (square - fm.getHeight()) / 2 + fm.getAscent();
				g2.drawString(label, cx, cy);
			}
		}
	}

	private static String pieceLabel(Piece piece) {
		String colorPrefix = "WHITE".equals(piece.getColor()) ? "W" : "B";
		String type = piece.getType();
		if ("KNIGHT".equals(type)) {
			return colorPrefix + "N";
		}
		return colorPrefix + type.charAt(0);
	}

	protected GameController getController() {
		return controller;
	}
}
