**Note:** Sorry for submitting these planning/progress updates so late. We reconstructed these retroactively from our GitHub PR history and team memory, so some details such as exact PR links may be incomplete or missing. Thank you for the grace in letting us fill this in this late.

# Week 6 (05/04/2026-05/10/2026)

**Planning and Progress Tracking**:

1. [done] Mukhtar: Added BVA analysis for the project.
2. [done] Taha: Completed the first three Board methods using TDD and added tests for the initial Board behavior.
3. [done] Saeed: Implemented initial Game and Player functionality.
4. [done] Team: Established the first working pieces of the Board, Game, Player, and BVA structure.

# Week 7 (05/11/2026-05/17/2026)

**Planning and Progress Tracking**:

1. [done] Mukhtar: Added `Board.movePiece` with relevant tests.
2. [done] Taha: Set up SpotBugs and Checkstyle in the Gradle workflow to support static analysis and code quality checks.
3. [done] Taha: Added project configuration for SpotBugs and Checkstyle reporting.
4. [done] Saeed: Updated file names to follow the project naming conventions.
5. [done] Team: Continued cleaning up the project structure and preparing the codebase for later testing, coverage, and gameplay work.

# Week 8 (05/18/2026-05/24/2026)

**Planning and Progress Tracking**:

1. [done] Taha: Documented the expected Game board setup behavior in the BVA documentation.
2. [done] Taha: Implemented Game board integration so `startNewGame()` creates and initializes a Board.
3. [done] Taha: Added `getBoard()` behavior and tests for returning the current board after a game starts.
4. [done] Taha: Added tests for resetting/replacing the board when `startNewGame()` is called again.
5. [done] Team: Addressed Week 6 and Week 7 instructor code review feedback.
6. [done] Team: Continued improving the connection between BVA documentation, tests, and implementation.

# Week 9 (05/25/2026-05/31/2026)

**Planning and Progress Tracking**:

1. [done] Saeed: Set up JaCoCo and Pitest for code coverage and mutation testing.
2. [done] Saeed: Cleaned up Board state access and added Piece tests.
3. [done] Saeed: Added Game turn flow through `makeMove`.
4. [done] Saeed: Expanded Board move rules for gameplay.
5. [done] Taha: Added game-over state tracking to the Game class.
6. [done] Taha: Added winner tracking with `getWinnerColor()`.
7. [done] Taha: Updated `makeMove(...)` so capturing the opponent’s king ends the game.
8. [done] Taha: Prevented moves from being made after the game is already over.
9. [done] Taha: Added tests for new games starting as not over, White winning by capturing the Black king, Black winning by capturing the White king, and rejecting moves after game over.
10. [done] Taha: Added a basic command-line UI in `Main.java` and updated the run configuration so `./gradlew run` accepts terminal input.
11. [done] Taha: Updated game rules and BVA documentation to describe simplified chess and the king-capture win condition.

# Week 10 (06/01/2026-06/07/2026)

**Planning and Progress Tracking**:

1. [done] Saeed: Added check detection helpers.
2. [done] Saeed: Prevented moves that leave the king in check.
3. [done] Saeed: Added checkmate and stalemate game-ending conditions.
4. [done] Mukhtar: Added pawn promotion to `Board.movePiece`.
5. [done] Mukhtar: Added castling with rights and check rules.
6. [done] Saeed: Added en passant support.
7. [done] Mukhtar: Added draw rules and move history.
8. [done] Taha: Reviewed gameplay-rule changes and helped verify their interaction with the existing Game flow.
9. [done] Team: Added Week 10 feedback documentation and continued aligning the project with instructor feedback.

# Week 11 (06/08/2026-06/10/2026)

**Planning and Progress Tracking**:

1. [done] Mukhtar: Added GUI MVC shell with CLI and Swing launch.
2. [done] Mukhtar: Added GUI board, move interaction, status display, and new game support.
3. [done] Mukhtar: Polished GUI behavior, added promotion UI, move history, and verification.
4. [done] Mukhtar: Added Swing GUI internationalization with message bundles and locale support.
5. [done] Taha: Refactored Game and Board to improve testability and clean up implementation details.
6. [done] Taha: Added package-private constructors for dependency injection in Game and Board.
7. [done] Taha: Refactored `Game.makeMove(...)` into smaller helper methods for move validation, move application, castling path validation, simulated move/self-check validation, completed move recording, and game-ending state updates.
8. [done] Taha: Extracted castling-specific logic and simulated board/self-check logic into clearer helper methods.
9. [done] Taha: Replaced repeated magic numbers and string literals with named constants in Game and Board.
10. [done] Taha: Refactored Game BVA tests into true unit tests using EasyMock, with Board and Player mocked/stubbed instead of relying on real implementations.
11. [done] Taha: Cleaned up spacing, formatting, and outdated comments while preserving existing gameplay behavior.
