package thing;

import map.Map;
import progress.CreateMonster;
import progress.MonsterThread;
import progress.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class World implements Serializable {
    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;
    private Map map;
    private Thing[][] things = new Thing[WIDTH][HEIGHT];
    private Player player1;
    private Player player2;
    public static int numberOfMonster;
    private ArrayList<MonsterThread> process = new ArrayList<>();
    private ArrayList<Runnable> explores= new ArrayList<>();

    public World(){
        map = new Map(World.WIDTH);
        player1 = new Player(2, this);  // 玩家出生在游戏的左上角
        int[][] mapInt = map.getMap();
        // 初始化地图
        Random r = new Random();
        // 利用随机数随机摆放物品
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                if (i == 0 && j == 0){
                    things[i][j] = player1;
                    things[i][j].setPosition(i, j);
                }
                else if (mapInt[i][j] == 1){
                    things[i][j] = new Floor();
                    things[i][j].setPosition(i, j);
                }
                else {
                    switch (r.nextInt(3)){
                        case 0 -> {things[i][j] = new Flower();things[i][j].setPosition(i, j);}
                        case 1 -> {things[i][j] = new Stone();things[i][j].setPosition(i, j);}
                        case 2 -> {things[i][j] = new Tree();things[i][j].setPosition(i, j);}
                    }
                }
            }
        }
        numberOfMonster = 6;
        new CreateMonster(numberOfMonster, this);
    }

    public Thing[][] getThings(){
        return things;
    }

    public int getHP(){
        return player1.getLive();
    }


    /**
     * 放置完炸弹后为了防止重叠会上右下左顺序判断是否可走并进行一次强制移动
     */
    public synchronized Bloom setBloom(int x, int y){
        Creature creature = (Creature) things[x][y];

        if (y-1 >= 0 && things[x][y-1].getSign() == (char)5){
            creature.moveUp();
            things[x][y-1] = creature;
            things[x][y] = new Bloom(2000, 1, this);
            things[x][y].setPosition(x, y);
            return (Bloom) things[x][y];
        }
        else if (x+1 <= WIDTH-1 && things[x+1][y].getSign() == (char)5){
            creature.moveRight();
            things[x+1][y] = creature;
            things[x][y] = new Bloom(2000, 1, this);
            things[x][y].setPosition(x, y);
            return (Bloom) things[x][y];
        }
        else if (y+1 <= HEIGHT-1 && things[x][y+1].getSign() == (char)5){
            creature.moveDown();
            things[x][y+1] = creature;
            things[x][y] = new Bloom(2000, 1, this);
            things[x][y].setPosition(x, y);
            return (Bloom) things[x][y];
        }
        else if (x-1 >= 0 && things[x-1][y].getSign() == (char)5){
            creature.moveLeft();
            things[x-1][y] = creature;
            things[x][y] = new Bloom(2000, 1, this);
            things[x][y].setPosition(x, y);
            return (Bloom) things[x][y];
        }
        else {
            return null;
        }
    }

    public synchronized void dealExplore(int x, int y, int range){
        // 上右下左检测爆炸范围内物体

        for(int i = 1; i <= range; i++){
            if (y - i >= 0){
                if (things[x][y-i].beBroke()){       // 被破坏了,beBroke自带扣一滴血
                    things[x][y-i] = new Floor();
                    things[x][y-i].setPosition(x, y-i);

                }
            }
            if (x + i <= WIDTH ){
                if (things[x+i][y].beBroke()){
                    things[x+i][y] = new Floor();
                    things[x+i][y].setPosition(x+i, y);
                }
            }
            if (y + i <= HEIGHT ){
                if (things[x][y+i].beBroke()){
                    things[x][y+i] = new Floor();
                    things[x][y+i].setPosition(x, y+i);
                }
            }
            if (x - i >= 0 ){
                if (things[x-i][y].beBroke()){
                    things[x-i][y] = new Floor();
                    things[x-i][y].setPosition(x-i, y);
                }
            }
        }
        things[x][y] = new Floor();
        things[x][y].setPosition(x, y);
    }



    public synchronized void canGoUp(int x, int y){
        Creature creature;
        if (y-1 >= 0 && things[x][y-1].getSign() == (char)5){
            creature = (Creature) things[x][y];
            creature.moveUp();
            things[x][y] = new Floor();
            things[x][y].setPosition(x, y);
            things[x][y-1] = creature;
        }

    }

    public synchronized void canGoDown(int x, int y){
        Creature creature;

        if (y+1 <= HEIGHT-1 && things[x][y+1].getSign() == (char)5){
            creature = (Creature) things[x][y];
            creature.moveDown();
            things[x][y] = new Floor();
            things[x][y].setPosition(x, y);
            things[x][y+1] = creature;
        }

    }

    public synchronized void canGoLeft(int x, int y){
        Creature creature;

        if (x-1 >= 0 && things[x-1][y].getSign() == (char)5){
            creature = (Creature) things[x][y];
            creature.moveLeft();
            things[x][y] = new Floor();
            things[x][y].setPosition(x, y);
            things[x-1][y] = creature;
        }

    }

    public synchronized void canGoRight(int x, int y){
        Creature creature;

        if (x+1 <= WIDTH-1 && things[x+1][y].getSign() == (char)5){
            creature = (Creature) things[x][y];
            creature.moveRight();
            things[x][y] = new Floor();
            things[x][y].setPosition(x, y);
            things[x+1][y] = creature;
        }

    }

    public synchronized boolean setMonster(Monster monster, int x, int y){
        if(x + y > 4 && things[x][y].getSign() == (char)5){         // 避免产生的位置距离玩家太近
            things[x][y] = monster;
            monster.setPosition(x, y);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 添加线程
     * @param mt
     */
    public synchronized void listenMonsterThread(MonsterThread mt){
        process.add(mt);
    }

    /**
     * 移除该线程
     * @param mt
     */
    public synchronized void removeMonsterThread(MonsterThread mt){
        process.remove(mt);
    }

//    public synchronized void dieMonster(Monster monster){
//        numberOfMonster--;
//    }

    public int getNumberOfMonster(){
        return process.size();
    }

    public Player getPlayer1(){
        return player1;
    }

    public synchronized void reStart(){
        ExecutorService exec = Executors.newFixedThreadPool(process.size());

        for (Runnable r : process){
            exec.execute(r);
        }
        exec.shutdown();
        reSetExplore();
    }


    public synchronized void addExplore(Runnable t){
        explores.add(t);
    }

    public synchronized void removeExplore(Runnable t){
        explores.remove(t);
    }

    public void reSetExplore(){
        if (explores.size() != 0){
            ExecutorService exec = Executors.newFixedThreadPool(explores.size());

            for (Runnable r : explores){
                exec.execute(r);
            }
            exec.shutdown();
        }

    }

}
