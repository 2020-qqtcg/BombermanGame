package network.doubleGame;

import thing.Thing;

import java.io.Serializable;

/**
 * 用于在网络中传输的数据包
 */
public class Packge implements Serializable {
    private Thing[][] things;
    private int live0;
    private int live1;

    public Packge(Thing[][] things, int live0, int live1){
        this.live0 = live0;
        this.live1 = live1;
        this.things = things;
    }

    public int getLive0(){
        return live0;
    }

    public int getLive1(){
        return live1;
    }

    public Thing[][] getThings(){
        return things;
    }
}
