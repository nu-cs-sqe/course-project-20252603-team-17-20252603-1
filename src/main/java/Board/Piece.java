package Board;

public class Piece {
    private final String type;
    private final String color;

    public Piece(String type, String color) {
        this.type = type;
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }
}