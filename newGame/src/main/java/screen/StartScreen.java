package screen;

import asciiPanel.AsciiPanel;
import thing.World;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StartScreen implements Screen{



    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write((char)1, 0, 0);
        terminal.write("Welcome to my game!",5,8);
        terminal.write("press B to start",5,10);
        terminal.write("press N to stop", 5, 12);

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_B) {
            return new WorldScreen(new World());
        }
        return this;

    }
}
