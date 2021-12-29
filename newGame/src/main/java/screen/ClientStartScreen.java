package screen;

import asciiPanel.AsciiPanel;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class ClientStartScreen implements Screen, Serializable {
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write((char)1, 0, 0);
        terminal.write("Welcome to game!",5,8);
        terminal.write("A->start",5,10);
        terminal.write("P->add Player", 5, 10);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_A) {
            return this;
        }
        else if (key.getKeyCode() == KeyEvent.VK_P){

        }
        return this;
    }
}
