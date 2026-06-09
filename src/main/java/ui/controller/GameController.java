package ui.controller;

import game.Game;

/**
 * Thin MVC controller: owns the {@link Game} model and delegates moves.
 * Views read state through {@link #getGame()}; they must not implement chess rules.
 */
public class GameController {

	private final Game game = new Game();

	public GameController() {
		game.startNewGame();
	}

	public Game getGame() {
		return game;
	}

	/** Resets the model to a fresh standard game (same entry as CLI new games). */
	public void startNewGame() {
		game.startNewGame();
	}

	public boolean tryMove(int startRow, int startCol, int endRow, int endCol) {
		return game.makeMove(startRow, startCol, endRow, endCol);
	}

	public boolean isGameOver() {
		return game.isGameOver();
	}

	public String getWinnerColor() {
		return game.getWinnerColor();
	}
}
