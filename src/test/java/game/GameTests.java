package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import board.Board;
import board.Piece;
import player.Player;

class GameTest {

    private Player whitePlayer;
    private Player blackPlayer;

    @BeforeEach
    void setUp() {
        whitePlayer = EasyMock.niceMock(Player.class);
        blackPlayer = EasyMock.niceMock(Player.class);

        EasyMock.expect(whitePlayer.getColor()).andStubReturn("WHITE");
        EasyMock.expect(blackPlayer.getColor()).andStubReturn("BLACK");

        EasyMock.replay(whitePlayer, blackPlayer);
    }

    @Test
    void startNewGameInitializesBoardAndResetsState() {
        Board board = EasyMock.niceMock(Board.class);

        board.initializeBoard();
        EasyMock.expectLastCall().once();

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);
        game.startNewGame();

        assertSame(board, game.getBoard());
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
        assertFalse(game.isGameOver());
        assertFalse(game.isDraw());
        assertNull(game.getWinnerColor());
        assertNull(game.getDrawReason());
        assertEquals(0, game.getHalfmoveClock());

        EasyMock.verify(board);
    }

    @Test
    void getBoardReturnsInjectedBoard() {
        Board board = EasyMock.niceMock(Board.class);
        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertSame(board, game.getBoard());
    }

    @Test
    void switchTurnChangesWhiteToBlack() {
        Board board = EasyMock.niceMock(Board.class);
        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertEquals("WHITE", game.getCurrentPlayer().getColor());

        game.switchTurn();

        assertEquals("BLACK", game.getCurrentPlayer().getColor());
    }

    @Test
    void switchTurnChangesBlackToWhite() {
        Board board = EasyMock.niceMock(Board.class);
        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        game.switchTurn();
        game.switchTurn();

        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenBoardIsNull() {
        Game game = new Game(null, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 5, 0));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenStartIsOutOfBounds() {
        Board board = EasyMock.niceMock(Board.class);

        EasyMock.expect(board.isWithinBounds(-1, 0)).andReturn(false);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(-1, 0, 0, 0));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenEndIsOutOfBounds() {
        Board board = EasyMock.niceMock(Board.class);

        EasyMock.expect(board.isWithinBounds(6, 0)).andReturn(true);
        EasyMock.expect(board.isWithinBounds(8, 0)).andReturn(false);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 8, 0));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenStartSquareIsEmpty() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(4, 4, 5, 4));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenMovingOpponentPiece() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(1, 0), new Piece("PAWN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(1, 0, 2, 0));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenDestinationHasKing() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(6, 0), new Piece("ROOK", "WHITE"));
        state.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 0, 4));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenSimulatedMoveFails() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(6, 0), new Piece("PAWN", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.expect(board.copy()).andReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 3, 0, null)).andReturn(false);

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 3, 0));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveReturnsTrueAndSwitchesTurnWhenBoardAcceptsMove() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);
        Board legalMoveProbe = EasyMock.niceMock(Board.class);

        Map<String, Piece> realState = new HashMap<>();
        realState.put(key(7, 4), new Piece("KING", "WHITE"));
        realState.put(key(0, 4), new Piece("KING", "BLACK"));
        realState.put(key(6, 0), new Piece("PAWN", "WHITE"));
        realState.put(key(1, 0), new Piece("PAWN", "BLACK"));
        realState.put(key(0, 0), new Piece("ROOK", "BLACK"));

        Map<String, Piece> simulatedState = new HashMap<>();
        simulatedState.put(key(7, 4), new Piece("KING", "WHITE"));
        simulatedState.put(key(0, 4), new Piece("KING", "BLACK"));
        simulatedState.put(key(5, 0), new Piece("PAWN", "WHITE"));
        simulatedState.put(key(1, 0), new Piece("PAWN", "BLACK"));
        simulatedState.put(key(0, 0), new Piece("ROOK", "BLACK"));

        Map<String, Piece> legalProbeState = new HashMap<>();
        legalProbeState.put(key(7, 4), new Piece("KING", "WHITE"));
        legalProbeState.put(key(0, 4), new Piece("KING", "BLACK"));
        legalProbeState.put(key(0, 1), new Piece("ROOK", "BLACK"));
        legalProbeState.put(key(5, 0), new Piece("PAWN", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, realState);

        EasyMock.expect(board.copy()).andReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andReturn(true);
        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, simulatedState);

        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andAnswer(() -> {
            realState.remove(key(6, 0));
            realState.put(key(5, 0), new Piece("PAWN", "WHITE"));
            return true;
        });

        EasyMock.expect(board.copy()).andStubReturn(legalMoveProbe);
        EasyMock.expect(legalMoveProbe.movePiece(
                        EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubReturn(true);
        stubAllInBounds(legalMoveProbe);
        stubBoardState(legalMoveProbe, legalProbeState);

        EasyMock.expect(board.piecePlacementKey()).andStubReturn("position-after-move");
        EasyMock.expect(board.castlingRightsKey()).andStubReturn("1111");

        EasyMock.replay(board, simulatedBoard, legalMoveProbe);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(6, 0, 5, 0));
        assertEquals("BLACK", game.getCurrentPlayer().getColor());
        assertEquals(0, game.getHalfmoveClock());
    }

    @Test
    void makeMoveRejectsMoveThatLeavesOwnKingInCheck() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> realState = new HashMap<>();
        realState.put(key(7, 4), new Piece("KING", "WHITE"));
        realState.put(key(7, 0), new Piece("ROOK", "BLACK"));
        realState.put(key(7, 2), new Piece("ROOK", "WHITE"));
        realState.put(key(0, 4), new Piece("KING", "BLACK"));

        Map<String, Piece> simulatedState = new HashMap<>();
        simulatedState.put(key(7, 4), new Piece("KING", "WHITE"));
        simulatedState.put(key(7, 0), new Piece("ROOK", "BLACK"));
        simulatedState.put(key(6, 2), new Piece("ROOK", "WHITE"));
        simulatedState.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, realState);

        EasyMock.expect(board.copy()).andReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(7, 2, 6, 2, null)).andReturn(true);
        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, simulatedState);

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(7, 2, 6, 2));
        assertEquals("WHITE", game.getCurrentPlayer().getColor());
    }

    @Test
    void makeMoveEndsGameWhenBlackIsCheckmated() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);
        Board legalMoveProbe = EasyMock.niceMock(Board.class);

        Map<String, Piece> realState = new HashMap<>();
        realState.put(key(7, 4), new Piece("KING", "WHITE"));
        realState.put(key(0, 0), new Piece("KING", "BLACK"));
        realState.put(key(0, 7), new Piece("ROOK", "WHITE"));
        realState.put(key(6, 0), new Piece("PAWN", "WHITE"));

        Map<String, Piece> simulatedState = new HashMap<>();
        simulatedState.put(key(7, 4), new Piece("KING", "WHITE"));
        simulatedState.put(key(0, 0), new Piece("KING", "BLACK"));
        simulatedState.put(key(0, 7), new Piece("ROOK", "WHITE"));
        simulatedState.put(key(5, 0), new Piece("PAWN", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, realState);

        EasyMock.expect(board.copy()).andReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andReturn(true);
        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, simulatedState);

        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andAnswer(() -> {
            realState.remove(key(6, 0));
            realState.put(key(5, 0), new Piece("PAWN", "WHITE"));
            return true;
        });

        EasyMock.expect(board.copy()).andStubReturn(legalMoveProbe);
        EasyMock.expect(legalMoveProbe.movePiece(
                        EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubReturn(false);
        stubAllInBounds(legalMoveProbe);
        stubBoardState(legalMoveProbe, realState);

        EasyMock.replay(board, simulatedBoard, legalMoveProbe);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(6, 0, 5, 0));
        assertTrue(game.isGameOver());
        assertFalse(game.isDraw());
        assertEquals("WHITE", game.getWinnerColor());
    }

    @Test
    void makeMoveRejectsMoveAfterGameOver() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);
        Board legalMoveProbe = EasyMock.niceMock(Board.class);

        Map<String, Piece> realState = new HashMap<>();
        realState.put(key(7, 4), new Piece("KING", "WHITE"));
        realState.put(key(0, 0), new Piece("KING", "BLACK"));
        realState.put(key(0, 7), new Piece("ROOK", "WHITE"));
        realState.put(key(6, 0), new Piece("PAWN", "WHITE"));

        Map<String, Piece> simulatedState = new HashMap<>();
        simulatedState.put(key(7, 4), new Piece("KING", "WHITE"));
        simulatedState.put(key(0, 0), new Piece("KING", "BLACK"));
        simulatedState.put(key(0, 7), new Piece("ROOK", "WHITE"));
        simulatedState.put(key(5, 0), new Piece("PAWN", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, realState);

        EasyMock.expect(board.copy()).andReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andReturn(true);
        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, simulatedState);

        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andAnswer(() -> {
            realState.remove(key(6, 0));
            realState.put(key(5, 0), new Piece("PAWN", "WHITE"));
            return true;
        });

        EasyMock.expect(board.copy()).andStubReturn(legalMoveProbe);
        EasyMock.expect(legalMoveProbe.movePiece(
                        EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubReturn(false);
        stubAllInBounds(legalMoveProbe);
        stubBoardState(legalMoveProbe, realState);

        EasyMock.replay(board, simulatedBoard, legalMoveProbe);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(6, 0, 5, 0));
        assertTrue(game.isGameOver());
        assertFalse(game.makeMove(5, 0, 4, 0));
        assertEquals("WHITE", game.getWinnerColor());
    }

    @Test
    void isKingInCheckReturnsFalseWhenBoardIsNull() {
        Game game = new Game(null, whitePlayer, blackPlayer);

        assertFalse(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckDetectsRookAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(4, 0), new Piece("ROOK", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckDetectsBishopAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(1, 1), new Piece("BISHOP", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckDetectsKnightAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(2, 3), new Piece("KNIGHT", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckDetectsQueenAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(4, 0), new Piece("QUEEN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckDetectsPawnAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(3, 3), new Piece("PAWN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isKingInCheck("WHITE"));
    }

    @Test
    void isKingInCheckReturnsFalseWhenSlidingPathIsBlocked() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "WHITE"));
        state.put(key(4, 0), new Piece("ROOK", "BLACK"));
        state.put(key(4, 2), new Piece("BISHOP", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isKingInCheck("WHITE"));
    }

    @Test
    void isSquareUnderAttackReturnsFalseWhenBoardIsNull() {
        Game game = new Game(null, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackReturnsFalseForOutOfBoundsSquare() {
        Board board = EasyMock.niceMock(Board.class);

        EasyMock.expect(board.isWithinBounds(-1, 0)).andReturn(false);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(-1, 0, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsRookAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 0), new Piece("ROOK", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsBishopAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(1, 1), new Piece("BISHOP", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsKnightAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(2, 3), new Piece("KNIGHT", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsQueenAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 0), new Piece("QUEEN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsPawnAttackByBlack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(3, 3), new Piece("PAWN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackDetectsPawnAttackByWhite() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(5, 3), new Piece("PAWN", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "WHITE"));
    }

    @Test
    void isSquareUnderAttackDetectsAdjacentKingAttack() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(3, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void isSquareUnderAttackReturnsFalseWhenPathIsBlocked() {
        Board board = EasyMock.niceMock(Board.class);
        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 0), new Piece("ROOK", "BLACK"));
        state.put(key(4, 2), new Piece("BISHOP", "WHITE"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    private static void stubBoardState(Board board, Map<String, Piece> state) {
        EasyMock.expect(board.getPieceAt(EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubAnswer(() -> {
                    Object[] args = EasyMock.getCurrentArguments();
                    return state.get(key((int) args[0], (int) args[1]));
                });
    }

    private static void stubAllInBounds(Board board) {
        EasyMock.expect(board.isWithinBounds(EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubReturn(true);
    }

    private static String key(int row, int col) {
        return row + "," + col;
    }
}