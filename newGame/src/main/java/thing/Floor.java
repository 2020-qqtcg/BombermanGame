package thing;

public class Floor extends Goods{
    public Floor() {
        super((char)5, 1, false);
    }

    public String getName(){
        return "Floor";
    }
}
