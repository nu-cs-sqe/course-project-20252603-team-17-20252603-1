## Game Rules

This project implements a full chess game using the standard rules of chess.

The game starts with the standard chess board setup. Each player begins with one king, one queen, two rooks, two bishops, two knights, and eight pawns. White moves first, followed by Black, and players alternate turns after each valid move.

Players move pieces by choosing a starting square and an ending square. A move is accepted only if there is a piece on the starting square, the piece belongs to the current player, the ending square is within the board, and the piece can legally move to that square  based on its movement rules.

The game enforces legal movement for pawns, rooks, bishops, knights, queens, and kings. Captures are allowed when a player legally  moves one of their pieces onto a square occupied by an opponent's piece. A player may not capture their own piece. Sliding pieces,  such as rooks, bishops, and queens, cannot move through other pieces.

Kings are not captured in the final rule set. Instead, the game detects check and checkmate. A player may not make a move that leaves their own king in check. The game ends when a player is checkmated, and the opposing player is declared the winner.

The game also supports draw conditions. Stalemate ends the game as a draw. Additional draw rules, including insufficient material, the fifty-move rule, and threefold repetition, are part of the full rule set.

The fifty-move rule is enforced with a half-move clock: it counts moves without a pawn move or capture; it resets on every pawn move and on every capture; when the count reaches 100, the game is declared drawn. Threefold repetition ends the game as a draw when the same position (board plus side to move, and any rights the implementation includes in its position key) occurs three times.

Game result and draw reason: The game exposes whether it is over (isGameOver()), whether the result is a draw (isDraw()), the winner's color when applicable (getWinnerColor() is null for any draw), and a getDrawReason() string for draws and for non-draw game-over clarity. Until a draw occurs or the game ends, getDrawReason() may be null. When the game ends in a draw, the reason uses stable tokens such as: STALEMATE, INSUFFICIENT_MATERIAL, FIFTY_MOVE, THREEFOLD_REPETITION (exact set implemented in code). When the game ends in checkmate, isDraw() is false, getWinnerColor() is the winner, and getDrawReason() is null (or CHECKMATE if the implementation chooses to set a non-null reason for UI; document in code). A new game clears all result fields.

Special chess rules are part of the full rule set, including castling, en passant, and pawn promotion. Castling is a single move of the king two squares toward a rook on its starting corner, with that rook jumping to the square the king crossed; it is allowed only if neither piece has moved, all squares between them are empty, the king is not in check, and the king does not pass through or land on a square attacked by the opponent. Pawn promotion allows a pawn reaching the final rank to promote to a queen, rook, bishop, or knight. If no promotion piece is specified (for example in the command-line interface), the pawn promotes to a queen by default.

After the game is over, no further moves are allowed.
