package screen;

import asciiPanel.AsciiPanel;
import network.GameClient;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.Serializable;

public class ClientStartScreen implements Screen, Serializable {
    GameClient gc;

    public ClientStartScreen(GameClient gc){
        this.gc = gc;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write((char)1, 0, 0);
        terminal.write("Welcome to game!",5,8);
        terminal.write("A->start",5,10);
//        terminal.write("P->add Player", 5, 10);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_A) {
            try {
                gc.send("A");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ClientScreen(gc);
        }

        return this;
    }
}
