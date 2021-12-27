package screen;

import asciiPanel.AsciiPanel;
import progress.State;
import thing.Player;
import thing.Thing;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class WorldScreen implements Screen, Serializable {
    private World world;
    private Thing[][] things;
    private Player player1;

    public WorldScreen(World world){
        this.world = world;
        things = world.getThings();
        player1 = world.getPlayer1();

    }


    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.changeFontSize(20);

        // 显示游戏界面
        for (int i = 0; i < World.WIDTH; i++){
            for (int j = 0; j < World.HEIGHT; j++){
                terminal.write(things[i][j].getSign(), i, j);
            }
        }

        // 显示游戏右侧菜单界面
        terminal.write("HP: "+ world.getHP(), World.WIDTH+2, 3);
        terminal.write("W->up", World.WIDTH+1, 9);
        terminal.write("S->down", World.WIDTH+1, 10);
        terminal.write("A->left", World.WIDTH+1, 11);
        terminal.write("D->right", World.WIDTH+1, 12);
        terminal.write("J->attack", World.WIDTH+1, 13);

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (!player1.isLive()){
            State.state = "end";
            return new EndScreen(false);
        }
        else if (World.numberOfMonster == 0){
            State.state = "end";
            return new EndScreen(true);
        }
        int x = player1.getX();
        int y = player1.getY();
        if (key.getKeyCode() == KeyEvent.VK_N){
            State.state = (State.state.equals("run")) ? "stop" : "run";
        }
        if (State.state.equals("run")){
            switch (key.getKeyCode()){
                case KeyEvent.VK_W -> world.canGoUp(x, y);
                case KeyEvent.VK_S -> world.canGoDown(x, y);
                case KeyEvent.VK_A -> world.canGoLeft(x, y);
                case KeyEvent.VK_D -> world.canGoRight(x, y);
                case KeyEvent.VK_J -> new Thread( ()->{player1.attack();} ).start();
            }
        }

        return this;
    }
}
