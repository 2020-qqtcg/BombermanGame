package thing;

public class Goods extends Thing{
    private final boolean canBroke;


    public Goods(char sign, int live, boolean canBroke) {
        super(sign, live);
        this.canBroke = canBroke;
    }

    private boolean isCanBroke(){
        return canBroke;
    }

    /**
     * 返回破坏后的状态
     * @return true为存在，false为被完全破坏
     */
    public boolean beBroke(){
        if (isCanBroke()){
            this.hurt();

        }
        return !this.isLive();
    }
}
