package progress;

import thing.Bloom;
import thing.World;

import java.io.Serializable;

public class DealExplore implements Runnable, Serializable {
    Bloom b;
    World world;

    public DealExplore(Bloom b, World world){
        this.b = b;
        this.world = world;
        world.addExplore(this);

    }
    @Override
    public void run() {
        try {
            b.explore();
        } catch (Exception e) {
            e.printStackTrace();
        }
        world.removeExplore(this);
    }
}
