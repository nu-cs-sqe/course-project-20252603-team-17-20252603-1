package player;

public class Player {

    private final String name;
    private final String color;

    public Player(String name, String color){

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }


        this.name = name;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public String getColor() {
        return color;
    }
}
