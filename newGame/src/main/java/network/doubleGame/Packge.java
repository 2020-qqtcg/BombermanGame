package network.doubleGame;

import thing.Thing;

import java.io.Serializable;

public class Packge implements Serializable {
    Thing[][] things;

    public Packge(Thing[][] things){
        this.things = things;
    }
}
