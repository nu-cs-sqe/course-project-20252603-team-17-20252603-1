# BVA Analysis for `Game` Class

## Method under test: `startNewGame()`

| Test Case ID | State of the System | Expected Output | Implemented?       |
|-------------|---------------------|-----------------|--------------------|
| GAME-START-001 | No game is currently active. `startNewGame()` is called. | A new `Board` is created for the game. | :white_check_mark: |
| GAME-START-002 | No game is currently active. `startNewGame()` is called. | The new board is initialized with the starting chess pieces. | :white_check_mark: |
| GAME-START-003 | A game is already active. `startNewGame()` is called again. | The existing board is replaced with a clean, newly initialized board. No old board state remains. | :white_check_mark: |

---

## Method under test: `getBoard()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| GAME-BOARD-001 | A game has not started yet. `getBoard()` is called. | Returns `null` because no board has been created yet. | :x: |
| GAME-BOARD-002 | A game has started. `getBoard()` is called. | Returns the current initialized `Board`. | :x: |

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