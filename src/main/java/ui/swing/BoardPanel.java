package ui.swing;

import board.Board;
import board.Piece;
import ui.controller.GameController;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Swing surface for drawing the chess board; model access via {@link GameController} only.
 */
public class BoardPanel extends JPanel {

	private static final int PREFERRED_PX = 400;

	private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
	private static final Color DARK_SQUARE = new Color(181, 136, 99);

	private final GameController controller;
	private final JLabel statusLine;
	private final JLabel errorLine;
	private final Runnable afterStatusSync;

	private Integer selectedRow;
	private Integer selectedCol;

	public BoardPanel(GameController controller, JLabel statusLine, JLabel errorLine,
			Runnable afterStatusSync) {
		if (controller == null) {
			throw new IllegalArgumentException("controller");
		}
		if (statusLine == null) {
			throw new IllegalArgumentException("statusLine");
		}
		if (errorLine == null) {
			throw new IllegalArgumentException("errorLine");
		}
		this.controller = controller;
		this.statusLine = statusLine;
		this.errorLine = errorLine;
		this.afterStatusSync = afterStatusSync;
		setOpaque(true);
		setBackground(new Color(220, 220, 220));
		setPreferredSize(new Dimension(PREFERRED_PX, PREFERRED_PX));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				onBoardMousePressed(e.getX(), e.getY());
			}
		});
	}

	private void onBoardMousePressed(int px, int py) {
		BoardGeometry geo = BoardGeometry.fromSize(getWidth(), getHeight());
		int[] sq = pixelToSquare(px, py, geo);
		if (sq == null || controller.isGameOver()) {
			return;
		}

		int row = sq[0];
		int col = sq[1];

		if (selectedRow != null) {
			if (selectedRow == row && selectedCol == col) {
				clearSelection();
				syncStatusFromGame();
				repaint();
				return;
			}
			boolean moved = controller.tryMove(selectedRow, selectedCol, row, col);
			if (moved) {
				clearSelection();
				syncStatusFromGame();
			} else {
				errorLine.setText("Invalid move. Try again.");
			}
			repaint();
			return;
		}

		Piece piece = controller.getGame().getBoard().getPieceAt(row, col);
		if (piece == null) {
			errorLine.setText("");
			return;
		}
		String turn = controller.getGame().getCurrentPlayer().getColor();
		if (!turn.equals(piece.getColor())) {
			errorLine.setText("");
			return;
		}
		selectedRow = row;
		selectedCol = col;
		syncStatusFromGame();
		repaint();
	}

	private void clearSelection() {
		selectedRow = null;
		selectedCol = null;
	}

	private void syncStatusFromGame() {
		errorLine.setText("");
		statusLine.setText(GameStatusTexts.primaryStatusLine(controller));
		if (afterStatusSync != null) {
			afterStatusSync.run();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		BoardGeometry geo = BoardGeometry.fromSize(getWidth(), getHeight());

		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				boolean light = (row + col) % 2 == 0;
				g2.setColor(light ? LIGHT_SQUARE : DARK_SQUARE);
				int x = geo.originX() + col * geo.square();
				int y = geo.originY() + row * geo.square();
				g2.fillRect(x, y, geo.square(), geo.square());
			}
		}

		drawPieces(g2, geo.originX(), geo.originY(), geo.square());

		g2.dispose();
	}

	/**
	 * @return {@code {row, col}} for the board square under the pixel, or {@code null} if outside.
	 */
	private int[] pixelToSquare(int px, int py, BoardGeometry geo) {
		int edgeX = geo.originX() + 8 * geo.square();
		int edgeY = geo.originY() + 8 * geo.square();
		if (px < geo.originX() || px >= edgeX || py < geo.originY() || py >= edgeY) {
			return null;
		}
		int col = (px - geo.originX()) / geo.square();
		int row = (py - geo.originY()) / geo.square();
		return new int[] {row, col};
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

	/**
	 * Pixel layout of the centered 8×8 board (shared by painting and future hit-testing).
	 */
	private static final class BoardGeometry {

		private static final int PAD = 8;

		private final int originX;
		private final int originY;
		private final int square;

		private BoardGeometry(int originX, int originY, int square) {
			this.originX = originX;
			this.originY = originY;
			this.square = square;
		}

		static BoardGeometry fromSize(int width, int height) {
			int innerW = width - 2 * PAD;
			int innerH = height - 2 * PAD;
			int square = Math.max(1, Math.min(innerW, innerH) / 8);
			int boardPx = square * 8;
			int ox = (width - boardPx) / 2;
			int oy = (height - boardPx) / 2;
			return new BoardGeometry(ox, oy, square);
		}

		int originX() {
			return originX;
		}

		int originY() {
			return originY;
		}

		int square() {
			return square;
		}
	}
}
