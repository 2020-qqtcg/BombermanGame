package thing;

public class Flower extends Goods{
    public Flower() {
        super((char)4, 1, true);
    }

    public String getName(){
        return "flower";
    }
}
