package screen;

import asciiPanel.AsciiPanel;
import thing.World;

import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ChoiceScreen implements Screen{
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write((char)1, 0, 0);
        terminal.write("R -> continue the last game",5,8);
        terminal.write("A -> single",5,10);
        terminal.write("B -> double", 5, 12);

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_A) {
            return new WorldScreen(new World());
        }
        else if (key.getKeyCode() == KeyEvent.VK_R){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/java/file/data.txt"));
                WorldScreen worldScreen = (WorldScreen)ois.readObject();
                worldScreen.reSetThread();
                return worldScreen;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }
}
