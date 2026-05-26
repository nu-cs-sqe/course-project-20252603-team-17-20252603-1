# BVA Analysis for `Piece` Class

## Method under test: `getColor()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|-----------|
| PIECE-COLOR-001 | A White piece exists. `getColor()` is called. | Returns `WHITE`. | :white_check_mark: |
| PIECE-COLOR-002 | A Black piece exists. `getColor()` is called. | Returns `BLACK`. | :white_check_mark: |

---

## Method under test: `getType()`

| Test Case ID | State of the System | Expected Output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| PIECE-TYPE-001 | A King piece exists. `getType()` is called. | Returns `KING`. | :white_check_mark:  |
| PIECE-TYPE-002 | A Queen piece exists. `getType()` is called. | Returns `QUEEN`. | :white_check_mark:          |
| PIECE-TYPE-003 | A Rook piece exists. `getType()` is called. | Returns `ROOK`. | :white_check_mark:          |
| PIECE-TYPE-004 | A Bishop piece exists. `getType()` is called. | Returns `BISHOP`. | :white_check_mark:          |
| PIECE-TYPE-005 | A Knight piece exists. `getType()` is called. | Returns `KNIGHT`. | :white_check_mark:          |
| PIECE-TYPE-006 | A Pawn piece exists. `getType()` is called. | Returns `PAWN`. | :white_check_mark:          |