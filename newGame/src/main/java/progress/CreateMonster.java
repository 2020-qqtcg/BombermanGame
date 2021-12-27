package progress;

import thing.World;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CreateMonster {
    private final int numberOfMonster;
    private World world;

    public CreateMonster(int n, World world){
        numberOfMonster = n;
        this.world = world;
        ExecutorService exec = Executors.newFixedThreadPool(numberOfMonster);
        for (int i = 0; i < numberOfMonster; i++){
            exec.execute(new MonsterThread(world));
        }
        exec.shutdown();
        System.out.println("Create Successfully！");
    }




}
