package screen;

import asciiPanel.AsciiPanel;
import progress.State;
import thing.World;

import java.awt.event.KeyEvent;

public class EndScreen implements Screen{
    private final boolean result;

    /**
     *
     * @param result win or not
     */
    public EndScreen(boolean result){
        this.result = result;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.changeFontSize(40);
        terminal.write((char)1, 0, 0);
        if (result){
            terminal.write("Congratulations!",5,8);
        }
        else {
            terminal.write("Don't be sad.",5,8);
        }

        terminal.write("press B to reStart",5,10);

    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        if (key.getKeyCode() == KeyEvent.VK_B) {
            State.state = "run";
            return new WorldScreen(new World());
        }
        return this;
    }
}
