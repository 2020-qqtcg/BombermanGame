package screen;

import asciiPanel.AsciiPanel;
import network.GameClient;
import network.doubleGame.Transfer;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;

public class ClientScreen implements Screen, Serializable {
    GameClient gc;


    public ClientScreen(GameClient gc){
        this.gc = gc;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        String data = gc.getData();

        if (data != null){
            if (data.length() == 632){
                data = data.substring(5);
                char[][] things = Transfer.outTransferThings(data.substring(0, 25*25));
                int[] temp = Transfer.outTransferLive(data.substring(25*25, 25*25+2));
                int live0 = temp[0];
                int live1 = temp[1];
                // 显示游戏界面
                for (int i = 0; i < World.WIDTH; i++){
                    for (int j = 0; j < World.HEIGHT; j++){
                        terminal.write(things[i][j], i, j);
                    }
                }
                terminal.changeFontSize(20);
                // 显示游戏右侧菜单界面
                terminal.write("HP: "+ live0, World.WIDTH+2, 3);
                terminal.write("HP: "+ live1, World.WIDTH+2, 4);
                terminal.write("W->up", World.WIDTH+1, 9);
                terminal.write("S->down", World.WIDTH+1, 10);
                terminal.write("A->left", World.WIDTH+1, 11);
                terminal.write("D->right", World.WIDTH+1, 12);
                terminal.write("J->attack", World.WIDTH+1, 13);
                terminal.write("when stop", World.WIDTH+1, 13);
                terminal.write("L->save", World.WIDTH+1, 13);
            }

        }





    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        try {
            switch (key.getKeyCode()){
                case KeyEvent.VK_W -> gc.send("W");
                case KeyEvent.VK_S -> gc.send("S");
                case KeyEvent.VK_A -> gc.send("A");
                case KeyEvent.VK_D -> gc.send("D");
                case KeyEvent.VK_J -> gc.send("J");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return this;
    }
}
