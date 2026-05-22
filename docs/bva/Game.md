# BVA Analysis for `Game` Class

## Method under test: `startNewGame()`

| Test Case ID | State of the System | Expected Output | Implemented?       |
|-------------|---------------------|-----------------|--------------------|
| GAME-START-001 | No game is currently active. `startNewGame()` is called. | A new `Board` is created for the game. | :white_check_mark: |
| GAME-START-002 | No game is currently active. `startNewGame()` is called. | The new board is initialized with the starting chess pieces. | :white_check_mark: |
| GAME-START-003 | A game is already active. `startNewGame()` is called again. | The existing board is replaced with a clean, newly initialized board. No old board state remains. | :white_check_mark: |

---

## Method under test: `getBoard()`

| Test Case ID | State of the System | Expected Output | Implemented?       |
|-------------|---------------------|-----------------|--------------------|
| GAME-BOARD-001 | A game has not started yet. `getBoard()` is called. | Returns `null` because no board has been created yet. | :white_check_mark: |
| GAME-BOARD-002 | A game has started. `getBoard()` is called. | Returns the current initialized `Board`. | :white_check_mark: |

---

## Method under test: `getCurrentPlayer()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------|
| GAME-CURRENT-001 | A new game has just started. | Returns the White player because White moves first in chess. | :white_check_mark: |
| GAME-CURRENT-002 | White has completed one valid move. | Returns the Black player. | :white_check_mark: |
| GAME-CURRENT-003 | Black has completed one valid move after White. | Returns the White player. | :white_check_mark: |

---

## Method under test: `switchTurn()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------|
| GAME-TURN-001 | It is White's turn. `switchTurn()` is called. | Current player changes to Black. | :white_check_mark: |
| GAME-TURN-002 | It is Black's turn. `switchTurn()` is called. | Current player changes to White. | :white_check_mark: |

---

## Method under test: `isGameOver()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|-----------|
| GAME-OVER-001 | A new game has started and no moves have been made. | Returns `false`. | :white_check_mark: |
| GAME-OVER-002 | A checkmate position has been reached. | Returns `true`. | :x: |
| GAME-OVER-003 | A draw condition has been reached. | Returns `true`. | :x: |

---

---

## Method under test: `makeMove(startRow, startCol, endRow, endCol)`

| Test Case ID | State of the System | Expected Output | Implemented?      |
|-------------|---------------------|-----------------|-------------------|
| GAME-MOVE-001 | It is White's turn. White moves a pawn from `(6, 0)` to `(5, 0)`. | Returns `true`, moves the pawn, and changes the current player to Black. | :white_check_mark: |
| GAME-MOVE-002 | It is Black's turn. Black moves a pawn from `(1, 0)` to `(2, 0)`. | Returns `true`, moves the pawn, and changes the current player to White. | :white_check_mark: |
| GAME-MOVE-003 | It is White's turn. White attempts an invalid pawn move from `(6, 0)` to `(3, 0)`. | Returns `false`, does not move the pawn, and keeps the current player as White. | :white_check_mark:                  |
| GAME-MOVE-004 | It is White's turn. White attempts to move a Black pawn from `(1, 0)` to `(2, 0)`. | Returns `false`, does not move the Black pawn, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-005 | It is White's turn. White attempts to move from an empty square `(4, 4)` to `(5, 4)`. | Returns `false`, does not change the board, and keeps the current player as White. | :x:               |
| GAME-MOVE-006 | It is White's turn. White attempts to move a pawn from an out-of-bounds square `(-1, 0)` to `(0, 0)`. | Returns `false`, does not change the board, and keeps the current player as White. | :x:               |
| GAME-MOVE-007 | It is White's turn. White attempts to move a pawn from `(6, 0)` to an out-of-bounds square `(8, 0)`. | Returns `false`, does not change the board, and keeps the current player as White. | :x:               |
| GAME-MOVE-008 | A game has not started yet. `makeMove(6, 0, 5, 0)` is called. | Returns `false` because no board exists yet. | :x:               |
| GAME-MOVE-009 | It is White's turn. White attempts to move a pawn from `(6, 0)` to an occupied square `(7, 0)`. | Returns `false`, does not move the pawn, and keeps the current player as White. | :x:               |
| GAME-MOVE-010 | It is White's turn. White attempts an invalid knight move from `(7, 1)` to `(6, 1)`. | Returns `false`, does not move the knight, and keeps the current player as White. | :x:               |
