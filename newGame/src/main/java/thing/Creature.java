package thing;

public class Creature extends Thing {
    private int x;
    private int y;
    private int numberOfBloom;
    private World world;

    public Creature(char sign, int live, World world) {
        super(sign, live);
        this.world = world;
    }

    public void attack(){

        Bloom b = world.setBloom(x, y);
        if (b != null){
            try {
                b.explore();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public boolean moveUp(){
        if (this.y - 1 >= 0){
            y --;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveDown(){
        if (this.y + 1 >= 0){
            y ++;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveLeft(){
        if (this.x - 1 >= 0){
            x --;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveRight(){
        if (this.x + 1 >= 0){
            x ++;
            return true;
        }
        else {
            return false;
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

}
