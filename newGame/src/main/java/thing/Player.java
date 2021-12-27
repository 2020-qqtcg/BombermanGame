package thing;

public class Player extends Creature{
    public Player(int live, World world) {
        super((char)7, live, world);
    }

    public String getName(){
        return "player";
    }
}
