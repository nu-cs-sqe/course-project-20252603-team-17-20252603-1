# BVA Analysis for `Player` Class

## Method under test: `getColor()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| PLAYER-COLOR-001 | A White player exists. `getColor()` is called. | Returns `WHITE`. | :white_check_mark: |
| PLAYER-COLOR-002 | A Black player exists. `getColor()` is called. | Returns `BLACK`. | :white_check_mark: |

---

## Method under test: `getName()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| PLAYER-NAME-001 | Player is created with name `"White Player"`. `getName()` is called. | Returns `"White Player"`. | :white_check_mark: |
| PLAYER-NAME-002 | Player is created with name `"Black Player"`. `getName()` is called. | Returns `"Black Player"`. | :white_check_mark: |
| PLAYER-NAME-003 | Player is created with an empty name `""`. | System rejects empty name by throwing an IllegalArgumentException. | :white_check_mark: |