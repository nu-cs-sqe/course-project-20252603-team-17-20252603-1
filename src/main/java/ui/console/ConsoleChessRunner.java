package ui.console;

import board.Board;
import board.Piece;
import game.Game;
import ui.controller.GameController;

import java.util.Scanner;

/**
 * Terminal-based view + input loop; chess rules stay in {@link Game}.
 */
public class ConsoleChessRunner {

	private final GameController controller;

	public ConsoleChessRunner(GameController controller) {
		this.controller = controller;
	}

	public void run() {
		Game game = controller.getGame();
		Scanner scanner = new Scanner(System.in);

		System.out.println("Simplified Chess");
		System.out.println("Enter moves as: startRow startCol endRow endCol");
		System.out.println("Example: 6 0 4 0");
		System.out.println();

		while (!controller.isGameOver()) {
			printBoard(game.getBoard());

			System.out.println(game.getCurrentPlayer().getColor() + "'s turn.");
			System.out.print("Move: ");

			int startRow = scanner.nextInt();
			int startCol = scanner.nextInt();
			int endRow = scanner.nextInt();
			int endCol = scanner.nextInt();

			boolean moved = controller.tryMove(startRow, startCol, endRow, endCol);

			if (!moved) {
				System.out.println("Invalid move. Try again.");
			}

			System.out.println();
		}

		printBoard(game.getBoard());
		System.out.println("Game over!");
		System.out.println("Winner: " + controller.getWinnerColor());
	}

	private static void printBoard(Board board) {
		System.out.println("   0  1  2  3  4  5  6  7");

		for (int row = 0; row < 8; row++) {
			System.out.print(row + "  ");

			for (int col = 0; col < 8; col++) {
				Piece piece = board.getPieceAt(row, col);

				if (piece == null) {
					System.out.print(".  ");
				} else {
					System.out.print(getPieceSymbol(piece) + " ");
				}
			}

			System.out.println();
		}

		System.out.println();
	}

	private static String getPieceSymbol(Piece piece) {
		String colorPrefix = piece.getColor().equals("WHITE") ? "W" : "B";
		String type = piece.getType();

		if (type.equals("KNIGHT")) {
			return colorPrefix + "N";
		}

		return colorPrefix + type.charAt(0);
	}
}
