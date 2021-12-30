package network.doubleGame;

import thing.Thing;

import java.io.Serializable;

/**
 * 用于在网络中传输的数据包
 */
public class Packge implements Serializable {
    private Thing[][] things;
    private int lives;

    public Packge(Thing[][] things, int lives){
        this.lives = lives;
        this.things = things;
    }

    public int getLives(){
        return lives;
    }

    public Thing[][] getThings(){
        return things;
    }
}
