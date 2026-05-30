## Game Rules

This project implements a simplified version of chess. The goal is to model the main structure of a chess game without implementing every advanced rule from official chess.

The game starts with the standard chess board setup. Each player begins with the normal set of pieces: one king, one queen, two rooks, two bishops, two knights, and eight pawns. White moves first, followed by Black, and players continue alternating turns after each valid move.

Players move pieces by choosing a starting square and an ending square. A move is only accepted if there is a piece on the starting square, the piece belongs to the current player, the ending square is within the board, and the piece can legally move to that square based on its movement rules. The game enforces legal movement for pawns, rooks, bishops, knights, queens, and kings.

Captures are allowed when a player legally moves one of their pieces onto a square occupied by an opponent's piece. A player may not capture their own piece. Sliding pieces, such as rooks, bishops, and queens, cannot move through other pieces.

This version uses a simplified win condition: the game ends when a king is captured. Full check, checkmate, castling, promotion, and en passant are not required for this version. Once a player captures the opponent's king, the game is over and that player is declared the winner.

After the game is over, no further moves are allowed.