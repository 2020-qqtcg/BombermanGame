package thing;

import java.io.Serializable;

public class Thing implements Serializable {
    private int x;
    private int y;
    private int live;
    private final char sign;


    public Thing(char sign, int live){
        this.sign = sign;
        this.live = live;

    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }


    public char getSign(){
        return sign;
    }

    public void hurt(){
        live--;
    }

    public void care(){
        live++;
    }

    public boolean isLive(){
        return live > 0;
    }

    public int getLive(){
        return live;
    }

    /**
     *
     * @return 是否被破坏， 破坏返回true
     */
    public boolean beBroke(){
        this.hurt();
        return !isLive();
    }

    public String getName(){
        return "thing";
    }
}
