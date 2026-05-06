# BVA Analysis for `Game` Class

## Method under test: `startNewGame()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-START-001 | No game is currently active. `startNewGame()` is called. | A new chess game is created, the board is initialized, two players are created, White is set as the first player, and the game status becomes active. | :x: |
| GAME-START-002 | A game is already active. `startNewGame()` is called again. | The system either resets the current game or creates a new clean game state, depending on team design decision. No old board state should remain. | :x: |

---

## Method under test: `getCurrentPlayer()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-CURRENT-001 | A new game has just started. | Returns the White player because White moves first in chess. | :x: |
| GAME-CURRENT-002 | White has completed one valid move. | Returns the Black player. | :x: |
| GAME-CURRENT-003 | Black has completed one valid move after White. | Returns the White player. | :x: |

---

## Method under test: `switchTurn()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-TURN-001 | It is White's turn. `switchTurn()` is called. | Current player changes to Black. | :x: |
| GAME-TURN-002 | It is Black's turn. `switchTurn()` is called. | Current player changes to White. | :x: |

---

## Method under test: `isGameOver()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-OVER-001 | A new game has started and no moves have been made. | Returns `false`. | :x: |
| GAME-OVER-002 | A checkmate position has been reached. | Returns `true`. | :x: |
| GAME-OVER-003 | A draw condition has been reached. | Returns `true`. | :x: |