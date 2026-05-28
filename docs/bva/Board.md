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

## Method under test: `movePiece(int startRow, int startCol, int endRow, int endCol)`

| Test Case ID | State of the System                                                                                               | Expected Output                                                                                                   | Implemented? |
|-------------|-------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|----------|
| BOARD-MOVE-001 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 5, 0)` is called.                               | Pawn moves one square forward. Starting square becomes empty and ending square contains the White pawn.           | :white_check_mark: |
| BOARD-MOVE-002 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 4, 0)` is called.                               | Pawn moves two squares forward because it is the pawn's first move.                                               | :white_check_mark: |
| BOARD-MOVE-003 | Board is initialized. White pawn is at `(6, 0)`. `movePiece(6, 0, 3, 0)` is called.                               | Move is rejected because a pawn cannot move three squares.                                                        | :white_check_mark: |
| BOARD-MOVE-004 | Board is initialized. Empty square at `(3, 3)`. `movePiece(3, 3, 4, 3)` is called.                                | Move is rejected because there is no piece at the starting square.                                                | :white_check_mark: |
| BOARD-MOVE-005 | Board is initialized. `movePiece(-1, 0, 0, 0)` is called.                                                         | Move is rejected because the starting row is outside the board.                                                   | :white_check_mark: |
| BOARD-MOVE-006 | Board is initialized. `movePiece(0, 0, 8, 0)` is called.                                                          | Move is rejected because the ending row is outside the board.                                                     | :white_check_mark: |
| BOARD-MOVE-007 | Board is initialized. Black pawn is at `(1, 0)`. `movePiece(1, 0, 2, 0)` is called.                               | Pawn moves one square forward. Starting square becomes empty and ending square contains the Black pawn.           | :white_check_mark: |
| BOARD-MOVE-008 | Board is initialized. Black pawn is at `(1, 0)`. `movePiece(1, 0, 3, 0)` is called.                               | Pawn moves two squares forward because it is the pawn's first move.                                               | :white_check_mark: |
| BOARD-MOVE-009 | Board is initialized. White pawn is at `(6, 0)` and White rook is at `(7, 0)`. `movePiece(6, 0, 7, 0)` is called. | Move is rejected because pawns cannot move forward into an occupied square. Both pieces remain in place.          | :white_check_mark: |
| BOARD-MOVE-010 | Black pawn is on `(5, 1)` and White pawn is on `(6, 0)`. `movePiece(6, 0, 5, 1)` is called. | White pawn captures diagonally. Starting square becomes empty and ending square contains the White pawn.          | :white_check_mark: |
| BOARD-MOVE-011 | White pawn is at `(6, 0)` and Black pawn is directly in front at `(5, 0)`. `movePiece(6, 0, 5, 0)` is called. | Move is rejected because pawns cannot capture straight forward. Both pieces remain in place.                      | :white_check_mark: |
| BOARD-MOVE-012 | White pawn is on `(2, 1)` and Black pawn is on `(1, 0)`. `movePiece(1, 0, 2, 1)` is called. | Black pawn captures diagonally. Starting square becomes empty and ending square contains the Black pawn.          | :white_check_mark: |
| BOARD-MOVE-013 | Black pawn is at `(1, 0)` and White pawn is directly in front at `(2, 0)`. `movePiece(1, 0, 2, 0)` is called. | Move is rejected because pawns cannot capture straight forward. Both pieces remain in place.                      | :white_check_mark: |
| BOARD-MOVE-014 | White pawn is at `(6, 0)` and Black pawn is at `(4, 0)`. `movePiece(6, 0, 4, 0)` is called. | Move is rejected because pawns cannot move two squares into an occupied destination. Both pieces remain in place. | :white_check_mark: |






