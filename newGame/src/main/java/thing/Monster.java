package thing;

public class Monster extends Creature{
    public Monster(int live, World world) {
        super((char)2, live, world);
    }

    public String getName(){
        return "monster";
    }
}
