![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-17-20252603-1/actions/workflows/main.yml/badge.svg)

[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23635769)
# PROJECT NAME
Chess Game
## Contributors
- Taha Azeem
- Mukhtar Handulle
- Saeed AlKaabi

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10

## Run

- CLI (default): `./gradlew run`
- Swing shell: `./gradlew run --args='gui'`
  - **Locale:** GUI strings load from `src/main/resources/messages.properties` (default English).
    For French, pass the JVM flag through Gradle before the task name, for example:
    `./gradlew -Dchess.locale=fr run --args='gui'`
  - In the GUI, click a piece of the side to move, then a destination square to move (same square
    again cancels the selection).
  - **Status:** the north label shows turn, in-check, and endgame results; invalid moves appear on
    the south label; the window title mirrors the north line.
  - **New game:** use the **New game** button in the window header to reset the board and status.
  - **Board polish:** the selected square is highlighted; click another of your pieces to change
    selection; an illegal destination clears the highlight; after checkmate/draw, board clicks do
    nothing until **New game**.
  - **Promotion:** when a pawn moves to the last rank, a dialog offers Queen (default), Rook,
    Bishop, or Knight; **Cancel** keeps your piece selected so you can pick another destination.
  - **Team Defined Feature: Move history:** the east **Move history** panel lists completed moves from the game model;
    it clears on **New game** and scrolls to the latest line as you play.

## Verification

Run these before merging GUI-related work:

- `./gradlew test`
- `./gradlew checkstyleMain`

`MoveHistoryFormatterTest` exercises the move-line formatter used by the east history panel (no GUI
head required). `UiMessagesTest` checks English/French resource bundles and `MessageFormat` wiring.

### GUI smoke checklist (`./gradlew run --args='gui'`)

1. Window opens; north status shows **WHITE to move**; title bar matches.
2. Select a white pawn square (highlight); click a legal forward square; piece moves and turn switches.
3. Click an illegal destination; south shows **Invalid move**; highlight clears; north still shows correct turn.
4. Put the side to move in **check** if you can; north shows **— check** and the title updates.
5. Reach **checkmate** or a **draw**; north shows endgame text; board clicks do nothing.
6. Click **New game**; starting position returns; history shows **(No moves yet.)**; you can move again.
7. **Promotion:** advance a pawn to the last rank; pick each piece type once; board shows the chosen piece; **Cancel** leaves selection so you can retry.
8. **Move history:** after several moves, east panel lists lines; newest is visible at the bottom; castling/capture/promotion lines look sensible when they occur.
9. Select a piece, then click **another friendly piece**; selection moves (no bogus illegal line).
10. Resize the window; board stays centered and still playable.



### CLI smoke checklist (`./gradlew run`)

1. Run with no args; complete one legal move using **startRow startCol endRow endCol** input.
2. Confirm an illegal coordinate set prints **Invalid move** and the loop continues.
3. Play until **Game over**; confirm the session ends cleanly.

## Acknowledgements

We used generative AI for coding assistance, documentation drafts, and debugging ideas. All outputs were reviewed and revised by the team before use.

