package screen;

import asciiPanel.AsciiPanel;
import network.GameClient;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class ClientScreen implements Screen, Serializable {
    GameClient gc;

    public ClientScreen(GameClient gc){
        this.gc = gc;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {

//        switch (key.getKeyCode()){
//            case KeyEvent.VK_W -> world.canGoUp(x, y);
//            case KeyEvent.VK_S -> world.canGoDown(x, y);
//            case KeyEvent.VK_A -> world.canGoLeft(x, y);
//            case KeyEvent.VK_D -> world.canGoRight(x, y);
//            case KeyEvent.VK_J -> new Thread(()->player1.attack()).start();
//        }
        return this;
    }
}
