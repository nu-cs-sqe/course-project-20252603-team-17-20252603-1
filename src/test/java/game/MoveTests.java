package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoveTests {

	@Test
	void quietPawnMoveStoresSquaresPieceAndColor() {
		Move m = new Move(6, 4, 5, 4, "PAWN", "WHITE",
				false, null, false, false, null);
		assertEquals(6, m.getStartRow());
		assertEquals(4, m.getStartCol());
		assertEquals(5, m.getEndRow());
		assertEquals(4, m.getEndCol());
		assertEquals("PAWN", m.getPieceType());
		assertEquals("WHITE", m.getColor());
		assertFalse(m.isCapture());
		assertNull(m.getCapturedPieceType());
		assertFalse(m.isCastling());
		assertFalse(m.isEnPassant());
		assertNull(m.getPromotionPieceType());
	}

	@Test
	void equalsReturnsTrueForSameObject() {
		Move move = new Move(1, 2, 3, 4,
				"PAWN", "WHITE",
				false, null,
				false, false,
				null);

		assertEquals(move, move);
	}

	@Test
	void equalsReturnsFalseForNullAndDifferentClass() {
		Move move = new Move(1, 2, 3, 4,
				"PAWN", "WHITE",
				false, null,
				false, false,
				null);

		assertNotEquals(null, move);
		assertNotEquals("not a move", move);
	}

	@Test
	void hashCodeDiffersForDifferentMoves() {
		Move first = new Move(1, 2, 3, 4,
				"PAWN", "WHITE",
				false, null,
				false, false,
				null);

		Move second = new Move(1, 2, 3, 5,
				"PAWN", "WHITE",
				false, null,
				false, false,
				null);

		assertNotEquals(first.hashCode(), second.hashCode());
	}

	@Test
	void captureStoresCapturedType() {
		Move m = new Move(4, 4, 3, 4, "PAWN", "WHITE",
				true, "PAWN", false, false, null);
		assertTrue(m.isCapture());
		assertEquals("PAWN", m.getCapturedPieceType());
	}

	@Test
	void promotionStoresResultType() {
		Move m = new Move(1, 2, 0, 2, "PAWN", "BLACK",
				false, null, false, false, "QUEEN");
		assertEquals("QUEEN", m.getPromotionPieceType());
	}

	@Test
	void castlingAndEnPassantFlags() {
		Move castle = new Move(7, 4, 7, 6, "KING", "WHITE",
				false, null, true, false, null);
		assertTrue(castle.isCastling());

		Move ep = new Move(3, 4, 2, 3, "PAWN", "WHITE",
				true, "PAWN", false, true, null);
		assertTrue(ep.isEnPassant());
		assertTrue(ep.isCapture());
	}

	@Test
	void equalsAndHashCodeSameForEqualMoves() {
		Move a = new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, false, false, null);
		Move b = new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, false, false, null);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void equalsFalseWhenAnyFieldDiffers() {
		Move base = new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, false, false, null);
		assertNotEquals(base, new Move(6, 1, 5, 0, "PAWN", "WHITE",
				false, null, false, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 1, "PAWN", "WHITE",
				false, null, false, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "ROOK", "WHITE",
				false, null, false, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "PAWN", "BLACK",
				false, null, false, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "PAWN", "WHITE",
				true, "PAWN", false, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, true, false, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, false, true, null));
		assertNotEquals(base, new Move(6, 0, 5, 0, "PAWN", "WHITE",
				false, null, false, false, "QUEEN"));
	}

	@Test
	void nullPieceTypeRejected() {
		assertThrows(NullPointerException.class, () -> new Move(0, 0, 0, 1,
				null, "WHITE", false, null, false, false, null));
	}

	@Test
	void nullColorRejected() {
		assertThrows(NullPointerException.class, () -> new Move(0, 0, 0, 1,
				"PAWN", null, false, null, false, false, null));
	}
}
