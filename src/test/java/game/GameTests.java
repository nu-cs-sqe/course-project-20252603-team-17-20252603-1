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

class GameTests {

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
    void getPositionSignatureIncludesPiecesCastlingRightsAndSideToMove() {
        Board board = EasyMock.niceMock(Board.class);

        EasyMock.expect(board.piecePlacementKey()).andStubReturn("pieces");
        EasyMock.expect(board.castlingRightsKey()).andStubReturn("1111");

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertEquals("pieces|1111|WHITE", game.getPositionSignature("WHITE"));
    }

    @Test
    void makeMoveReturnsFalseWhenActualBoardMoveFailsAfterSimulationPasses() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> boardState = new HashMap<>();
        boardState.put(key(6, 0), new Piece("PAWN", "WHITE"));
        boardState.put(key(7, 4), new Piece("KING", "WHITE"));
        boardState.put(key(0, 4), new Piece("KING", "BLACK"));

        Map<String, Piece> simulatedState = new HashMap<>();
        simulatedState.put(key(5, 0), new Piece("PAWN", "WHITE"));
        simulatedState.put(key(7, 4), new Piece("KING", "WHITE"));
        simulatedState.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, boardState);
        stubBoardState(simulatedBoard, simulatedState);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andStubReturn(true);
        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andStubReturn(false);

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 5, 0));
        assertSame(whitePlayer, game.getCurrentPlayer());
        assertEquals(0, game.getMoveHistory().size());
    }

    @Test
    void promotedMoveHistoryRecordsPromotionPieceType() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(1, 0), new Piece("PAWN", "WHITE"));
        state.put(key(7, 4), new Piece("KING", "WHITE"));
        state.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(1, 0, 0, 0, "KNIGHT")).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(1, 0, 0, 0, "KNIGHT")).andAnswer(() -> {
            state.remove(key(1, 0));
            state.put(key(0, 0), new Piece("KNIGHT", "WHITE"));
            return true;
        });

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(1, 0, 0, 0, "KNIGHT"));
        assertEquals(1, game.getMoveHistory().size());
        assertEquals("KNIGHT", game.getMoveHistory().get(0).getPromotionPieceType());
    }

    @Test
    void enPassantMoveHistoryRecordsCaptureAndCapturedPawn() {
        Game game = new Game();
        game.startNewGame();

        assertTrue(game.makeMove(6, 4, 4, 4));
        assertTrue(game.makeMove(1, 0, 2, 0));
        assertTrue(game.makeMove(4, 4, 3, 4));
        assertTrue(game.makeMove(1, 3, 3, 3));
        assertTrue(game.makeMove(3, 4, 2, 3));

        Move move = game.getMoveHistory().get(game.getMoveHistory().size() - 1);

        assertTrue(move.isEnPassant());
        assertTrue(move.isCapture());
        assertEquals("PAWN", move.getCapturedPieceType());
    }

    @Test
    void blackKingsideCastlingMoveHistoryRecordsCastling() {
        Board board = EasyMock.niceMock(Board.class);
        Board throughProbe = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(0, 4), new Piece("KING", "BLACK"));
        state.put(key(0, 7), new Piece("ROOK", "BLACK"));
        state.put(key(7, 4), new Piece("KING", "WHITE"));

        stubAllInBounds(board);
        stubAllInBounds(throughProbe);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(throughProbe, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andReturn(throughProbe);
        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);

        EasyMock.expect(throughProbe.movePiece(0, 4, 0, 5)).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(0, 4, 0, 6, null)).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(0, 4, 0, 6, null)).andAnswer(() -> {
            state.remove(key(0, 4));
            state.remove(key(0, 7));
            state.put(key(0, 6), new Piece("KING", "BLACK"));
            state.put(key(0, 5), new Piece("ROOK", "BLACK"));
            return true;
        });

        EasyMock.replay(board, throughProbe, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);
        game.switchTurn();

        assertTrue(game.makeMove(0, 4, 0, 6));

        Move move = game.getMoveHistory().get(0);
        assertTrue(move.isCastling());
        assertEquals("KING", move.getPieceType());
        assertEquals("BLACK", move.getColor());
    }

    @Test
    void makeMoveReturnsFalseWhenSimulationFailsEvenIfRealBoardWouldMove() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> boardState = new HashMap<>();
        boardState.put(key(6, 0), new Piece("PAWN", "WHITE"));
        boardState.put(key(7, 4), new Piece("KING", "WHITE"));
        boardState.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, boardState);
        stubBoardState(simulatedBoard, boardState);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andStubReturn(false);
        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andStubReturn(true);

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(6, 0, 5, 0));
        assertEquals(0, game.getMoveHistory().size());
        assertSame(whitePlayer, game.getCurrentPlayer());
    }

    @Test
    void normalPawnMoveDoesNotRecordPromotionPieceType() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(6, 0), new Piece("PAWN", "WHITE"));
        state.put(key(7, 4), new Piece("KING", "WHITE"));
        state.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 5, 0, null)).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(6, 0, 5, 0, null)).andAnswer(() -> {
            state.remove(key(6, 0));
            state.put(key(5, 0), new Piece("PAWN", "WHITE"));
            return true;
        });

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(6, 0, 5, 0));
        assertNull(game.getMoveHistory().get(0).getPromotionPieceType());
    }

    @Test
    void blackPawnPromotionRecordsPromotionPieceType() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(6, 0), new Piece("PAWN", "BLACK"));
        state.put(key(7, 4), new Piece("KING", "WHITE"));
        state.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 0, 7, 0, "ROOK")).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(6, 0, 7, 0, "ROOK")).andAnswer(() -> {
            state.remove(key(6, 0));
            state.put(key(7, 0), new Piece("ROOK", "BLACK"));
            return true;
        });

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);
        game.switchTurn();

        assertTrue(game.makeMove(6, 0, 7, 0, "ROOK"));
        assertEquals("ROOK", game.getMoveHistory().get(0).getPromotionPieceType());
    }

    @Test
    void whiteKingTwoSquareMoveAwayFromHomeRowIsNotRecordedAsCastling() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(6, 4), new Piece("KING", "WHITE"));
        state.put(key(0, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(6, 4, 6, 6, null)).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(6, 4, 6, 6, null)).andAnswer(() -> {
            state.remove(key(6, 4));
            state.put(key(6, 6), new Piece("KING", "WHITE"));
            return true;
        });

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.makeMove(6, 4, 6, 6));
        assertFalse(game.getMoveHistory().get(0).isCastling());
    }

    @Test
    void blackKingTwoSquareMoveAwayFromHomeRowIsNotRecordedAsCastling() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(1, 4), new Piece("KING", "BLACK"));
        state.put(key(7, 4), new Piece("KING", "WHITE"));

        stubAllInBounds(board);
        stubAllInBounds(simulatedBoard);
        stubBoardState(board, state);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(1, 4, 1, 6, null)).andStubReturn(true);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.anyInt(),
                        EasyMock.<String>isNull()))
                .andStubReturn(true);

        EasyMock.expect(board.movePiece(1, 4, 1, 6, null)).andAnswer(() -> {
            state.remove(key(1, 4));
            state.put(key(1, 6), new Piece("KING", "BLACK"));
            return true;
        });

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);
        game.switchTurn();

        assertTrue(game.makeMove(1, 4, 1, 6));
        assertFalse(game.getMoveHistory().get(0).isCastling());
    }

    @Test
    void stalemateWithNullBoardCoversNoLegalMovePath() {
        Game game = new Game(null, whitePlayer, blackPlayer);

        assertTrue(game.isStalemate("WHITE"));
    }

    @Test
    void legalPieceOnlyOnLastBoardSquarePreventsStalemate() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(7, 7), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(
                        EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt(), EasyMock.anyInt()))
                .andStubReturn(true);

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isStalemate("BLACK"));
    }

    @Test
    void queensideCastlingRejectedWhenKingCannotMoveThroughMiddleSquare() {
        Board board = EasyMock.niceMock(Board.class);
        Board throughProbe = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(7, 4), new Piece("KING", "WHITE"));
        state.put(key(7, 0), new Piece("ROOK", "WHITE"));
        state.put(key(0, 3), new Piece("ROOK", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        stubAllInBounds(throughProbe);
        stubBoardState(throughProbe, state);

        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andReturn(throughProbe);
        EasyMock.expect(throughProbe.movePiece(7, 4, 7, 3)).andReturn(false);

        EasyMock.replay(board, throughProbe, simulatedBoard);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.makeMove(7, 4, 7, 2));
    }

    @Test
    void invalidColorKingTwoSquareMoveIsNotRecordedAsCastling() {
        Board board = EasyMock.niceMock(Board.class);
        Board simulatedBoard = EasyMock.niceMock(Board.class);

        Player greenPlayer = EasyMock.niceMock(Player.class);
        EasyMock.expect(greenPlayer.getColor()).andStubReturn("GREEN");
        EasyMock.replay(greenPlayer);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "GREEN"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        stubAllInBounds(simulatedBoard);
        stubBoardState(simulatedBoard, state);

        EasyMock.expect(board.copy()).andStubReturn(simulatedBoard);
        EasyMock.expect(simulatedBoard.movePiece(4, 4, 4, 6, null)).andStubReturn(true);
        EasyMock.expect(board.movePiece(4, 4, 4, 6, null)).andStubReturn(true);
        EasyMock.expect(board.piecePlacementKey()).andStubReturn("green-king");
        EasyMock.expect(board.castlingRightsKey()).andStubReturn("0000");

        EasyMock.replay(board, simulatedBoard);

        Game game = new Game(board, greenPlayer, blackPlayer);

        assertTrue(game.makeMove(4, 4, 4, 6));
        assertFalse(game.getMoveHistory().get(0).isCastling());
    }

    @Test
    void blockedBishopDoesNotAttackSquare() {
        Board board = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(0, 0), new Piece("BISHOP", "BLACK"));
        state.put(key(1, 1), new Piece("PAWN", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(3, 3, "BLACK"));
    }

    @Test
    void kingOnTargetSquareDoesNotCountAsAttackingItself() {
        Board board = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KING", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(4, 4, "BLACK"));
    }

    @Test
    void pawnWithInvalidColorDoesNotAttackSquare() {
        Board board = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("PAWN", "GREEN"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertFalse(game.isSquareUnderAttack(3, 5, "GREEN"));
    }

    @Test
    void knightAttacksOtherLShapeOrientation() {
        Board board = EasyMock.niceMock(Board.class);

        Map<String, Piece> state = new HashMap<>();
        state.put(key(4, 4), new Piece("KNIGHT", "BLACK"));

        stubAllInBounds(board);
        stubBoardState(board, state);

        EasyMock.replay(board);

        Game game = new Game(board, whitePlayer, blackPlayer);

        assertTrue(game.isSquareUnderAttack(5, 6, "BLACK"));
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