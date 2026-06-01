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

| Test Case ID | State of the System | Expected Output | Implemented?       |
|-------------|---------------------|-----------------|--------------------|
| GAME-OVER-001 | A new game has started and no moves have been made. | Returns `false`. | :white_check_mark: |
| GAME-OVER-002 | Black king has been captured by White. | Returns `true` and winner is White. | :white_check_mark: |
| GAME-OVER-003 | White king has been captured by Black. | Returns `true` and winner is Black. | :white_check_mark: |
---

---

## Method under test: `makeMove(startRow, startCol, endRow, endCol)`

| Test Case ID | State of the System | Expected Output | Implemented?      |
|-------------|---------------------|-----------------|-------------------|
| GAME-MOVE-001 | It is White's turn. White moves a pawn from `(6, 0)` to `(5, 0)`. | Returns `true`, moves the pawn, and changes the current player to Black. | :white_check_mark: |
| GAME-MOVE-002 | It is Black's turn. Black moves a pawn from `(1, 0)` to `(2, 0)`. | Returns `true`, moves the pawn, and changes the current player to White. | :white_check_mark: |
| GAME-MOVE-003 | It is White's turn. White attempts an invalid pawn move from `(6, 0)` to `(3, 0)`. | Returns `false`, does not move the pawn, and keeps the current player as White. | :white_check_mark:                  |
| GAME-MOVE-004 | It is White's turn. White attempts to move a Black pawn from `(1, 0)` to `(2, 0)`. | Returns `false`, does not move the Black pawn, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-005 | It is White's turn. White attempts to move from an empty square `(4, 4)` to `(5, 4)`. | Returns `false`, does not change the board, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-006 | It is White's turn. White attempts to move a pawn from an out-of-bounds square `(-1, 0)` to `(0, 0)`. | Returns `false`, does not change the board, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-007 | It is White's turn. White attempts to move a pawn from `(6, 0)` to an out-of-bounds square `(8, 0)`. | Returns `false`, does not change the board, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-008 | A game has not started yet. `makeMove(6, 0, 5, 0)` is called. | Returns `false` because no board exists yet. | :white_check_mark:               |
| GAME-MOVE-009 | It is White's turn. White attempts to move a pawn from `(6, 0)` to an occupied square `(7, 0)`. | Returns `false`, does not move the pawn, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-010 | It is White's turn. White attempts an invalid knight move from `(7, 1)` to `(6, 1)`. | Returns `false`, does not move the knight, and keeps the current player as White. | :white_check_mark:               |
| GAME-MOVE-011 | The game is already over because a king has been captured. A player attempts another move. | Returns `false`, does not change the board, and does not change the winner. | :white_check_mark: |


| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-SELFCHECK-001 | The White king is shielded by a White rook from a Black rook. White attempts to move the shielding rook away. | Move is rejected because it would leave the White king in check. Board and turn stay unchanged. | :white_check_mark: |
| GAME-SELFCHECK-002 | The White king attempts to move onto a square attacked by a Black rook. | Move is rejected because the White king cannot move into check. Board and turn stay unchanged. | :x: |
| GAME-SELFCHECK-003 | The White king is in check from a Black rook. White moves the king to a safe square. | Move succeeds because the move gets White out of check. Turn changes to Black. | :x: |
| GAME-SELFCHECK-004 | The White king is in check from a Black rook. White attempts a move that does not resolve the check. | Move is rejected because White remains in check. Board and turn stay unchanged. | :x: |
| GAME-SELFCHECK-005 | The Black king would be exposed to check after Black moves a shielding piece. | Move is rejected because it would leave the Black king in check. Board and turn stay unchanged. | :x: |

## Method under test: isKingInCheck(String color)

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-CHECK-001 | A new game has started and the White king is not under attack. `isKingInCheck("WHITE")` is called. | Returns `false`. | :white_check_mark: |
| GAME-CHECK-002 | A new game has started and the Black king is not under attack. `isKingInCheck("BLACK")` is called. | Returns `false`. | :white_check_mark: |
| GAME-CHECK-003 | A Black rook has a clear row or column path attacking the White king. `isKingInCheck("WHITE")` is called. | Returns `true`. | :white_check_mark: |
| GAME-CHECK-004 | A Black bishop has a clear diagonal path attacking the White king. `isKingInCheck("WHITE")` is called. | Returns `true`. | :white_check_mark: |
| GAME-CHECK-005 | A Black knight attacks the White king in an L-shape. `isKingInCheck("WHITE")` is called. | Returns `true`. | :white_check_mark: |
| GAME-CHECK-006 | A Black queen attacks the White king along a clear row, column, or diagonal. `isKingInCheck("WHITE")` is called. | Returns `true`. | :white_check_mark: |
| GAME-CHECK-007 | A Black pawn attacks the White king diagonally. `isKingInCheck("WHITE")` is called. | Returns `true`. | :white_check_mark: |
| GAME-CHECK-008 | A Black sliding piece would attack the White king, but another piece blocks the path. `isKingInCheck("WHITE")` is called. | Returns `false`. | :white_check_mark: |
| GAME-CHECK-009 | A game has not started yet. `isKingInCheck("WHITE")` is called. | Returns `false`. | :white_check_mark: |
| GAME-CHECK-010 | A White rook has a clear row or column path attacking the Black king. `isKingInCheck("BLACK")` is called. | Returns `true`. | :white_check_mark: |



## Method under test: isSquareUnderAttack(int row, int col, String byColor)

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-ATTACK-001 | A new game has started. Square `(4, 4)` is not attacked by any Black piece. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `false`. | :white_check_mark: |
| GAME-ATTACK-002 | A Black rook has a clear row path to square `(4, 4)`. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-003 | A Black bishop has a clear diagonal path to square `(4, 4)`. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-004 | A Black knight attacks square `(4, 4)` in an L-shape. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-005 | A Black queen has a clear path to square `(4, 4)`. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-006 | A Black pawn attacks square `(4, 4)` diagonally. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-007 | A Black king is adjacent to square `(4, 4)`. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `true`. | :white_check_mark: |
| GAME-ATTACK-008 | A Black rook would attack square `(4, 4)`, but another piece blocks the path. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `false`. | :white_check_mark: |
| GAME-ATTACK-009 | The requested square is outside the board. `isSquareUnderAttack(-1, 0, "BLACK")` is called. | Returns `false`. | :white_check_mark: |
| GAME-ATTACK-010 | A game has not started yet. `isSquareUnderAttack(4, 4, "BLACK")` is called. | Returns `false`. | :white_check_mark: |
| GAME-ATTACK-011 | A White pawn attacks square `(4, 4)` diagonally. `isSquareUnderAttack(4, 4, "WHITE")` is called. | Returns  `true`. | :white_check_mark: |


