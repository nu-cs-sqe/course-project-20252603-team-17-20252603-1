# BVA Analysis for `Board` Class

## Method under test: `initializeBoard()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| BOARD-INIT-001 | A new empty board exists. `initializeBoard()` is called. | The board becomes an 8x8 chess board with all 32 pieces in their correct starting positions. | yes          |
| BOARD-INIT-002 | A board already has pieces on it. `initializeBoard()` is called again. | The board resets to the standard chess starting position with no leftover pieces from the previous state. | yes          |

---

## Method under test: `getPieceAt(int row, int col)`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| BOARD-GET-001 | Board is initialized. `getPieceAt(0, 0)` is called. | Returns Black rook. | yes          |
| BOARD-GET-002 | Board is initialized. `getPieceAt(0, 4)` is called. | Returns Black king. | yes          |
| BOARD-GET-003 | Board is initialized. `getPieceAt(7, 4)` is called. | Returns White king. | yes          |
| BOARD-GET-004 | Board is initialized. `getPieceAt(3, 3)` is called. | Returns `null` because the square is empty at the start. | yes          |
| BOARD-GET-005 | Board is initialized. `getPieceAt(-1, 0)` is called. | Throws an error or returns invalid result because row is below board boundary. | yes          |
| BOARD-GET-006 | Board is initialized. `getPieceAt(8, 0)` is called. | Throws an error or returns invalid result because row is above board boundary. | yes          |
| BOARD-GET-007 | Board is initialized. `getPieceAt(0, -1)` is called. | Throws an error or returns invalid result because column is below board boundary. | yes          |
| BOARD-GET-008 | Board is initialized. `getPieceAt(0, 8)` is called. | Throws an error or returns invalid result because column is above board boundary. | yes            |

---

## Method under test: `isWithinBounds(int row, int col)`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| BOARD-BOUNDS-001 | Board exists. `isWithinBounds(0, 0)` is called. | Returns `true`. | yes          |
| BOARD-BOUNDS-002 | Board exists. `isWithinBounds(7, 7)` is called. | Returns `true`. | yes          |
| BOARD-BOUNDS-003 | Board exists. `isWithinBounds(-1, 0)` is called. | Returns `false`. | yes          |
| BOARD-BOUNDS-004 | Board exists. `isWithinBounds(8, 0)` is called. | Returns `false`. | yes          |
| BOARD-BOUNDS-005 | Board exists. `isWithinBounds(0, -1)` is called. | Returns `false`. | yes          |
| BOARD-BOUNDS-006 | Board exists. `isWithinBounds(0, 8)` is called. | Returns `false`. | yes          |

---

## Method under test: `movePiece(int startRow, int startCol, int endRow, int endCol)` and `movePiece(..., String promotionPiece)`

| Test Case ID | State of the System                                                                                               | Expected Output                                                                                                                                         | Implemented? |
|-------------|-------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| BOARD-MOVE-001 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 5, 0)` is called.                               | Pawn moves one square forward. Starting square becomes empty and ending square contains the White pawn.                                                 | :white_check_mark: |
| BOARD-MOVE-002 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 4, 0)` is called.                               | Pawn moves two squares forward because it is the pawn's first move.                                                                                     | :white_check_mark: |
| BOARD-MOVE-003 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 3, 0)` is called.                               | Move is rejected because a pawn cannot move three squares.                                                                                              | :white_check_mark: |
| BOARD-MOVE-004 | Board is initialized. Empty square at `(3, 3)`. `movePiece(3, 3, 4, 3)` is called.                                | Move is rejected because there is no piece at the starting square.                                                                                      | :white_check_mark: |
| BOARD-MOVE-005 | Board is initialized. `movePiece(-1, 0, 0, 0)` is called.                                                         | Move is rejected because the starting row is outside the board.                                                                                         | :white_check_mark: |
| BOARD-MOVE-006 | Board is initialized. `movePiece(0, 0, 8, 0)` is called.                                                          | Move is rejected because the ending row is outside the board.                                                                                           | :white_check_mark: |
| BOARD-MOVE-007 | Board is initialized. Black pawn is at `(1, 0)`. `movePiece(1, 0, 2, 0)` is called.                               | Pawn moves one square forward. Starting square becomes empty and ending square contains the Black pawn.                                                 | :white_check_mark: |
| BOARD-MOVE-008 | Board is initialized. Black pawn is at `(1, 0)`. `movePiece(1, 0, 3, 0)` is called.                               | Pawn moves two squares forward because it is the pawn's first move.                                                                                     | :white_check_mark: |
| BOARD-MOVE-009 | Board is initialized. White pawn is at `(6, 0)` and White rook is at `(7, 0)`. `movePiece(6, 0, 7, 0)` is called. | Move is rejected because pawns cannot move forward into an occupied square. Both pieces remain in place.                                                | :white_check_mark: |
| BOARD-MOVE-010 | Black pawn is on `(5, 1)` and White pawn is on `(6, 0)`. `movePiece(6, 0, 5, 1)` is called. | White pawn captures diagonally. Starting square becomes empty and ending square contains the White pawn.                                                | :white_check_mark: |
| BOARD-MOVE-011 | White pawn is at `(6, 0)` and Black pawn is directly in front at `(5, 0)`. `movePiece(6, 0, 5, 0)` is called. | Move is rejected because pawns cannot capture straight forward. Both pieces remain in place.                                                            | :white_check_mark: |
| BOARD-MOVE-012 | White pawn is on `(2, 1)` and Black pawn is on `(1, 0)`. `movePiece(1, 0, 2, 1)` is called. | Black pawn captures diagonally. Starting square becomes empty and ending square contains the Black pawn.                                                | :white_check_mark: |
| BOARD-MOVE-013 | Black pawn is at `(1, 0)` and White pawn is directly in front at `(2, 0)`. `movePiece(1, 0, 2, 0)` is called. | Move is rejected because pawns cannot capture straight forward. Both pieces remain in place.                                                            | :white_check_mark: |
| BOARD-MOVE-014 | White pawn is at `(6, 0)` and Black pawn is at `(4, 0)`. `movePiece(6, 0, 4, 0)` is called. | Move is rejected because pawns cannot move two squares into an occupied destination. Both pieces remain in place.                                       | :white_check_mark: |
| BOARD-MOVE-015 | White rook is at `(4, 4)` and the path to `(4, 7)` is clear. `movePiece(4, 4, 4, 7)` is called. | Rook moves horizontally. Starting square becomes empty and ending square contains the White rook.                                                       | :white_check_mark: |
| BOARD-MOVE-016 | White rook is at `(4, 4)` and the path to `(1, 4)` is clear. `movePiece(4, 4, 1, 4)` is called. | Rook moves vertically. Starting square becomes empty and ending square contains the White rook.                                                         | :white_check_mark: |
| BOARD-MOVE-017 | White rook is at `(4, 4)` and another piece is blocking the path at `(4, 6)`. `movePiece(4, 4, 4, 7)` is called. | Move is rejected because the rook's path is blocked. Both pieces remain in place.                                                                       | :white_check_mark: |
| BOARD-MOVE-018 | White bishop is at `(4, 4)` and the diagonal path to `(1, 1)` is clear. `movePiece(4, 4, 1, 1)` is called. | Bishop moves diagonally. Starting square becomes empty and ending square contains the White bishop.                                                     | :white_check_mark: |
| BOARD-MOVE-019 | White knight is at `(4, 4)`. `movePiece(4, 4, 2, 5)` is called. | Knight moves in an L shape. Starting square becomes empty and ending square contains the White knight.                                                  | :white_check_mark: |
| BOARD-MOVE-020 | White queen is at `(4, 4)` and the path to `(4, 1)` is clear. `movePiece(4, 4, 4, 1)` is called. | Queen moves straight horizontally. Starting square becomes empty and ending square contains the White queen.                                            | :white_check_mark: |
| BOARD-MOVE-021 | White queen is at `(4, 4)` and the diagonal path to `(1, 1)` is clear. `movePiece(4, 4, 1, 1)` is called. | Queen moves diagonally. Starting square becomes empty and ending square contains the White queen.                                                       | :white_check_mark: |
| BOARD-MOVE-022 | White king is at `(4, 4)`. `movePiece(4, 4, 5, 5)` is called. | King moves one square. Starting square becomes empty and ending square contains the White king.                                                         | :white_check_mark: |
| BOARD-MOVE-023 | White rook is at `(4, 4)` and White pawn is at `(4, 7)`. `movePiece(4, 4, 4, 7)` is called. | Move is rejected because a piece cannot capture a same-color piece. Both pieces remain in place.                                                        | :white_check_mark: |
| BOARD-MOVE-024 | White rook is at `(4, 4)` and Black pawn is at `(4, 7)` with a clear path. `movePiece(4, 4, 4, 7)` is called. | White rook captures the Black pawn. Starting square becomes empty and ending square contains the White rook.                                            | :white_check_mark: |
| BOARD-MOVE-025 | White bishop is at `(4, 4)` and another piece is blocking the diagonal path at `(3, 3)`. `movePiece(4, 4, 2, 2)` is called. | Move is rejected because the bishop's path is blocked. Both pieces remain in place.                                                                     | :white_check_mark: |
| BOARD-MOVE-026 | White king is at `(4, 4)`. `movePiece(4, 4, 6, 4)` is called. | Move is rejected because a king cannot move two squares except legal castling from the e-file home rank (see BOARD-MOVE-034+). The king remains in place. | :white_check_mark: |
| BOARD-MOVE-027 | White pawn at `(1, 0)`, empty `(0, 0)`. `movePiece(1, 0, 0, 0)` is called with no promotion argument. | Pawn promotes; square `(0, 0)` contains a White queen.                                                                                                  | :white_check_mark: |
| BOARD-MOVE-028 | Black pawn at `(6, 7)`, empty `(7, 7)`. `movePiece(6, 7, 7, 7)` is called with no promotion argument. | Pawn promotes; square `(7, 7)` contains a Black queen.                                                                                                  | :white_check_mark: |
| BOARD-MOVE-029 | White pawn at `(1, 2)`, empty `(0, 2)`. `movePiece(1, 2, 0, 2, "ROOK")` is called. | Pawn promotes to a White rook on `(0, 2)`.                                                                                                              | :white_check_mark: |
| BOARD-MOVE-030 | White pawn at `(3, 0)`, empty `(2, 0)`. `movePiece(3, 0, 2, 0, "ROOK")` is called. | Move is rejected; promotion piece is only valid on the final rank. Pawn stays on `(3, 0)`.                                                              | :white_check_mark: |
| BOARD-MOVE-031 | White pawn at `(1, 1)`, empty `(0, 1)`. `movePiece(1, 1, 0, 1, "PAWN")` is called. | Move is rejected; invalid promotion type.                                                                                                               | :white_check_mark: |
| BOARD-MOVE-032 | White rook at `(4, 4)`, empty `(4, 7)`. `movePiece(4, 4, 4, 7, "QUEEN")` is called. | Move is rejected; non-pawns cannot take a promotion argument.                                                                                           | :white_check_mark: |
| BOARD-MOVE-033 | White pawn at `(1, 3)`, Black rook at `(0, 4)`. `movePiece(1, 3, 0, 4)` is called. | Capture and promotion; `(0, 4)` has a White queen.                                                                                                      | :white_check_mark: |
| BOARD-MOVE-034 | White king at `(7, 4)`, White rook at `(7, 7)`, squares `(7, 5)` and `(7, 6)` empty, castling rights preserved. `movePiece(7, 4, 7, 6)` is called. | Kingside castling: king on `(7, 6)`, rook on `(7, 5)`; `(7, 4)` and `(7, 7)` empty.                                                                     | :white_check_mark: |
| BOARD-MOVE-035 | White king at `(7, 4)`, White rook at `(7, 0)`, squares `(7, 1)`–`(7, 3)` empty, castling rights preserved. `movePiece(7, 4, 7, 2)` is called. | Queenside castling: king on `(7, 2)`, rook on `(7, 3)`; `(7, 0)` and `(7, 4)` empty.                                                                    | :white_check_mark: |
| BOARD-MOVE-036 | White king at `(7, 4)`, White rook at `(7, 7)`, a piece occupies `(7, 5)` blocking f1. `movePiece(7, 4, 7, 6)` is called. | Move rejected; king and rook unmoved.                                                                                                                   | :white_check_mark: |
| BOARD-MOVE-037 | White king has previously moved from `(7, 4)` and returned; White rook still on `(7, 7)`; path clear. `movePiece(7, 4, 7, 6)` is called. | Move rejected (no castling after king moved).                                                                                                           | :white_check_mark: |
| BOARD-MOVE-038 | White king at `(7, 4)`, White kingside rook has moved away and returned; path clear. `movePiece(7, 4, 7, 6)` is called. | Move rejected (no castling after that rook moved).                                                                                                      | :white_check_mark: |
| BOARD-MOVE-039 | White king at `(7, 4)`, White rook at `(7, 7)`, path clear, opponent attacks `(7, 5)` but not `(7, 4)`. `movePiece(7, 4, 7, 6)` via game rules. | Move rejected (cannot castle through check).                                                                                                            | :white_check_mark: |
| BOARD-MOVE-040 | White king at `(7, 4)`, White rook at `(7, 7)`, path clear, opponent attacks `(7, 6)` (g1). `movePiece(7, 4, 7, 6)` is called. | Move rejected (cannot castle into check).                                                                                                               | :white_check_mark: |
| BOARD-MOVE-041 | White king at `(7, 4)` in check; White rook at `(7, 7)`; path clear. `movePiece(7, 4, 7, 6)` is called. | Move rejected (cannot castle out of / while in check).                                                                                                  | :white_check_mark: |
| BOARD-MOVE-042 | Black king at `(0, 4)`, Black rook at `(0, 0)`, squares `(0, 1)`–`(0, 3)` empty, castling rights preserved. `movePiece(0, 4, 0, 2)` is called. | Black queenside castling: king on `(0, 2)`, rook on `(0, 3)`; `(0, 0)` and `(0, 4)` empty.                                                              | :white_check_mark: |
| BOARD-MOVE-043 | Black king at `(0, 4)`, Black rook at `(0, 7)`, squares `(0, 5)` and `(0, 6)` empty, castling rights preserved. `movePiece(0, 4, 0, 6)` is called. | Black kingside castling: king on `(0, 6)`, rook on `(0, 5)`.                                                                                            | :white_check_mark: |
| BOARD-MOVE-044 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | White pawn moves to `(2, 3)` and the Black pawn at `(3, 3)` is removed.                                                                                 | :white_check_mark: |
| BOARD-MOVE-045 | Black pawn at `(4, 4)` and White pawn at `(4, 3)`. `movePieceEnPassant(4, 4, 5, 3, 4, 3)` is called. | Black pawn moves to `(5, 3)` and the White pawn at `(4, 3)` is removed.                                                                                 | :white_check_mark: |
| BOARD-MOVE-046 | No piece exists at `(3, 4)` and Black pawn is at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | Move is rejected; Black pawn remains at `(3, 3)` and target square `(2, 3)` remains empty. | :white_check_mark: |
| BOARD-MOVE-047 | White rook at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | Move is rejected because only pawns can move en passant; both pieces remain in place. | :white_check_mark: |
| BOARD-MOVE-048 | White pawn at `(3, 4)` and White pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | Move is rejected because en passant cannot capture a same-color pawn; both pawns remain in place. | :white_check_mark: |
| BOARD-MOVE-049 | White pawn at `(3, 4)` and Black rook at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | Move is rejected because en passant can only capture a pawn; both pieces remain in place. | :white_check_mark: |
| BOARD-MOVE-050 | White pawn at `(3, 4)`, Black pawn at `(3, 3)`, and another piece already occupies target square `(2, 3)`. `movePieceEnPassant(3, 4, 2, 3, 3, 3)` is called. | Move is rejected because the en passant landing square must be empty; all pieces remain in place. | :white_check_mark: |
| BOARD-MOVE-051 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 4, 3, 3, 3)` is called. | Move is rejected because White cannot move en passant in the wrong direction; both pawns remain in place. | :white_check_mark: |
| BOARD-MOVE-052 | Black pawn at `(4, 4)` and White pawn at `(4, 3)`. `movePieceEnPassant(4, 4, 3, 3, 4, 3)` is called. | Move is rejected because Black cannot move en passant in the wrong direction; both pawns remain in place. | :white_check_mark: |
| BOARD-MOVE-053 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 1, 3, 3, 3)` is called. | Move is rejected because en passant must move exactly one row diagonally; both pawns remain in place. | :white_check_mark: |
| BOARD-MOVE-054 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 4, 3, 3)` is called. | Move is rejected because en passant must move diagonally by one column; both pawns remain in place. | :white_check_mark: |
| BOARD-MOVE-055 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(-1, 4, 2, 3, 3, 3)` is called. | Move is rejected because the start square is outside the board; both pawns remain in place. | :x: |
| BOARD-MOVE-056 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 8, 3, 3, 3)` is called. | Move is rejected because the target square is outside the board; both pawns remain in place. | :x: |
| BOARD-MOVE-057 | White pawn at `(3, 4)` and Black pawn at `(3, 3)`. `movePieceEnPassant(3, 4, 2, 3, 9, 3)` is called. | Move is rejected because the captured pawn square is outside the board; both pawns remain in place. | :x: |











