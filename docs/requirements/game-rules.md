## Game Rules

This project implements a full chess game using the standard rules of chess.

The game starts with the standard chess board setup. Each player begins with one king, one queen, two rooks, two bishops, two knights, and eight pawns. White moves first, followed by Black, and players alternate turns after each valid move.

Players move pieces by choosing a starting square and an ending square. A move is accepted only if there is a piece on the starting square, the piece belongs to the current player, the ending square is within the board, and the piece can legally move to that square  based on its movement rules.

The game enforces legal movement for pawns, rooks, bishops, knights, queens, and kings. Captures are allowed when a player legally  moves one of their pieces onto a square occupied by an opponent's piece. A player may not capture their own piece. Sliding pieces,  such as rooks, bishops, and queens, cannot move through other pieces.

Kings are not captured in the final rule set. Instead, the game detects check and checkmate. A player may not make a move that leaves their own king in check. The game ends when a player is checkmated, and the opposing player is declared the winner.

The game also supports draw conditions. Stalemate ends the game as a draw. Additional draw rules, including insufficient material, the fifty-move rule, and threefold repetition, are part of the full rule set.

Special chess rules are part of the full rule set, including castling, en passant, and pawn promotion. Pawn promotion allows a pawn reaching the final rank to promote to a queen, rook, bishop, or knight.

After the game is over, no further moves are allowed.
