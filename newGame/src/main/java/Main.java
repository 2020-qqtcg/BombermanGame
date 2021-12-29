import network.GameClient;
import progress.Game;


import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        int n;
        System.out.print("请输入将要进行游戏的玩家数目(1or2)：");
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        while (n != 1 && n != 2){
            System.out.print("请重新输入：");
            n = in.nextInt();
        }
        if (n == 1){
            Game game = new Game();
            while (true){
                try {
                    Thread.sleep(60);
                    game.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            GameClient client = new GameClient("127.0.0.1", 9000);
        }
    }


}
