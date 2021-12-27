package thing;

import progress.State;

public class Bloom extends Thing{
    private int times;         // 爆炸时间，单位毫秒
    private int range;         // 爆炸范围
    private World world;
    private long birthTime;

    public Bloom(int times, int range, World world) {
        super((char)8, 1);
        this.times = times;
        this.range = range;
        this.world = world;
        birthTime = System.currentTimeMillis();
    }

    // 不懂这里为什么要加锁
    // 实现了炸弹的暂停
    public synchronized void explore() throws Exception{
//        Thread.sleep(this.times);          // 先放在这里，不过感觉放在外面更容易控制暂停
        boolean ifChange = true;
        while (times >= 0){
            while (State.state.equals("run")){
                Thread.sleep(1);
                if (System.currentTimeMillis() - birthTime >= times){
                    times = -1;

                    break;
                }
                ifChange = true;
            }
            if (State.state.equals("stop")){
                if (ifChange){
                    ifChange = false;
                    long temp = System.currentTimeMillis();
                    times -= temp - birthTime;
                }
                birthTime = System.currentTimeMillis();
                Thread.sleep(1);
            }
        }
        world.dealExplore(this.getX(), this.getY(), this.range);
        System.out.println(getName() + " explore");
    }

    public int getRange(){
        return range;
    }

    public String getName(){
        return "bloom";
    }


}
