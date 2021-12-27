package progress;

import thing.Monster;
import thing.World;

import java.util.Random;

public class MonsterThread implements Runnable{
    private Monster monster;
    private World world;
    Random r = new Random();

    public MonsterThread(World world){
        this.world = world;
        this.monster = new Monster(1, world);
        // 随机产生位置让word去看能不能放置成功

        while (!world.setMonster(monster, r.nextInt(World.WIDTH), r.nextInt(World.HEIGHT))){
        }
        System.out.println("year");

    }


    @Override
    public void run() {
        while (monster.isLive() && !State.state.equals("end")){
            if (State.state.equals("run")){
                int x = monster.getX();
                int y = monster.getY();
                switch (r.nextInt(5)){
                    case 0 -> {world.canGoUp(x, y);}
                    case 1 -> {world.canGoRight(x, y);}
                    case 2 -> {world.canGoDown(x, y);}
                    case 3 -> {world.canGoLeft(x, y);}
                    case 4 -> {
                        if(r.nextInt(30) == 0){
                            new Thread(()->monster.attack()).start();}
                    }

                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

        world.dieMonster(monster);
    }
}
