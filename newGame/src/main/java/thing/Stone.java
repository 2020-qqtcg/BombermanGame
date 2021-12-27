package thing;

public class Stone extends Goods{
    public Stone() {
        super((char)6, 1, false);
    }

    public String getName(){
        return "stone";
    }
}
