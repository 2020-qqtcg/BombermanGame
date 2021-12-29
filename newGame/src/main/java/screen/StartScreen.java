package screen;

import asciiPanel.AsciiPanel;
import thing.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class StartScreen implements Screen{



    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write((char)1, 0, 0);
        terminal.write("Welcome to my game!",5,8);
        terminal.write("A->start",5,10);
        terminal.write("R->last game",5,12);
        terminal.write("N->stop", 5, 14);

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
