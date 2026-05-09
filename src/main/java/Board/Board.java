package Board;

public class Board {

    Piece[][] state;

    public Board() {
        this.state = new Piece[8][8];
    }

    public void initializeBoard() {
        // first create the top row, which will be the first
        // row at the top assuming that you are sitting on the
        // white side.
        this.state[0] = generateBoundaryRows("BLACK");
        this.state[1] = generatePawnRow("BLACK");

        this.state[6] = generatePawnRow("WHITE");
        this.state[7] = generateBoundaryRows("WHITE");

        return;
    }

    public Piece getPieceAt(int row, int col) {
        return this.state[row][col];
    }

    private Piece[] generateBoundaryRows(String color) {
        Piece[] row = new Piece[8];

        row[0] = new Piece("ROOK", color);
        row[1] = new Piece("KNIGHT", color);
        row[2] = new Piece("BISHOP", color);
        row[3] = new Piece("QUEEN", color);
        row[4] = new Piece("KING", color);
        row[5] = new Piece("BISHOP", color);
        row[6] = new Piece("KNIGHT", color);
        row[7] = new Piece("ROOK", color);

        return row;
    }

    private Piece[] generatePawnRow(String color) {
        Piece[] row = new Piece[8];

        for (int col = 0; col < 8; col++) {
            row[col] = new Piece("PAWN", color);
        }

        return row;
    }
}
