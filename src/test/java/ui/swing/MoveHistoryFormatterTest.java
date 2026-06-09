package ui.swing;

import game.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveHistoryFormatterTest {

	@Test
	void castlingKingsideFormatsOO() {
		Move m = new Move(7, 4, 7, 6, "KING", "WHITE",
				false, null, true, false, null);
		assertEquals("1. W O-O", MoveHistoryFormatter.formatLine(1, m));
	}

	@Test
	void castlingQueensideFormatsOOO() {
		Move m = new Move(0, 4, 0, 2, "KING", "BLACK",
				false, null, true, false, null);
		assertEquals("2. B O-O-O", MoveHistoryFormatter.formatLine(2, m));
	}

	@Test
	void captureUsesXSeparator() {
		Move m = new Move(4, 4, 3, 4, "PAWN", "WHITE",
				true, "PAWN", false, false, null);
		String line = MoveHistoryFormatter.formatLine(3, m);
		assertTrue(line.contains("x(3,4)"));
	}

	@Test
	void enPassantAppendsEpSuffix() {
		Move m = new Move(3, 4, 2, 3, "PAWN", "WHITE",
				true, "PAWN", false, true, null);
		String line = MoveHistoryFormatter.formatLine(4, m);
		assertTrue(line.endsWith(" e.p."));
	}

	@Test
	void promotionAppendsEqualsPieceLetter() {
		Move m = new Move(1, 2, 0, 2, "PAWN", "BLACK",
				false, null, false, false, "KNIGHT");
		String line = MoveHistoryFormatter.formatLine(5, m);
		assertTrue(line.contains("=N"));
	}
}
