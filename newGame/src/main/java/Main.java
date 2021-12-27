import progress.Game;
import progress.State;

public class Main{
    Game game;

    public Main(){
        this.game = new Game();

        while (true){
            try {
                Thread.sleep(60);
                game.repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



    public static void main(String[] args) {
        new Main();
    }


}
